package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.LimitQueue;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.NumberUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.vo.*;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import com.e_commerce.miscroservice.csq_proj.service.CsqServiceService;
import com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum.STATUS_ALREADY_PAY;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 14:02
 */
@Transactional(rollbackFor = Throwable.class)
@Component
@Log
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
	private CsqPaymentService csqPaymentService;

	@Autowired
	@Qualifier("csqRedisTemplate")
	HashOperations<String, String, Object> userRedisTemplate;

	public static String CSQ_GLOBAL_DONATE_BROADCAST = "csq:global:doante:broadcast";

	@Override
	public Long publish(Long userId, TCsqService service) {
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
			log.warn("存在同名项目, name={}", name);
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "存在同名项目!");
		}

		//插入一条记录
		service.setId(null);
//		service.setStatus(CsqServiceEnum.STATUS_UNDER_CERT.getCode());
		service.setUserId(userId);
		service.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		service.setType(CsqServiceEnum.TYPE_SERIVE.getCode());
		csqServiceDao.insert(service);
		return service.getId();
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
		if (userId == null && !OPTION_ALL.equals(option)) {    //未登录时查看非推荐项目(捐献过、我的项目
			result.setResultList(new ArrayList<>());
			//或抛出登录信号
			return result;
		}

		option = option == null ? OPTION_ALL : option;

		Page<Object> startPage;
		List<TCsqService> tCsqServices;
		Long total = 0L;
		if (OPTION_MINE.equals(option)) {
//			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = csqServiceDao.selectMinePage(pageNum, pageSize, userId);
			total = IdUtil.getTotal();
		} else if (OPTION_ALL.equals(option)) {
//			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = csqServiceDao.selectAllPage(pageNum, pageSize);
			total = IdUtil.getTotal();
		} else if (OPTION_DONATED.equals(option)) {
			//找到我捐助过的项目记录
//			List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndToTypeDesc(userId, CsqEntityTypeEnum.TYPE_SERVICE.toCode());
			List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdInToTypeAndStatusDesc(userId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqEntityTypeEnum.TYPE_FUND.toCode());
			//构建时间map用于排序
			Map<Long, List<TCsqOrder>> toIdOrderMap = tCsqOrders.stream()
				.collect(Collectors.groupingBy(TCsqOrder::getToId));
			//处理（去重等
			List<Long> serviceIds = tCsqOrders.stream()
				.filter(a -> CsqEntityTypeEnum.TYPE_SERVICE.toCode() == a.getToType())
				.map(TCsqOrder::getToId)
				.distinct()
				.collect(Collectors.toList());
			List<Long> fundIds = tCsqOrders.stream()
				.filter(a -> CsqEntityTypeEnum.TYPE_FUND.toCode() == a.getToType())
				.map(TCsqOrder::getToId)
				.distinct()
				.collect(Collectors.toList());
//			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = (serviceIds.isEmpty() && fundIds.isEmpty()) ? new ArrayList<>() : csqServiceDao.selectInIdsOrInFundIdsPage(pageNum, pageSize, serviceIds, fundIds);
			total = IdUtil.getTotal();
			tCsqServices = tCsqServices.stream()
				.sorted(Collections.reverseOrder(Comparator.comparing(a -> {
					Long toId = CsqServiceEnum.TYPE_FUND.getCode() == a.getType() ? a.getFundId() : a.getId();
					List<TCsqOrder> csqOrders = toIdOrderMap.get(toId);
					Long time = 0L;
					if (csqOrders != null) {
						TCsqOrder tCsqOrder = csqOrders.get(0);
						time = tCsqOrder.getCreateTime().getTime();
					}
					return time;
				}))).collect(Collectors.toList());
		} else {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数option不正确!");
		}

		if (tCsqServices.isEmpty()) {
			return result;
		}

		//处理数据 -> 如果为基金，获取即时的信息
		List<Long> fundIds = tCsqServices.stream()
			.filter(a -> CsqServiceEnum.TYPE_FUND.getCode() == a.getType())    //基金类型
			.map(TCsqService::getFundId)
			.collect(Collectors.toList());
		List<TCsqFund> csqFunds = fundIds.isEmpty() ? new ArrayList<>() : csqFundDao.selectInIds(fundIds);    //得到目标基金信息
		Map<Long, List<TCsqFund>> fundMap = csqFunds.stream()
			.collect(Collectors.groupingBy(TCsqFund::getId));

		//获取我已捐款金额
		List<Long> serviceIds = tCsqServices.stream()
			.map(a -> {
				if(CsqServiceEnum.TYPE_FUND.getCode() == a.getType()) {
					return a.getFundId();
				}
				return a.getId();
			}).collect(Collectors.toList());
		List<TCsqOrder> csqOrders = userId == null? new ArrayList<>():csqOrderDao.selectByUserIdInToIdAndStatus(userId, serviceIds, STATUS_ALREADY_PAY.getCode());
		Map<Long, List<TCsqOrder>> toIdOrderMap = csqOrders.stream()
			.collect(Collectors.groupingBy(TCsqOrder::getToId));

		List<CsqServiceListVo> csqServices = tCsqServices.stream()
			.map(a -> {
				Integer type = a.getType();
				boolean isFund = CsqServiceEnum.TYPE_FUND.getCode() == type;
				if (isFund) {
					Long fundId = a.getFundId();
					List<TCsqFund> tCsqFunds = fundMap.get(fundId);
					if (tCsqFunds != null) {
						TCsqFund csqFund = tCsqFunds.get(0);
						//覆写相关信息
						a = transferAttrs(a, csqFund);
					}
				}
				Long serviceId = a.getId();
				List<TCsqOrder> tCsqOrders = toIdOrderMap.get(isFund?a.getFundId():serviceId);
				Double reduce = 0d;
				if (tCsqOrders != null) {
					reduce = tCsqOrders.stream()
						.map(freddie -> freddie.getPrice())
						.reduce(0d, Double::sum);
				}

				a.setSumTotalPayMine(NumberUtil.keep2Places(reduce));//已筹集
				double doubleVal = a.getExpectedAmount() == 0 ? 1 : a.getSumTotalIn() / a.getExpectedAmount();
				doubleVal = doubleVal > 1d ? 1d:doubleVal;
				String donePercent = DecimalFormat.getPercentInstance().format(doubleVal).replaceAll("%", "");
				a.setDonePercent(donePercent);
				a.setDonaterCnt(a.getTotalInCnt());
				String coverPic = a.getCoverPic();
				//处理封面图仅返回第一张
				coverPic = coverPic == null? "" : coverPic;
				String[] split = coverPic.split(",");
				coverPic = split[0];
				a.setCoverPic(coverPic);
				return a.copyCsqServiceListVo();
			})
			.collect(Collectors.toList());
		result.setTotalCount(total);
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
		isMine = tCsqService.getUserId().equals(userId);
		//进入查询过程
		Integer type = tCsqService.getType();

		if (CsqServiceEnum.TYPE_FUND.getCode() == type) {
			//查询基金
			isFund = true;
			Long fundId = tCsqService.getFundId();
			CsqFundVo csqFundVo = csqFundService.fundDetail(userId, fundId);
			Integer raiseStatus = 0;    //未公开
			Integer status = csqFundVo.getStatus();    //五种状态归约成两种
			if (CsqFundEnum.STATUS_PUBLIC.getVal() == status) {
				raiseStatus = 1;    //已公开
			}
			csqFundVo.setRaiseStatus(raiseStatus);
			resultMap.put("serviceVo", csqFundVo);
			//基金没有播报信息
		} else {
			Double sumTotalIn = tCsqService.getSumTotalIn();
			Double surplusAmount = tCsqService.getSurplusAmount();
			tCsqService.setSumTotalOut(NumberUtil.keep2Places(sumTotalIn - surplusAmount));    //总支出
			//捐入流水
			List<Long> orderIds = csqPaymentService.getPaymentRelatedOrderIds(serviceId);
			List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = orderIds.isEmpty() ? new ArrayList<>() : paymentDao.selectInOrderIdsAndInOut(orderIds, CsqUserPaymentEnum.INOUT_OUT.toCode());
			List<CsqBasicUserVo> donaterList = csqPaymentService.getTopDonaters(tCsqUserPaymentRecords, orderIds);

			tCsqService.setDonaters(donaterList);
			//捐助列表
			List<TCsqUserPaymentRecord> csqUserPaymentRecords = donateListNonePage(tCsqUserPaymentRecords);

			tCsqService.setCsqUserPaymentRecords(csqUserPaymentRecords);
			//查询所有项目汇报
			List<TCsqServiceReport> csqServiceReports = csqUserServiceReportDao.selectByServiceIdDesc(serviceId);
			tCsqService.setReports(csqServiceReports);
			// 若为已捐款，则还需要项目汇报信息（PC端开发时再添加）
			CsqServiceDetailVo csqServiceDetailVo = tCsqService.copyCsqServiceDetailVo();
			String description = csqServiceDetailVo.getDescription();
			description = description.replaceAll("<br>", "\n");
			csqServiceDetailVo.setDescription(description);

			List<CsqUserPaymentRecordVo> collect1 = csqUserPaymentRecords.stream()
				.map(a -> a.copyUserPaymentRecordVo()).collect(Collectors.toList());
			csqServiceDetailVo.setCsqUserPaymentRecordVos(collect1);    //处理userpayemnt => toVo()

			List<CsqServiceReportVo> collect2 = csqServiceReports.stream()
				.map(a -> a.copyCsqServiceReportVo()).collect(Collectors.toList());
			csqServiceDetailVo.setCsqServiceReportVos(collect2);
			Integer raiseStatus = 0;    //筹备中
			Double expectedAmount = csqServiceDetailVo.getExpectedAmount();    //目标金额
			boolean isFull = sumTotalIn >= expectedAmount;
//			boolean isDone = isFull && surplusAmount==0;
			raiseStatus = isFull ? 1 : raiseStatus;
			csqServiceDetailVo.setRaiseStatus(raiseStatus);
			String typePubKeys = csqServiceDetailVo.getTypePubKeys();
			List<String> publishName = csqPublishService.getPublishName(CsqPublishEnum.MAIN_KEY_TREND.toCode(), typePubKeys);
			csqServiceDetailVo.setTrendPubNames(publishName);
			double doubleVal = csqServiceDetailVo.getExpectedAmount() == 0 ? 1 : csqServiceDetailVo.getSumTotalIn() / csqServiceDetailVo.getExpectedAmount();
			doubleVal = doubleVal > 1d ? 1d : doubleVal;
			String donePercent = DecimalFormat.getPercentInstance().format(doubleVal).replaceAll("%", "");
			Double aDouble = Double.valueOf(donePercent);
			csqServiceDetailVo.setDonePercent(donePercent);
			resultMap.put("serviceVo", csqServiceDetailVo);

			List<CsqDonateRecordVo> resultList = dealWithRedisDonateRecord(serviceId);
			//装载结果
			resultMap.put("broadCast", resultList);
		}
		//是否捐助过该项目
		boolean isDonated = false;
		if(userId != null) {
			List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndToTypeAndToIdDesc(userId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), serviceId);
			if (!tCsqOrders.isEmpty()) {
				isDonated = true;
			}
		}
		resultMap.put("donated", isDonated);
		resultMap.put("isMine", isMine);
		resultMap.put("isFund", isFund);
		return resultMap;
	}

	private HashMap<Long, Boolean> getIsAnonymousOrderMap(List<Long> orderIds) {
		if (orderIds.isEmpty()) {
			return new HashMap<>();
		}
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectInIds(orderIds);
		Map<Long, List<TCsqOrder>> collect = tCsqOrders.stream()
			.collect(Collectors.groupingBy(TCsqOrder::getId));
		HashMap<Long, Boolean> isAnonymousOrderMap = new HashMap<>();
		collect.forEach((key, val) -> {
			TCsqOrder tCsqOrder = val.get(0);
			Integer isAnonymous = tCsqOrder.getIsAnonymous();
			isAnonymousOrderMap.put(key, CsqOrderEnum.IS_ANONYMOUS_TRUE.getCode().equals(isAnonymous));
		});
		return isAnonymousOrderMap;
	}

	@Override
	public List<CsqDonateRecordVo> dealWithRedisDonateRecord(Long serviceId) {
		String hashKey = CsqRedisEnum.ALL.getMsg();
		hashKey = serviceId != null ? serviceId.toString() : hashKey;
		Object exist = userRedisTemplate.get(CSQ_GLOBAL_DONATE_BROADCAST, hashKey);
		Queue<CsqDonateRecordVo> voList;
		if (exist == null) {
			voList = new LimitQueue<>(1);    //创建带上限的队列
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
			Long minuteAgo = interval / 1000 / 60;
			minuteAgo = minuteAgo > 60 ? 60 : minuteAgo;
			csqDonateRecordVo.setMinutesAgo(minuteAgo.intValue());
			iterator.remove();
			resultList.add(csqDonateRecordVo);
		}
		//排序
		resultList = resultList.stream()
			.sorted(Comparator.comparing(CsqDonateRecordVo::getCreateTime).reversed()).collect(Collectors.toList());
		return resultList;
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
		pageNum = pageNum == null ? 1 : pageNum;
		pageSize = pageSize == null ? 9999999 : pageSize;
//		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqUserPaymentDao.selectByEntityIdAndEntityTypeAndInOutDescPage(serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqUserPaymentEnum.INOUT_OUT.toCode(), pageNum, pageSize);
		long total = IdUtil.getTotal();
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
			.map(a -> {
				CsqUserPaymentRecordVo csqUserPaymentRecordVo = a.copyUserPaymentRecordVo();
				csqUserPaymentRecordVo.setDate(com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				return csqUserPaymentRecordVo;
			}).collect(Collectors.toList());
		QueryResult<CsqUserPaymentRecordVo> queryResult = new QueryResult<>();
//		queryResult.setResultList(userPaymentRecords);
		queryResult.setResultList(copyList);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	private Map<Long, List<TCsqService>> getServiceMap(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		List<Long> serviceIds = tCsqUserPaymentRecords.stream()
			.filter(a -> a.getEntityType() == CsqEntityTypeEnum.TYPE_SERVICE.toCode())
			.map(TCsqUserPaymentRecord::getEntityId)
			.collect(Collectors.toList());
		List<TCsqService> tCsqServices = serviceIds.isEmpty() ? new ArrayList<>() : csqServiceDao.selectInIds(serviceIds);
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
		if (CsqEntityTypeEnum.TYPE_SERVICE.toCode() != toType) {
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
		if (exist == null) {
			donateQueue = new LimitQueue<>(maximum);    //创建带上限的队列
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
		if (csqFund == null || (fundId = csqFund.getId()) == null) {
			return;
		}
		//找到对应的service
		boolean isInsert = false;
		TCsqService csqService = csqServiceDao.selectByFundId(fundId);
		if (csqService == null) {
			csqService = new TCsqService();
			isInsert = true;
		}
		csqService.setType(CsqServiceEnum.TYPE_FUND.getCode());
		csqService.setFundId(fundId);
		csqService.setUserId(csqFund.getUserId());
		csqService.setFundStatus(csqFund.getStatus());
		csqService.setTypePubKeys(csqFund.getTrendPubKeys());
		csqService.setName(csqFund.getName());
		csqService.setSurplusAmount(csqFund.getBalance());    //余额
		csqService.setTotalInCnt(csqFund.getTotalInCnt());    //累积被捐助次数
		csqService.setSumTotalIn(csqFund.getSumTotalIn());
		csqService.setDescription(csqFund.getDescription());
		csqService.setCoverPic(csqFund.getCoverPic());
		csqService.setDescription(csqFund.getDescription());
		csqService.setPersonInCharge(csqFund.getPersonInCharge());
		csqService.setPersonInChargePic(csqFund.getPersonInChargePic());
		csqService.setOccupation(csqFund.getOccupation());
		csqService.setSharePic(csqFund.getSharePic());
		if (isInsert) {
			csqServiceDao.insert(csqService);
			return;
		}
		csqServiceDao.update(csqService);
	}

	@Override
	public QueryResult<CsqServiceReportVo> reportList(Long serviceId, Integer pageNum, Integer pageSize) {
//		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqServiceReport> csqServiceReports = csqUserServiceReportDao.selectByServiceIdDescPage(pageNum, pageSize, serviceId);
		long total = IdUtil.getTotal();
		List<CsqServiceReportVo> voList = csqServiceReports.stream()
			.map(a -> {
				CsqServiceReportVo csqServiceReportVo = a.copyCsqServiceReportVo();
				csqServiceReportVo.setDate(com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date(a.getCreateTime().getTime()));
				return csqServiceReportVo;
			}).collect(Collectors.toList());
		QueryResult<CsqServiceReportVo> queryResult = new QueryResult<>();
		queryResult.setResultList(voList);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	@Override
	public QueryResult donateList(Long serviceId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum == null ? 1 : pageNum;
		pageSize = pageSize == null ? 0 : pageSize;
		//类型推断
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		boolean isFund = CsqServiceEnum.TYPE_FUND.getCode() == csqService.getType();
		Long entityId = isFund? csqService.getFundId(): serviceId;
		List<Long> orderIds = csqPaymentService.getPaymentRelatedOrderIds(entityId, isFund? CsqEntityTypeEnum.TYPE_FUND.toCode(): CsqEntityTypeEnum.TYPE_SERVICE.toCode());
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords;
//		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		tCsqUserPaymentRecords = orderIds.isEmpty() ? new ArrayList<>() : paymentDao.selectInOrderIdsAndInOutDescPage(pageNum, pageSize, orderIds, CsqUserPaymentEnum.INOUT_OUT.toCode());
		long total = IdUtil.getTotal();
		//构建匿名捐入map
		HashMap<Long, Boolean> isAnonymousOrderMap = getIsAnonymousOrderMap(orderIds);

		List<TCsqUserPaymentRecord> csqUserPaymentRecords = donateListNonePage(tCsqUserPaymentRecords);
		List<CsqDonateRecordVo> resultList = csqUserPaymentRecords.stream()
			.map(a -> {
				//获取是否匿名信息
				TCsqUser user = a.getUser();
				Long orderId = a.getOrderId();
				Boolean isAnonymous = isAnonymousOrderMap.get(orderId);
				isAnonymous = isAnonymous != null ? isAnonymous : false;
				String name = isAnonymous ? CsqUserEnum.DEFAULT_ANONYMOUS_NAME : user.getName();
				String userHeadPath = isAnonymous ? CsqUserEnum.DEFAULT_ANONYMOUS_HEADPORTRAITUREPATH : user.getUserHeadPortraitPath();

				Integer minutesAgo = a.getMinutesAgo();
				//处理 minuteAgo
				minutesAgo = minutesAgo > 60 ? 60 : minutesAgo;

				return CsqDonateRecordVo.builder()
					.minutesAgo(minutesAgo)
					.name(name)    //姓名
					.userHeadPortraitPath(userHeadPath)    //头像
					.donateAmount(a.getMoney())
					.createTime(a.getCreateTime().getTime()).build();
			})
			.sorted(Comparator.comparing(CsqDonateRecordVo::getCreateTime).reversed())
			.collect(Collectors.toList());

		QueryResult result = new QueryResult();
		result.setResultList(resultList);
		result.setTotalCount(total);
		return result;
	}

	@Override
	public void modify(TCsqService csqService) {
		Double expectedAmount = csqService.getExpectedAmount();
		//针对修改期望金额需要更多逻辑
		if(expectedAmount != null) {
			//重新计算剩余期望金额
			TCsqService csqService1 = csqServiceDao.selectByPrimaryKey(csqService.getId());
			Double sumTotalIn = csqService1.getSumTotalIn();
			double amount = expectedAmount - sumTotalIn;
			Double expectRemainAmount = amount > 0d? amount: 0d;
			csqService.setExpectedRemainAmount(expectRemainAmount);
		}
		csqServiceDao.update(csqService);
	}

	@Override
	public TCsqService getService(Long fundId) {
		return csqServiceDao.selectByFundId(fundId);
	}

	@Override
	public CsqServiceReportVo reportDetail(Long serviceReportId) {
		TCsqServiceReport tCsqServiceReport = csqUserServiceReportDao.selectByPrimaryKey(serviceReportId);
		if(tCsqServiceReport == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "项目汇报编号不正确！");
		}
		CsqServiceReportVo csqServiceReportVo = tCsqServiceReport.copyCsqServiceReportVo();
		csqServiceReportVo.setDate(com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date(tCsqServiceReport.getCreateTime().getTime()));
		return csqServiceReportVo;
	}

	@Override
	public TCsqService selectByExtend(String extend) {
		return MybatisPlus.getInstance().findOne(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getExtend, extend));
	}

	@Override
	public List<TCsqService> findAllByTypeAndIdGreaterThan(int type, long id) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqService::getType, type)
			.gt(TCsqService::getId, id));
	}

	@Override
	public List<TCsqService> selectInIds(Long... ids) {

		return MybatisPlus.getInstance().findAll(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqService::getId, ids)
		);
	}

	@Override
	public List<TCsqService> selectInExtends(List<Long> collect) {
		return csqServiceDao.selectInExtends(collect);
	}

	@Override
	public QueryResult<TCsqService> list(String searchParam, Integer pageNum, Integer pageSize, boolean isFuzzySearch) {
		MybatisPlusBuild baseBuild = csqServiceDao.getBaseBuild();

		boolean emptySearchParam = StringUtil.isEmpty(searchParam);
		//根据业务需求，增加筛选条件
		baseBuild = baseBuild
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_SERIVE.getCode());	//仅要类型为 -> 项目的数据

		//参数判定 & 条件预装
		String pattern = "^[1-9]\\d*$";
		boolean matches = Pattern.matches(pattern, searchParam);
		if(matches) {	//参数为id
			baseBuild = baseBuild
				.eq(TCsqService::getId, searchParam);
		} else {
			if(isFuzzySearch) {	//模糊查询
				baseBuild = emptySearchParam?baseBuild:baseBuild
					.like(TCsqService::getName, getLikeParam(searchParam));
			} else {
				baseBuild = emptySearchParam?baseBuild:baseBuild
					.eq(TCsqService::getName, searchParam);
			}
		}
		//排序
		baseBuild = baseBuild
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime));

		//列表
		List<TCsqService> tCsqServices = csqServiceDao.selectWithBuildPage(baseBuild, pageNum, pageSize);
		long total = IdUtil.getTotal();

		QueryResult<TCsqService> queryResult = new QueryResult<>();
		queryResult.setResultList(tCsqServices);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	@Override
	public Map<Integer, Object> countGroupByStatus(Long userId) {
		HashMap<Integer, Object> map = new HashMap<>();
		List<TCsqService> tCsqServices = csqServiceDao.selectByType(CsqServiceEnum.TYPE_SERIVE.getCode());
		Map<Integer, List<TCsqService>> statusServiceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getStatus));
		statusServiceMap.forEach((k,v) -> {
			int size = v==null?0:v.size();
			map.put(k, size);	//数量
		});

		return map;
	}

	@Override
	public void synchronizeService(List<TCsqFund> toUpdateFunds) {
		toUpdateFunds.stream().forEach(a -> {
			synchronizeService(a);
		});
	}

	private String getLikeParam(String searchParam) {
		return "%" + searchParam + "%";
	}

	private List<TCsqUserPaymentRecord> donateListNonePage(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		return donateListNonePage(tCsqUserPaymentRecords, null);
	}

	private List<TCsqUserPaymentRecord> donateListNonePage(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords, Map isAnonymoustMap) {
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
