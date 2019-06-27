package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.LimitQueue;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import com.e_commerce.miscroservice.csq_proj.service.CsqServiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDonateRecordVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundVo;
import com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 14:02
 */
@Transactional(rollbackFor = Throwable.class)
@Component
public class CsqServiceServiceImpl implements CsqServiceService {

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqUserDao userDao;

	@Autowired
	private CsqFundDao csqFundDao;

	@Autowired
	private CsqUserPaymentDao paymentDao;

	@Autowired
	private CsqPublishDao csqPublishDao;

	@Autowired
	private CsqPublishService csqPublishService;

	@Autowired
	private CsqFundService csqFundService;

	@Autowired
	private CsqUserPaymentDao csqUserPaymentDao;

	@Autowired
	private CsqUserServiceReportDao csqUserServiceReportDao;

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Autowired
	@Qualifier("csqRedisTemplate")
	HashOperations<String, String, Object> userRedisTemplate;

	public static String CSQ_GLOBAL_DONATE_BROADCAST = "csq:global:doante:broadcast";

	@Override
	public void publish(Long userId, TCsqService service) {
		//check用户是否满足发布项目的前置条件
		//1.组织账户
		//2.已通过机构认证
		TCsqUser user = userDao.selectByPrimaryKey(userId);
//		checkPubAuth(user);	//最好放在填写之前

		String name = service.getName();
		//check字段完整性
		if (StringUtil.isAnyEmpty(name)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "必填字段为空!");
		}

		//check名下同名项目
		List<TCsqService> tCsqServices = csqServiceDao.selectByNameAndUserId(name, userId);
		if (!tCsqServices.isEmpty()) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "存在同名项目!");
		}

		//插入一条记录
		service.setId(null);
//		service.setStatus(CsqServiceEnum.STATUS_UNDER_CERT.getCode());
		service.setUserId(userId);
		service.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		service.setType(CsqServiceEnum.TYPE_SERIVE.getCode());
		csqServiceDao.insert(service);
	}

	@Override
	public QueryResult<TCsqService> list(Long userId, Integer option, Integer pageNum, Integer pageSize) {
		QueryResult<TCsqService> result = new QueryResult<>();
		pageNum = pageNum == null ? 1 : pageNum;
		pageSize = pageSize == null ? 0 : pageSize;
		Integer OPTION_ALL = 0;
		Integer OPTION_MINE = 2;

		Integer OPTION_DONATED = 1;
		option = option == null ? OPTION_ALL : option;

		Page<Object> startPage;
		List<TCsqService> tCsqServices;
		if (OPTION_MINE.equals(option)) {
			if (userId == null) {
				QueryResult<TCsqService> objectQueryResult = new QueryResult<>();
				objectQueryResult.setResultList(new ArrayList<>());
				//或抛出登录信号
				return objectQueryResult;
			}
			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = csqServiceDao.selectMine(userId);
		} else if (OPTION_ALL.equals(option)) {
			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = csqServiceDao.selectAll();
		} else if (OPTION_DONATED.equals(option)) {
			//找到我捐助过的项目记录
			List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndToTypeDesc(userId, CsqEntityTypeEnum.TYPE_SERVICE.toCode());
			//处理（去重等
			List<Long> uniqueServiceIds = tCsqOrders.stream()
				.map(TCsqOrder::getToId)
				.distinct()
				.collect(Collectors.toList());
			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = csqServiceDao.selectInIds(uniqueServiceIds);
		} else {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数option不正确!");
		}

		if(tCsqServices.isEmpty()) {
			return result;
		}

		//处理数据 -> 如果为基金，获取即时的信息
		List<Long> fundIds = tCsqServices.stream()
			.filter(a -> CsqServiceEnum.TYPE_FUND.getCode() == a.getType())    //基金类型
			.map(TCsqService::getFundId)
			.collect(Collectors.toList());
		List<TCsqFund> csqFunds = fundIds.isEmpty()? new ArrayList<>(): csqFundDao.selectInIds(fundIds);    //得到目标基金信息
		Map<Long, List<TCsqFund>> fundMap = csqFunds.stream()
			.collect(Collectors.groupingBy(TCsqFund::getId));
		List<TCsqService> csqServices = tCsqServices.stream()
			.map(a -> {
				Integer type = a.getType();
				if (CsqServiceEnum.TYPE_FUND.getCode() == type) {
					Long fundId = a.getFundId();
					List<TCsqFund> tCsqFunds = fundMap.get(fundId);
					if (tCsqFunds != null) {
						TCsqFund csqFund = tCsqFunds.get(0);
						//覆写相关信息
						a = transferAttrs(a, csqFund);
					}
				}
				return a;
			})
			.collect(Collectors.toList());
		result.setTotalCount(startPage.getTotal());
		result.setResultList(csqServices);
		return result;
	}

	@Override
	public Map<String, Object> detail(Long userId, Long serviceId) {
		Map<String, Object> resultMap = new HashMap<>();
		boolean isMine = false;
		boolean isFund = false;
		TCsqService tCsqService = csqServiceDao.selectByPrimaryKey(serviceId);
		// 判断是否为自己
		if (userId.equals(tCsqService.getUserId())) {
			isMine = true;
		}
		//进入查询过程
		Integer type = tCsqService.getType();

		if (CsqServiceEnum.TYPE_FUND.getCode() == type) {
			//查询基金
			isFund = true;
			Long fundId = tCsqService.getFundId();
			CsqFundVo csqFundVo = csqFundService.fundDetail(fundId);
			resultMap.put("csqService", csqFundVo);
			//基金没有播报信息
		} else {
			Double sumTotalIn = tCsqService.getSumTotalIn();
			Double surplusAmount = tCsqService.getSurplusAmount();
			tCsqService.setSumTotalOut(sumTotalIn - surplusAmount);    //剩余金额
			//捐入流水
			List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqPaymenEnum.INOUT_IN.toCode());    //TODO 分页
			List<Long> orderIds = tCsqUserPaymentRecords.stream()
				.map(TCsqUserPaymentRecord::getOrderId)
				.distinct().collect(Collectors.toList());
			tCsqUserPaymentRecords = paymentDao.selectInOrderIdsAndInOut(orderIds, CsqPaymenEnum.INOUT_OUT.toCode());
			//统计捐款数，获取top10
			Map<Long, List<TCsqUserPaymentRecord>> unsortedMap = tCsqUserPaymentRecords.stream()
				.collect(Collectors.groupingBy(TCsqUserPaymentRecord::getUserId));
			/*Map<Long, Double> reducedMap = new HashMap<>();
			unsortedMap.entrySet().stream()
				.forEach(a -> {
					List<TCsqUserPaymentRecord> value = a.getValue();
					Double reduce = value.stream().map(TCsqUserPaymentRecord::getMoney)
						.reduce(0d, Double::sum);
					reducedMap.put(a.getKey(), reduce);
				});
			Map<Long, Double> sortedMap = new LinkedHashMap<>();
			reducedMap.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getValue)).forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));
			sortedMap.entrySet().stream()
				.forEachOrdered(a -> a.getValue());*/

			Map<Long, Double> donaterMoneyMap = new HashMap<>();
			List<Long> donaterIds = new ArrayList<>();
			List<TCsqUser> donaters;
			unsortedMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Comparator.comparing(a -> {	//排序
					List<TCsqUserPaymentRecord> value = a.getValue();
					Double totalDonate = value.stream()
						.map(TCsqUserPaymentRecord::getMoney)
						.reduce(0d, Double::sum);
					donaterMoneyMap.put(a.getKey(), totalDonate);
					return totalDonate;
				}))).forEachOrdered(a -> {
					donaterIds.add(a.getKey());
				}
			);
			donaters = userDao.selectInIds(donaterIds);
			//纠正排序
			Map<Long, TCsqUser> donaterMap = new HashMap<>();
			donaters.stream()
				.forEach(donater -> donaterMap.put(donater.getId(), donater));
			donaters = donaterIds.stream()
				.map(a -> donaterMap.get(a)).collect(Collectors.toList());

			/*Map<String, Object> functionMap = new HashMap<>();
			for (Map.Entry entry : collect1.entrySet()) {
				Long theUserId = (Long) entry.getKey();
				List<TCsqUserPaymentRecord> value = (List<TCsqUserPaymentRecord>) entry.getValue();
				Double totalMoney = value.stream()
					.map(TCsqUserPaymentRecord::getMoney)
					.reduce(0d, (a, b) -> a + b);
				functionMap.put(theUserId.toString(), totalMoney);
				Double max = (Double) functionMap.get("max");
				max = max == null? 0: max;
				if (totalMoney >= max) {
					functionMap.put("max", totalMoney);
					List<Long> maxer = (List<Long>) functionMap.get(totalMoney.toString());
					if (totalMoney.equals(max)) {
						maxer.add(theUserId);
					} else {
						maxer = new ArrayList<>();
					}
					maxer.add(theUserId);
					functionMap.put(totalMoney.toString(), maxer);
				}
			}
			Double max = (Double) functionMap.get("max");
			List<TCsqUser> donaters = new ArrayList<>();
			if (max != null) {
				List<Long> maxer = (List<Long>) functionMap.get(max.toString());
				if (maxer != null) {
					donaters = userDao.selectInIds(maxer);
				}
			}*/
			donaters = donaters.stream()
				.map(a -> {
					Double totalDonate = donaterMoneyMap.get(a.getId());
					if (totalDonate != null) {
						a.setTotalDonate(totalDonate);
					}
					return a;
				}).collect(Collectors.toList());
			//去敏感化
			List<CsqBasicUserVo> donaterList = donaters.stream()
				.map(a -> {
					CsqBasicUserVo csqBasicUserVo = a.copyCsqBasicUserVo();
					return csqBasicUserVo;
				}).collect(Collectors.toList());
			tCsqService.setDonaters(donaterList);
			//捐助列表
			List<Long> userIds = tCsqUserPaymentRecords.stream()
				.map(TCsqUserPaymentRecord::getUserId)
				.collect(Collectors.toList());
			List<TCsqUser> tCsqUsers = userIds.isEmpty()? new ArrayList<>(): userDao.selectInIds(userIds);
			Map<Long, List<TCsqUser>> collect = tCsqUsers.stream().collect(Collectors.groupingBy(TCsqUser::getId));
			List<TCsqUserPaymentRecord> csqUserPaymentRecords = tCsqUserPaymentRecords.stream()
				.map(a -> {
						Timestamp createTime = a.getCreateTime();
						long interval = System.currentTimeMillis() - createTime.getTime();
						int minutes = DateUtil.millsToMinutes(interval);
						List<TCsqUser> tCsqUsers1 = collect.get(a.getUserId());
						if (tCsqUsers1 != null) {
							TCsqUser tCsqUser = tCsqUsers1.get(0);
							tCsqUser.setMinutesAgo(minutes);
						}
						return a;
					}
				).collect(Collectors.toList());
			tCsqService.setCsqUserPaymentRecords(csqUserPaymentRecords);
			//查询所有项目汇报
			List<TCsqServiceReport> csqServiceReports = csqUserServiceReportDao.selectByServiceIdDesc(serviceId);
			tCsqService.setReports(csqServiceReports);
			// 若为已捐款，则还需要项目汇报信息（PC端开发时再添加）
			resultMap.put("csqService", tCsqService);

			Object exist = userRedisTemplate.get(CSQ_GLOBAL_DONATE_BROADCAST, serviceId.toString());
			Queue<CsqDonateRecordVo> voList;
			if(exist == null) {
				voList = new LimitQueue<>(1);	//创建带上限的队列
			} else {
				voList = (LimitQueue<CsqDonateRecordVo>) exist;
			}
			//处理得到list
			List<CsqDonateRecordVo> resultList = new ArrayList<>();
			Iterator<CsqDonateRecordVo> iterator = voList.iterator();
			while (iterator.hasNext()) {
				CsqDonateRecordVo csqDonateRecordVo = iterator.next();
				Long createTime = csqDonateRecordVo.getCreateTime();
				long interval = System.currentTimeMillis() - createTime;
				Long minuteAgo = interval /1000 / 60;
				csqDonateRecordVo.setMinutesAgo(minuteAgo.intValue());
				iterator.remove();
				resultList.add(csqDonateRecordVo);
			}
			//排序
			resultList = resultList.stream()
				.sorted(Comparator.comparing(CsqDonateRecordVo::getCreateTime).reversed()).collect(Collectors.toList());
			//装载结果
			resultMap.put("broadCast", resultList);
		}
		resultMap.put("isMine", isMine);
		resultMap.put("isFund", isFund);
		return resultMap;
	}

	@Override
	public void cert(Long userId, Long serviceId) {
		//check审核者身份
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		csqService.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		csqServiceDao.update(csqService);
	}

	@Override
	public QueryResult<TCsqUserPaymentRecord> billOut(Long userId, Long serviceId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum == null? 1:pageNum;
		pageSize = pageSize == null? 0:pageSize;
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqUserPaymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqPaymenEnum.INOUT_OUT.toCode());
		/*Map<Long, List<TCsqService>> serviceMap = getServiceMap(tCsqUserPaymentRecords);
		List<TCsqUserPaymentRecord> userPaymentRecords = tCsqUserPaymentRecords.stream()
			.map(a -> {
				List<TCsqService> tCsqServices = serviceMap.get(a.getEntityId());
				TCsqService tCsqService = tCsqServices.get(0);
				a.setDate(com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				a.setServiceName(tCsqService.getName());
				return a;
			}).collect(Collectors.toList());*/
		QueryResult<TCsqUserPaymentRecord> queryResult = new QueryResult<>();
//		queryResult.setResultList(userPaymentRecords);
		queryResult.setResultList(tCsqUserPaymentRecords);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

	private Map<Long, List<TCsqService>> getServiceMap(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		List<Long> serviceIds = tCsqUserPaymentRecords.stream()
			.filter(a -> a.getEntityType() == CsqEntityTypeEnum.TYPE_SERVICE.toCode())
			.map(TCsqUserPaymentRecord::getEntityId)
			.collect(Collectors.toList());
		List<TCsqService> tCsqServices = serviceIds.isEmpty()? new ArrayList<>(): csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));
		return serviceMap;
	}

	@Override
	public void submitReport(Long userId, Long serviceId, TCsqServiceReport serviceReport) {
		serviceReport.setId(null);
		serviceReport.setServiceId(serviceId);
		csqUserServiceReportDao.insert(serviceReport);
	}

	@Override
	public void donate(String orderNo) {
		//check订单状态
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		if (tCsqOrder == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单号错误!");
		}
		if (tCsqOrder.getStatus() == CsqOrderEnum.STATUS_ALREADY_PAY.getCode()) {
			return;
		}
		Integer toType = tCsqOrder.getToType();
		if(CsqEntityTypeEnum.TYPE_SERVICE.toCode() != toType) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的订单类型!");
		}
		//插入流水
		//防止重复插入
		Double price = tCsqOrder.getPrice();
		Long userId = tCsqOrder.getUserId();
		TCsqUserPaymentRecord build1 = TCsqUserPaymentRecord.builder()
			.userId(userId)
			.orderId(tCsqOrder.getId())
			.money(price)
			.entityId(tCsqOrder.getFromId())    //来源
			.entityType(tCsqOrder.getFromType())
			.inOrOut(CsqPaymenEnum.INOUT_OUT.toCode()).build();

		Long serviceId = tCsqOrder.getToId();
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		TCsqUserPaymentRecord build2 = TCsqUserPaymentRecord.builder()
			.userId(csqService.getUserId())
			.orderId(tCsqOrder.getId())
			.money(price)
			.entityId(serviceId)    //来源
			.entityType(toType)
			.inOrOut(CsqPaymenEnum.INOUT_IN.toCode()).build();
		csqUserPaymentDao.insert(build1, build2);
		//TODO 捐助人的个人捐助次数、基金捐助次数（如果来源为基金）、项目受助次数等增加

		//往"捐助播报"的缓存中添加记录,key: csqServiceId, value: TCsqService
		//构建一个DonateVo
		int maximum = 20;
		TCsqUser csqUser = userDao.selectByPrimaryKey(userId);
		CsqDonateRecordVo vo = CsqDonateRecordVo.builder().donateAmount(price)
			.userHeadPortraitPath(csqUser.getUserHeadPortraitPath())
			.name(csqUser.getName())
			.createTime(System.currentTimeMillis()).build();

		Object exist = userRedisTemplate.get(CSQ_GLOBAL_DONATE_BROADCAST, serviceId.toString());
		LimitQueue<CsqDonateRecordVo> donateQueue = new LimitQueue<>(maximum);
		if(exist == null) {
			donateQueue = new LimitQueue<>(maximum);	//创建带上限的队列
		} else {
			donateQueue = (LimitQueue<CsqDonateRecordVo>) exist;
		}
		donateQueue.offer(vo);
		userRedisTemplate.put(CSQ_GLOBAL_DONATE_BROADCAST, serviceId.toString(), donateQueue);

		tCsqOrder.setStatus(CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		csqOrderDao.update(tCsqOrder);
	}

	public static void main(String[] args) {
		LimitQueue<TCsqService> ObjectList = new LimitQueue(2);
		TCsqService sdad = TCsqService.builder().name("sdad").build();
		TCsqService adada = sdad;
		adada.setName("adada");
		TCsqService serv123 = new TCsqService();
		serv123.setName("serv123");
		ObjectList.offer(adada);
		System.out.println("----------------");
		ObjectList.stream().forEach(a -> System.out.println(a.getName()));
		ObjectList.offer(serv123);
		System.out.println("----------------");
		ObjectList.stream().forEach(a -> System.out.println(a.getName()));
		TCsqService csqService = new TCsqService();
		csqService.setName("new TCsqService");
		ObjectList.offer(csqService);
		System.out.println("----------------");
		ObjectList.stream().forEach(a -> System.out.println(a.getName()));
	}

	@Override
	public void checkPubAuth(Long userId) {
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		checkPubAuth(tCsqUser);
	}

	private void checkPubAuth(TCsqUser user) {
		Integer accountType = user.getAccountType();
		if (!CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode().equals(accountType)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "非机构账户不能发布项目");
		}
		Integer authenticationStatus = user.getAuthenticationStatus();
		if (!CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您还未通过机构实名认证，无法发布");
		}
	}

	private TCsqService transferAttrs(TCsqService target, TCsqFund origin) {
		target.setSumTotalIn(origin.getSumTotalIn());
		target.setTotalInCnt(origin.getTotalInCnt());
		target.setSurplusAmount(origin.getBalance());
		target.setName(origin.getName());
		target.setDescription(origin.getDescription());
		target.setCoverPic(origin.getCoverPic());
		target.setTypePubKeys(origin.getTrendPubKeys());
		return target;
	}

}
