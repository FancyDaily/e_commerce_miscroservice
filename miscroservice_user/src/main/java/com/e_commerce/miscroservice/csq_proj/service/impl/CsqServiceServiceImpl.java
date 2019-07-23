package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.LimitQueue;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.vo.*;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import com.e_commerce.miscroservice.csq_proj.service.CsqServiceService;
import com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum.STATUS_ALREADY_PAY;

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
	public QueryResult<CsqServiceListVo> list(Long userId, Integer option, Integer pageNum, Integer pageSize) {
		//check
		QueryResult<CsqServiceListVo> result = new QueryResult<>();
		pageNum = pageNum == null ? 1 : pageNum;
		pageSize = pageSize == null ? 0 : pageSize;
		Integer OPTION_ALL = 0;
		Integer OPTION_MINE = 2;

		Integer OPTION_DONATED = 1;
		if(userId == null && !OPTION_ALL.equals(option)) {	//未登录时查看非推荐项目(捐献过、我的项目
			result.setResultList(new ArrayList<>());
			//或抛出登录信号
			return result;
		}

		option = option == null ? OPTION_ALL : option;

		Page<Object> startPage;
		List<TCsqService> tCsqServices;
		if (OPTION_MINE.equals(option)) {
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
			tCsqServices = uniqueServiceIds.isEmpty()? new ArrayList<>():csqServiceDao.selectInIds(uniqueServiceIds);
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

		//获取我已捐款金额
		List<Long> serviceIds = tCsqServices.stream()
			.map(a -> a.getId()).collect(Collectors.toList());
		List<TCsqOrder> csqOrders = csqOrderDao.selectByUserIdInToIdAndStatus(userId, serviceIds, STATUS_ALREADY_PAY.getCode());
		Map<Long, List<TCsqOrder>> toIdOrderMap = csqOrders.stream()
                           			.collect(Collectors.groupingBy(TCsqOrder::getToId));

		List<CsqServiceListVo> csqServices = tCsqServices.stream()
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
				Long serviceId = a.getId();
				List<TCsqOrder> tCsqOrders = toIdOrderMap.get(serviceId);
				Double reduce = 0d;
				if(tCsqOrders !=  null) {
					reduce = tCsqOrders.stream()
						.map(freddie -> freddie.getPrice())
						.reduce(0d, Double::sum);
				}
				a.setSumTotalPayMine(reduce);
				double doubleVal = a.getExpectedAmount()==0? 0: a.getSumTotalIn() / a.getExpectedAmount();
				String donePercent = DecimalFormat.getPercentInstance().format(doubleVal).replaceAll("%", "");
				a.setDonePercent(donePercent);
				a.setDonaterCnt(a.getTotalInCnt());
				return a.copyCsqServiceListVo();
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
		if (tCsqService.getUserId().equals(userId)) {
			isMine = true;
		}
		//进入查询过程
		Integer type = tCsqService.getType();

		if (CsqServiceEnum.TYPE_FUND.getCode() == type) {
			//查询基金
			isFund = true;
			Long fundId = tCsqService.getFundId();
			CsqFundVo csqFundVo = csqFundService.fundDetail(fundId);
			Integer raiseStatus = 0;	//未公开
			Integer status = csqFundVo.getStatus();	//五种状态归约成两种
			if(CsqFundEnum.STATUS_PUBLIC.getVal() == status) {
				raiseStatus = 1;	//已公开
			}
			csqFundVo.setRaiseStatus(raiseStatus);
			resultMap.put("serviceVo", csqFundVo);
			//基金没有播报信息
		} else {
			Double sumTotalIn = tCsqService.getSumTotalIn();
			Double surplusAmount = tCsqService.getSurplusAmount();
			tCsqService.setSumTotalOut(sumTotalIn - surplusAmount);    //剩余金额
			//捐入流水
			List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqUserPaymentEnum.INOUT_IN.toCode());    //TODO 分页
			List<Long> orderIds = tCsqUserPaymentRecords.stream()
				.map(TCsqUserPaymentRecord::getOrderId)
				.distinct().collect(Collectors.toList());
			tCsqUserPaymentRecords = orderIds.isEmpty()? new ArrayList<>() : paymentDao.selectInOrderIdsAndInOut(orderIds, CsqUserPaymentEnum.INOUT_OUT.toCode());
			//统计捐款数，获取top3
			Map<Long, List<TCsqUserPaymentRecord>> unsortedMap = tCsqUserPaymentRecords.stream()
				.collect(Collectors.groupingBy(TCsqUserPaymentRecord::getUserId));
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
					donaterIds.add(a.getKey());	//排序后的集合
				}
			);
			donaters = donaterIds.isEmpty()?new ArrayList<>():userDao.selectInIds(donaterIds);
			//纠正排序
			Map<Long, TCsqUser> donaterMap = new HashMap<>();
			donaters.stream()
				.forEach(donater -> donaterMap.put(donater.getId(), donater));
			donaters = donaterIds.stream()		//通过顺序的ids获取到donaters(按捐款数额排序)
				.map(a -> donaterMap.get(a)).collect(Collectors.toList());

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
			//限制为前3条
			donaterList = donaterList.stream()
				.limit(3).collect(Collectors.toList());
			tCsqService.setDonaters(donaterList);
			//捐助列表
			List<TCsqUserPaymentRecord> csqUserPaymentRecords = donateListNonePage(tCsqUserPaymentRecords);

			tCsqService.setCsqUserPaymentRecords(csqUserPaymentRecords);
			//查询所有项目汇报
			List<TCsqServiceReport> csqServiceReports = csqUserServiceReportDao.selectByServiceIdDesc(serviceId);
			tCsqService.setReports(csqServiceReports);
			// 若为已捐款，则还需要项目汇报信息（PC端开发时再添加）
			CsqServiceDetailVo csqServiceDetailVo = tCsqService.copyCsqServiceDetailVo();
			List<CsqUserPaymentRecordVo> collect1 = csqUserPaymentRecords.stream()
				.map(a -> a.copyUserPaymentRecordVo()).collect(Collectors.toList());
			csqServiceDetailVo.setCsqUserPaymentRecordVos(collect1);	//处理userpayemnt => toVo()

			List<CsqServiceReportVo> collect2 = csqServiceReports.stream()
				.map(a -> a.copyCsqServiceReportVo()).collect(Collectors.toList());
			csqServiceDetailVo.setCsqServiceReportVos(collect2);
			Integer raiseStatus = 0;	//筹备中
			Double expectedAmount = csqServiceDetailVo.getExpectedAmount();	//目标金额
			boolean isFull = sumTotalIn >= expectedAmount;
//			boolean isDone = isFull && surplusAmount==0;
			raiseStatus = isFull? 1: raiseStatus;
			csqServiceDetailVo.setRaiseStatus(raiseStatus);
			String typePubKeys = csqServiceDetailVo.getTypePubKeys();
			List<String> publishName = csqPublishService.getPublishName(CsqPublishEnum.MAIN_KEY_TREND.toCode(), typePubKeys);
			csqServiceDetailVo.setTrendPubNames(publishName);	//TODO
			double doubleVal = csqServiceDetailVo.getExpectedAmount()==0? 0: csqServiceDetailVo.getSumTotalIn() / csqServiceDetailVo.getExpectedAmount();
			String donePercent = DecimalFormat.getPercentInstance().format(doubleVal).replaceAll("%", "");
			csqServiceDetailVo.setDonePercent(donePercent);
			resultMap.put("serviceVo", csqServiceDetailVo);

			List<CsqDonateRecordVo> resultList = dealWithRedisDonateRecord(serviceId);
			//装载结果
			resultMap.put("broadCast", resultList);
		}
		resultMap.put("isMine", isMine);
		resultMap.put("isFund", isFund);
		return resultMap;
	}

	@Override
	public List<CsqDonateRecordVo> dealWithRedisDonateRecord(Long serviceId) {
		String hashKey = CsqRedisEnum.ALL.getMsg();
		hashKey = serviceId!=null? serviceId.toString(): hashKey;
		Object exist = userRedisTemplate.get(CSQ_GLOBAL_DONATE_BROADCAST, hashKey);
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
		return resultList;
	}

	public static void main(String[] args) {
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = new ArrayList<>();
//		tCsqUserPaymentRecords.add(new TCsqUserPaymentRecord());
		List<Long> orderIds = tCsqUserPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getOrderId)
			.distinct()
			.collect(Collectors.toList());
		if(orderIds.contains(null)) {
			System.out.println("contains null:" + true);
			return;
		}
		System.out.println("isEmpty:" + orderIds.isEmpty());
		System.out.println(orderIds);
	}

	@Override
	public void cert(Long userId, Long serviceId) {
		//check审核者身份
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		csqService.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		csqServiceDao.update(csqService);
	}

	@Override
	public QueryResult<CsqUserPaymentRecordVo> billOut(Long userId, Long serviceId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum == null? 1:pageNum;
		pageSize = pageSize == null? 0:pageSize;
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqUserPaymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqUserPaymentEnum.INOUT_OUT.toCode());
		/*Map<Long, List<TCsqService>> serviceMap = getServiceMap(tCsqUserPaymentRecords);
		List<TCsqUserPaymentRecord> userPaymentRecords = tCsqUserPaymentRecords.stream()
			.map(a -> {
				List<TCsqService> tCsqServices = serviceMap.get(a.getEntityId());
				TCsqService tCsqService = tCsqServices.get(0);
				a.setDate(com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				a.setServiceName(tCsqService.getName());
				return a;
			}).collect(Collectors.toList());*/
		List<CsqUserPaymentRecordVo> copyList = tCsqUserPaymentRecords.stream()
			.map(a -> a.copyUserPaymentRecordVo()).collect(Collectors.toList());
		QueryResult<CsqUserPaymentRecordVo> queryResult = new QueryResult<>();
//		queryResult.setResultList(userPaymentRecords);
		queryResult.setResultList(copyList);
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
		if (tCsqOrder.getStatus() == STATUS_ALREADY_PAY.getCode()) {
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
			.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode()).build();

		Long serviceId = tCsqOrder.getToId();
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		TCsqUserPaymentRecord build2 = TCsqUserPaymentRecord.builder()
			.userId(csqService.getUserId())
			.orderId(tCsqOrder.getId())
			.money(price)
			.entityId(serviceId)    //来源
			.entityType(toType)
			.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode()).build();
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

		tCsqOrder.setStatus(STATUS_ALREADY_PAY.getCode());
		csqOrderDao.update(tCsqOrder);
	}

	@Override
	public void checkPubAuth(Long userId) {
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		checkPubAuth(tCsqUser);
	}

	@Override
	public void synchronizeService(Long fundId) {
		TCsqFund csqFund = csqFundDao.selectByPrimaryKey(fundId);
		synchronizeService(csqFund);
	}

	@Override
	public void synchronizeService(TCsqFund csqFund) {
		Long fundId;
		if(csqFund == null || (fundId = csqFund.getId()) == null) {
			return;
		}
		//找到对应的service
		boolean isInsert = false;
		TCsqService csqService = csqServiceDao.selectByFundId(fundId);
		if(csqService == null) {
			csqService = new TCsqService();
			isInsert = true;
		}
		csqService.setType(CsqServiceEnum.TYPE_FUND.getCode());
		csqService.setFundId(fundId);
		csqService.setUserId(csqFund.getUserId());
		csqService.setFundStatus(csqFund.getStatus());
		csqService.setTypePubKeys(csqFund.getTrendPubKeys());
		csqService.setName(csqFund.getName());
		csqService.setSurplusAmount(csqFund.getBalance());	//余额
		csqService.setTotalInCnt(csqFund.getTotalInCnt());	//累积被捐助次数
		csqService.setSumTotalIn(csqFund.getSumTotalIn());
		csqService.setDescription(csqFund.getDescription());
		csqService.setCoverPic(csqFund.getCoverPic());
		csqService.setDescription(csqFund.getDescription());
		if(isInsert) {
			csqServiceDao.insert(csqService);
			return;
		}
		csqServiceDao.update(csqService);
	}

	@Override
	public QueryResult<CsqServiceReportVo> reportList(Long serviceId, Integer pageNum, Integer pageSize) {
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqServiceReport> csqServiceReports = csqUserServiceReportDao.selectByServiceIdDesc(serviceId);
		List<CsqServiceReportVo> voList = csqServiceReports.stream()
			.map(a -> {
				CsqServiceReportVo csqServiceReportVo = a.copyCsqServiceReportVo();
				csqServiceReportVo.setDate(com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date(a.getCreateTime().getTime()));
				return csqServiceReportVo;
			}).collect(Collectors.toList());
		QueryResult<CsqServiceReportVo> queryResult = new QueryResult<>();
		queryResult.setResultList(voList);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

	@Override
	public QueryResult donateList(Long serviceId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum==null?1:pageNum;
		pageSize = pageSize==null?0:pageSize;
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqUserPaymentEnum.INOUT_IN.toCode());    //TODO 分页
		List<Long> orderIds = tCsqUserPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getOrderId)
			.distinct().collect(Collectors.toList());
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		tCsqUserPaymentRecords = orderIds.isEmpty()? new ArrayList<>() : paymentDao.selectInOrderIdsAndInOut(orderIds, CsqUserPaymentEnum.INOUT_OUT.toCode());
		List<TCsqUserPaymentRecord> csqUserPaymentRecords = donateListNonePage(tCsqUserPaymentRecords);
		List<CsqDonateRecordVo> resultList = csqUserPaymentRecords.stream()
			.map(a -> {
				TCsqUser user = a.getUser();
				CsqDonateRecordVo build = CsqDonateRecordVo.builder()
//					.minutesAgo(user.getMinutesAgo())
					.minutesAgo(a.getMinutesAgo())
					.name(user.getName())
					.userHeadPortraitPath(user.getUserHeadPortraitPath())
					.donateAmount(a.getMoney())
					.createTime(a.getCreateTime().getTime()).build();
				return build;
			})
			.sorted(Comparator.comparing(CsqDonateRecordVo::getMinutesAgo))
			.collect(Collectors.toList());

		QueryResult result = new QueryResult();
		result.setResultList(resultList);
		result.setTotalCount(startPage.getTotal());
		return result;
	}

	@Override
	public void modify(TCsqService csqService) {
		csqServiceDao.update(csqService);
	}

	private List<TCsqUserPaymentRecord> donateListNonePage(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		List<Long> userIds = tCsqUserPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getUserId)
			.distinct()
			.collect(Collectors.toList());
		List<TCsqUser> tCsqUsers = userIds.isEmpty() ? new ArrayList<>() : userDao.selectInIds(userIds);
		Map<Long, List<TCsqUser>> collect = tCsqUsers.stream().collect(Collectors.groupingBy(TCsqUser::getId));
		return tCsqUserPaymentRecords.stream()
			.map(a -> {
					Timestamp createTime = a.getCreateTime();
					long interval = System.currentTimeMillis() - createTime.getTime();
					int minutes = DateUtil.millsToMinutes(interval);
					List<TCsqUser> tCsqUsers1 = collect.get(a.getUserId());
					if (tCsqUsers1 != null) {
						TCsqUser tCsqUser = tCsqUsers1.get(0);
//						tCsqUser.setMinutesAgo(minutes);
						a.setUser(tCsqUser);
					}
					a.setMinutesAgo(minutes);
					return a;
				}
			).collect(Collectors.toList());
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
