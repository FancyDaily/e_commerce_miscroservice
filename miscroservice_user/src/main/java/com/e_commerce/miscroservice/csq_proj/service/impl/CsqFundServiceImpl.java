package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.service.*;
import com.e_commerce.miscroservice.csq_proj.vo.*;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:58
 */
@Transactional(rollbackFor = Throwable.class)
@Service
public class CsqFundServiceImpl implements CsqFundService {

	@Autowired
	private CsqMsgService csqMsgService;

	@Autowired
	private CsqFundDao fundDao;

	@Autowired
	private CsqUserDao userDao;

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Autowired
	private CsqOrderService csqOrderService;

	@Autowired
	private CsqUserPaymentDao paymentDao;

	@Autowired
	private CsqPublishDao csqPublishDao;

	@Autowired
	private CsqPublishService csqpublishService;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqServiceService csqServiceService;

	@Autowired
	private CsqPaymentService csqPaymentService;

	@Autowired
	private CsqFundService csqFundService;

	@Override
	public void applyForAFund(Long userId, String orderNo) {
		//这其实是一系列支付成功后的操作,应当在notifyUrl指向的接口中被调用
		//基金生命周期：在用户请求微信支付(举例)时，产生待激活状态的实体，在用户支付成功后被激活.
		//check实名状态
		// 6.11 把对实名的check放到编辑名字时
		//6.11	基金申请即通过(付款后激活变为可用)，状态为未公开，达到标准线（eg.10000）时可以申请公开，此间状态为待审核
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);	//check订单支付状态
//		csqOrderService.checkPaid(tCsqOrder);	//TODO 为了测试拿掉了check
		//开始基金创建流程:
		Long fundId = tCsqOrder.getToId();
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		if(CsqFundEnum.STATUS_ACTIVATED.getVal() == csqFund.getStatus()) {
			return;
		}
		csqFund.setStatus(CsqFundEnum.STATUS_ACTIVATED.getVal());	//激活
		fundDao.update(csqFund);

		TCsqService csqService = csqFund.copyCsqService();
		csqService.setId(null);
		csqService.setFundId(fundId);
		csqService.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		csqService.setType(CsqServiceEnum.TYPE_FUND.getCode());
		csqService.setFundStatus(csqFund.getStatus());
		csqServiceDao.insert(csqService);
	}

	private void checkSingletonForPerson(TCsqUser user) {
		Long userId = user.getId();
		Integer authType = user.getAuthenticationType();
		if(UserEnum.AUTHENTICATION_TYPE_PERSON.toCode().equals(authType)) {
			List<TCsqFund> tCsqFunds = fundDao.selectByUserId(userId);
			if(!tCsqFunds.isEmpty()) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经有一项基金或基金审核中,请勿重复申请!");
			}
		}
	}

	@Override
	public void modifyFund(Long userId, TCsqFund fund) {
		if(fund == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "所有必要参数为空");
		}
		String description = fund.getDescription();
		description = description == null? "": description;
		if(description.length() > 8192) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "描述长度过长！");
		/*// check实名
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		if(!CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(tCsqUser.getAuthenticationStatus())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请先进行实名认证!");
		}*/
		Long fundId = fund.getId();
		TCsqFund csqFund = getFundIfNotNullFundId(fundId);
		Long finalUserId = csqFund.getUserId();
		Integer currentStatus = csqFund.getStatus();
		//包含[申请公开]基金业务
		Integer status = fund.getStatus();
		if(status != null) {
			if(CsqFundEnum.STATUS_ACTIVATED.getVal() == currentStatus && CsqFundEnum.STATUS_UNDER_CERT.getVal() == status) {	//申请公开基金
				Double totalIn = csqFund.getSumTotalIn();
				if(totalIn < CsqFundEnum.PUBLIC_MINIMUM) {	//未达到标准
					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您的基金未达到标准，请再接再厉!");
				}
			}
			if(!status.equals(currentStatus) && CsqFundEnum.STATUS_DONE.getVal() == currentStatus) {	//基金捐助已完成
				//停止捐款， 首页继续展示 => pass
			}
			if(!status.equals(currentStatus) && CsqFundEnum.STATUS_OFF_SHELF.getVal() == currentStatus) {	//基金下架
				//停止捐款， 首页不展示， 停止使用基金付费 => is_shown => 0
				csqFund.setIsShown(CsqFundEnum.IS_SHOWN_NO.getVal());
			}
		}
		//若 status = 2 直接处理成【公开】
//		fund.setBalance(null);	//若仅用于基本信息修改则不允许修改金额
		fund.setUserId(null);	//可能与基金持有者不同的修改人
		fundDao.update(fund);
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
				//计算是否满足开放条件
				if(checkIsOkForPublic(csqFund)) {
					TCsqFund build = TCsqFund.builder()
						.id(csqFund.getId())
						.status(CsqFundEnum.STATUS_PUBLIC.getVal())
						.build();
					fundDao.update(build);
					csqFund.setStatus(CsqFundEnum.STATUS_PUBLIC.getVal());
					csqFund.setName(fund.getName() == null? csqFund.getName(): fund.getName());
					csqMsgService.sendServiceMsgForFund(csqFund, finalUserId);
				}
				csqServiceService.synchronizeService(csqFund);
			}
		});
	}

	@Override
	public boolean checkIsOkForPublic(TCsqFund fund) {
		Double currentBalance = fund.getBalance();
		boolean undone = CsqFundEnum.STATUS_ACTIVATED.getVal() == fund.getStatus();	//当前为 "已激活"(未公开的状态)
		boolean achieveMinimum = CsqFundEnum.PUBLIC_MINIMUM <= currentBalance;	//达到下限值
		boolean isNotDoneOrOffShelf = CsqFundEnum.STATUS_DONE.getVal() != fund.getStatus() && CsqFundEnum.STATUS_OFF_SHELF.getVal() != fund.getStatus();
		return undone & achieveMinimum && checkFundCompletion(fund) && isNotDoneOrOffShelf;	//满足开放条件
	}

	@Override
	public boolean checkFundCompletion(TCsqFund fund) {
		if(fund == null) {
			return false;
		}
		String trendPubKeys = fund.getTrendPubKeys();
		String name = fund.getName();
		String description = fund.getDescription();
		String coverPic = fund.getCoverPic();
		String detailPic = fund.getDetailPic();
		String creditCardId = fund.getCreditCardId();
		String creditCardName = fund.getCreditCardName();
		String purpose = fund.getPurpose();
		return !StringUtil.isAnyEmpty(trendPubKeys, name, description, coverPic, detailPic, purpose
//			,creditCardId
//			,creditCardName
		);
	}

	@Override
	public boolean checkBeforeApplyForAFund(Long userId) {
		boolean checkResult = false;
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		Integer authenticationStatus = tCsqUser.getAuthenticationStatus();
		Integer authType = tCsqUser.getAuthenticationType();
		boolean singleFunder = CsqUserEnum.AUTHENTICATION_STATUS_NO.toCode().equals(authenticationStatus) ||
			CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus) && CsqUserEnum.AUTHENTICATION_TYPE_PERSON.toCode().equals(authType);
		singleFunder = true;	//按照前期的要求，所有的用户都是singleFunder
		if(singleFunder) {
			checkSingletonForPerson(tCsqUser);
		}
		checkResult = true;
		return checkResult;
	}

	@Transactional(rollbackFor = Throwable.class)
	@Override
	public void certFund(Long userId, Long fundId, Integer option) {
		if(Objects.isNull(fundId) || Objects.isNull(option)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数fundId、option都不能为空");
		}
		final int CERT_SUCESS = 1;
		final int CERT_FAIL = 2;
		if(false) {
			userDao.selectByPrimaryKey(userId);
			//check用户审核权限
		}
		//审核过程
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		if(CsqFundEnum.STATUS_UNDER_CERT.getVal() != csqFund.getStatus()) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的审核前状态!当前基金无法审核!");
		}

		if(CERT_SUCESS == option) {
			csqFund.setStatus(CsqFundEnum.STATUS_PUBLIC.getVal());
			//更新对应项目状态
			TCsqService build = TCsqService.builder().fundId(fundId)
				.fundStatus(CsqFundEnum.STATUS_PUBLIC.getVal()).build();
			csqServiceDao.updateByFundId(build);
			//TODO sysMsg
		} else if(CERT_FAIL == option) {
			csqFund.setStatus(CsqFundEnum.STATUS_CERT_FAIL.getVal());
			//TODO sysMsg
		} else {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "option参数不正确!");
		}
		fundDao.update(csqFund);
		//找到基金对应的项目，更新状态
//		TCsqService csqService = csqServiceDao.selectByFundId(fundId);
	}

	@Override
	public CsqFundVo fundDetail(Long userId, Long fundId) {
		if(fundId == null) {
			throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "参数基金编号为空!");
		}
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		if(csqFund == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "基金不存在!");
		}
		CsqFundVo csqFundVo = csqFund.copyCsqFundVo();
		//统计捐款人数
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOut(fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqUserPaymentEnum.INOUT_IN.toCode());
		int count = tCsqUserPaymentRecords.size();
		csqFundVo.setContributeInCnt(count);
		//把基金方向的publish_id转换成名字
		List<String> publishName = csqpublishService.getPublishName(CsqPublishEnum.MAIN_KEY_TREND.toCode(), csqFundVo.getTrendPubKeys());
		csqFundVo.setTrendPubNames(publishName);
		//基金资助项目记录 列表 TODO 可能需要分页
		if(tCsqUserPaymentRecords.isEmpty()) {
			return csqFundVo;
		}
		List<TCsqOrder> resultList = getGotoList(null, null, fundId, tCsqUserPaymentRecords).getResultList();
		csqFundVo.setGoToList(resultList);
		boolean isMine = csqFund.getUserId().equals(userId);
		csqFundVo.setMine(isMine);
		csqFundVo.setRaiseStatus(CsqFundEnum.STATUS_PUBLIC.getVal() == csqFundVo.getStatus()? CsqFundEnum.RAISE_STAUTS_DONE.getVal(): CsqFundEnum.RAISE_STATUS_RAISING.getVal());

		//获取项目编号
		TCsqService service = csqServiceService.getService(fundId);
		if(service != null) {
			csqFundVo.setServiceId(service.getId());
		}

		List<Long> orderIds = csqPaymentService.getPaymentRelatedOrderIds(fundId);
		tCsqUserPaymentRecords = orderIds.isEmpty() ? new ArrayList<>() : paymentDao.selectInOrderIdsAndInOut(orderIds, CsqUserPaymentEnum.INOUT_OUT.toCode());
		List<CsqBasicUserVo> donaterList = csqPaymentService.getTopDonaters(tCsqUserPaymentRecords, orderIds);
		csqFundVo.setDonaterList(donaterList);

		return csqFundVo;
	}

	@Override
	public QueryResult getGotoList(Long fundId, Integer pageNum, Integer pageSize) {
		QueryResult result = new QueryResult();
		QueryResult gotoList = getGotoList(pageNum, pageSize, fundId, null);
		List<TCsqOrder> gotoListNonePage = gotoList.getResultList() == null? new ArrayList<>(): gotoList.getResultList();
		result.setTotalCount(gotoList.getTotalCount());
		List<Long> serviceIds = gotoListNonePage.stream()
			.map(TCsqOrder::getToId)
			.distinct().collect(Collectors.toList());
		List<TCsqService> tCsqServices = serviceIds.isEmpty()? new ArrayList<>(): csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));

		List<CsqFundDonateVo> csqFundDonateVos = gotoListNonePage.stream()
			.sorted(Comparator.comparing(TCsqOrder::getCreateTime).reversed())
			.map(a -> {
				CsqFundDonateVo csqFundDonateVo = a.copyCsqFundDonateVo();
				Long time = a.getCreateTime().getTime();
				csqFundDonateVo.setYear(DateUtil.timeStamp2Date(time, "yyyy"));
				csqFundDonateVo.setDate(DateUtil.timeStamp2Date(time, "MM-dd"));
				Long serviceId = a.getToId();
				List<TCsqService> tCsqServices1 = serviceMap.get(serviceId);
				String serviceName = tCsqServices1.get(0).getName();
				csqFundDonateVo.setName(serviceName);
				return csqFundDonateVo;
			}).collect(Collectors.toList());
		List<Map<String, Object>> mapList = getMapList(csqFundDonateVos);
		result.setResultList(mapList);
		return result;
	}

	private List<Map<String, Object>> getMapList(List<CsqFundDonateVo> records) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		Map<String, List<CsqFundDonateVo>> currentMap = new HashMap<>();

		records.stream()
			.forEach(a -> {
				String year = a.getYear();
				List<CsqFundDonateVo> userPaymentRecords = currentMap.get(year);
				if (userPaymentRecords == null) {
					userPaymentRecords = new ArrayList<>();
				}
				userPaymentRecords.add(a);
				currentMap.put(year, userPaymentRecords);
			});

		currentMap.forEach((key, value) -> {
			Map<String, Object> yearMap = new HashMap<>();
			yearMap.put("year", key);
			yearMap.put("records", value);
			mapList.add(yearMap);    //向mapList放入一个含有年份信息的map
		});
		return mapList;
	}

	private QueryResult getGotoList(Integer pageNum, Integer pageSize, Long fundId, List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		if(pageNum == null || pageSize == null) {
			pageNum = 1;
			pageSize = 99999;
		}
		QueryResult queryResult = new QueryResult();
		if(tCsqUserPaymentRecords==null) {
			//根据fundId获取值
			tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOut(fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqUserPaymentEnum.INOUT_OUT.toCode());
		}
		List<Long> tOrderIds = tCsqUserPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getOrderId)
			.collect(Collectors.toList());
		if(tCsqUserPaymentRecords.isEmpty()) {
			return new QueryResult();
		}
		Long total = 0L;
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByFromIdAndFromTypeInOrderIdsAndStatusPage(pageNum, pageSize, fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), tOrderIds, CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		total = IdUtil.getTotal();
		//根据订单复写fund类型的toId
		List<Long> fundIds = tCsqOrders.stream()
			.filter(a -> CsqEntityTypeEnum.TYPE_FUND.toCode() == a.getToType())    //基金类型
			.map(TCsqOrder::getToId).distinct().collect(Collectors.toList());

		List<TCsqService> csqServices = fundIds.isEmpty()? new ArrayList(): csqServiceDao.selectInFundIds(fundIds);
		Map<Long, List<TCsqService>> fundIdServiceMap = csqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getFundId));
		tCsqOrders = tCsqOrders.stream()
			.map(a -> {
				Integer toType = a.getToType();
				if (CsqEntityTypeEnum.TYPE_FUND.toCode() == a.getToType()) {    //基金类型
					//将基金对应的serviceId映射的到原有的toId上
					List<TCsqService> tCsqServices = fundIdServiceMap.get(a.getToId());
					if (tCsqServices != null) {
						TCsqService csqService = tCsqServices.get(0);
						a.setToId(csqService.getId());
					}
				}
				return a;
			}).collect(Collectors.toList());

		List<Long> csqServiceIds = tCsqOrders.stream().map(TCsqOrder::getToId).collect(Collectors.toList());

		List<TCsqService> tCsqServices = csqServiceIds.isEmpty()? new ArrayList<>(): csqServiceDao.selectInIds(csqServiceIds);
		/*if(pageNum != null && pageSize != null) {
//			Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
//			total = startPage.getTotal();
			total = IdUtil.getTotal();
		}*/
		Map<Long, List<TCsqService>> collect = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));
		List<TCsqOrder> resultList = tCsqOrders.stream()
			.map(a -> {
				a.setDate(DateUtil.timeStamp2Date(a.getUpdateTime().getTime(), "yyyy/MM/dd"));
				List<TCsqService> tempList = collect.get(a.getId());
				if (tempList != null && !tempList.isEmpty()) {
					TCsqService tempService = tempList.get(0);
					a.setServiceName(tempService.getName());
				}
				return a;
			}).collect(Collectors.toList());

		queryResult.setResultList(resultList);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	@Override
	public Map<String, Object> share(Long userId, Long fundId) {
		Map<String, Object> map = new HashMap<>();
		String qrCode = "";	//TODO 二维码(根据具体的分享后逻辑完善)
		map.put("qrCode", qrCode);
		//查询捐入列表
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOut(fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqUserPaymentEnum.INOUT_IN.toCode());	//分页标记
		if(tCsqUserPaymentRecords.isEmpty()) {
			return map;
		}
		List<Long> userIds = tCsqUserPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getUserId).collect(Collectors.toList());
		List<TCsqUser> tUsers = userDao.selectInIds(userIds);
		Map<Long, List<TCsqUser>> collect = tUsers.stream()
			.collect(Collectors.groupingBy(TCsqUser::getId));
		List<CsqUserPaymentRecordVo> donateInList = tCsqUserPaymentRecords.stream()
			.map(a -> {
				//几分钟前
				long interval = System.currentTimeMillis() - a.getCreateTime().getTime();
				int minute = (int) (interval / 1000 / 60);
				a.setDate(minute + "分钟前");
				List<TCsqUser> tUsers1 = collect.get(a.getUserId());
				if (tUsers1 != null && !tUsers1.isEmpty()) {
					TCsqUser user = tUsers1.get(0);
					TCsqUser build = TCsqUser.builder().name(user.getName())
						.userHeadPortraitPath(user.getUserHeadPortraitPath()).build();	//可更换为copyInsensitiveUserVo
					a.setUser(build);
				}
				return a.copyUserPaymentRecordVo();
			}).collect(Collectors.toList());
		map.put("donateList", donateInList);
		return map;
	}

	@Override
	public QueryResult<CsqFundVo> list(Long userId, Integer pageNum, Integer pageSize, Integer[] option) {
		pageNum = pageNum==null? 1:pageNum;
		pageSize = pageSize==null? 0:pageSize;
//		Page<Object> startPage = startPage(pageNum, pageSize);
		List<TCsqFund> tCsqFunds = fundDao.selectByUserIdInStatusDescPage(userId, option, pageNum, pageSize);
		long total = IdUtil.getTotal();
		List<CsqFundVo> copyList = tCsqFunds.stream()
			.map(a -> {
				Integer status = a.getStatus();
				Integer raiseStatus = 0;	//筹备中
				raiseStatus = CsqFundEnum.STATUS_PUBLIC.getVal() == status? CsqFundEnum.STATUS_PUBLIC.getVal(): raiseStatus;
				CsqFundVo fundVo = a.copyCsqFundVo();
				fundVo.setRaiseStatus(raiseStatus);
				return fundVo;
			}).collect(Collectors.toList());
		QueryResult<CsqFundVo> tCsqFundQueryResult = new QueryResult<>();
		tCsqFundQueryResult.setResultList(copyList);
		tCsqFundQueryResult.setTotalCount(total);
		return tCsqFundQueryResult;
	}

	@Override
	public void insertForSomeOne(Long userId) {
		TCsqFund build = TCsqFund.builder()
			.userId(userId).build();
		int save = MybatisPlus.getInstance().save(build);
		System.out.println(save);
	}

	@Override
	public QueryResult donateServiceList(Long fundId, Integer pageNum, Integer pageSize) {
		//根据serviceId获取fundId
//		Long fundId = getFundId(serviceId);

//		return getGotoList(fundId, pageNum, pageSize);
		return getGotoListByPaymentRecord(fundId, pageNum, pageSize);
	}

	private QueryResult getGotoListByPaymentRecord(Long fundId, Integer pageNum, Integer pageSize) {
		QueryResult queryResult = getFundOutPaymentPage(fundId, pageNum, pageSize);
		List resultList = queryResult.getResultList();
		//按年份分组
		List<Map<String, Object>> mapList = getMapList(resultList);
		queryResult.setResultList(mapList);
		return queryResult;
	}

	/*public static void main(String[] args) {
		*//*String description = "向\"Fancy\uD83D\uDE0Bzzz\"基金";
		description = "从魏老爸和粉丝们公益基金基金拨款";
		description = description.startsWith("向")?description.substring(1):description;
		System.out.println(description);
		description = !description.contains("\"")?new StringBuilder(description).insert(description.length(), "\"").insert(0, "\"").append("事务花费").toString():description;
		System.out.println(description);*//**//*

		String description = "项目活动费用";
		String name = description;
		String s = new StringBuilder("因").append(name).insert(name.length() + 1, "\"").insert(1, "\"").append("事务花费").toString();
		name = !name.endsWith("\"")?s:name;
		System.out.println(name);*//*
	}*/

	private QueryResult getFundOutPaymentPage(Long fundId, Integer pageNum, Integer pageSize) {
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOutDescPage(fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqUserPaymentEnum.INOUT_OUT.toCode(), pageNum, pageSize);
		long total = IdUtil.getTotal();
		//若要筛选出真实名称
		/*List<Long> orderIds = tCsqUserPaymentRecords.stream()
			.filter(a -> a.getOrderId() != null)
			.map(TCsqUserPaymentRecord::getOrderId).collect(Collectors.toList());
		List<TCsqUserPaymentRecord> theOtherUserPayments = paymentDao.selectInOrderIdsAndInOut(orderIds, CsqUserPaymentEnum.INOUT_IN.toCode());	//找到项目对应的payments
		List<Long> fundIds = theOtherUserPayments.stream()
			.filter(a -> CsqEntityTypeEnum.TYPE_FUND.toCode() == a.getEntityType())
			.map(TCsqUserPaymentRecord::getEntityId).collect(Collectors.toList());
		List<Long> serviceIds = theOtherUserPayments.stream()
			.filter(a -> CsqEntityTypeEnum.TYPE_SERVICE.toCode() == a.getEntityType())
			.map(TCsqUserPaymentRecord::getEntityId).collect(Collectors.toList());
		List<TCsqService> tCsqService = csqServiceDao.selectInIds(serviceIds);
		List<TCsqFund> tCsqFunds = fundDao.selectInIds(fundIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqService.stream().collect(Collectors.groupingBy(TCsqService::getId));
		Map<Long, List<TCsqFund>> fundMap = tCsqFunds.stream().collect(Collectors.groupingBy(TCsqFund::getId));*/

		//装载
		List<CsqFundDonateVo> resultList = tCsqUserPaymentRecords.stream()
			.map(a -> {
				Long time = a.getCreateTime().getTime();
				Double money = a.getMoney();
				String name = a.getDescription();
				name = !name.contains("\"")?new StringBuilder("因").append(name).insert(name.length() + 1, "\"").insert(1, "\"").append("事务花费").toString():name;
				CsqFundDonateVo build = CsqFundDonateVo.builder()
					.year(DateUtil.timeStamp2Date(time, "yyyy"))
					.date(DateUtil.timeStamp2Date(time, "MM-dd"))
					.money(money)
					.name(name).build();
				String wholeDescription = "";
				if(a.getOrderId() != null) {
					wholeDescription = new StringBuilder().append(name).append("捐款")
//						.append(money).append("元")
						.toString();
				} else {
					wholeDescription = new StringBuilder().append(name)
//						.append(money).append("元")
						.toString();
				}
				build.setName(wholeDescription);	//此处故意这样使用
				build.setWholeDecription(name);
			/*
			//若要筛选出真实名称
				Long entityId = a.getEntityId();
				if(a.getOrderId() != null) {
					String serviceName = "未知项目或基金";
					List<TCsqService> tCsqServices = serviceMap.get(entityId);
					if(tCsqServices.isEmpty()) {
						List<TCsqFund> tCsqFundList = fundMap.get(entityId);
						tCsqFundList.get(0).getName();
					}
					serviceName = tCsqServices.get(0).getName();
					csqFundDonateVo.setName(serviceName);
				}
				*/
				return build;
			}).collect(Collectors.toList());
		QueryResult result = new QueryResult();
		result.setResultList(resultList);
		result.setTotalCount(total);
		return result;
	}

	private Long getFundId(Long serviceId) {
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		//check
		if(csqService == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的项目编号:该项目不存在");
		}

		Integer type = csqService.getType();
		if(CsqServiceEnum.TYPE_FUND.getCode() != type) {	//如果不是基金
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的项目有编号:不是基金类型!");
		}
		return csqService.getFundId();
	}

	@Override
	public Long getServiceId(Long fundId) {
		TCsqService csqService = csqServiceDao.selectByFundId(fundId);
		if(csqService == null) {
			return null;
		}
		return csqService.getId();
	}

	private Page<Object> startPage(Integer pageNum, Integer pageSize) {
		pageNum = pageNum == null? 1:pageNum;
		pageSize = pageSize == null? 0:pageSize;
		return PageHelper.startPage(pageNum, pageSize);
	}

	@Override
	public boolean isMine(Long fundId, Long userId) {
		return fundDao.selectByUserIdAndPrimaryKey(userId, fundId) != null;
	}

	@Override
	public List<TCsqFund> selectAllAndIdGreaterThan(long l) {
		return MybatisPlus.getInstance().findAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES)
			.gt(TCsqFund::getId, l));
	}

	@Override
	public HashMap<String, Object> searchList(Boolean isFundParam, String searchParam, Integer status, List<String> trendPubkeys, Integer pageNum, Integer pageSize, Boolean fuzzySearch) {
		MybatisPlusBuild baseBuild = fundDao.baseBuild();
		if(!StringUtil.isEmpty(searchParam)) {
			if(! isFundParam) {	//用户名查找
				List<TCsqFund> tCsqFundList = fundDao.selectAll();
				List<Long> userIds = tCsqFundList.stream()
					.map(TCsqFund::getUserId).collect(Collectors.toList());
				List<TCsqUser> tCsqUsers = userDao.selectInIds(userIds);
				List<String> userNames = tCsqUsers.stream().map(TCsqUser::getName).collect(Collectors.toList());
				List<String> matchUserNames = new ArrayList<>();
				matchUserNames.add(searchParam);
				matchUserNames = fuzzySearch? userNames.stream()
					.filter(a -> a.contains(searchParam)).collect(Collectors.toList()) : matchUserNames;

				Map<String, List<TCsqUser>> nameUserMap = tCsqUsers.stream().collect(Collectors.groupingBy(TCsqUser::getName));
				ArrayList<Long> createrIdsCondition = new ArrayList<>();
				List<String> finalMatchUserNames = matchUserNames;
				nameUserMap.forEach(
					(k,v) -> {
						TCsqUser csqUser = v.get(0);
						if(finalMatchUserNames.contains(k)) {
							Long id = csqUser.getId();
							createrIdsCondition.add(id);
						}
					}
				);
				baseBuild
					.in(TCsqFund::getUserId, createrIdsCondition);
			} else {
				baseBuild = fuzzySearch? baseBuild.like(TCsqFund::getName, "%" + searchParam + "%"): baseBuild.eq(TCsqFund::getName, searchParam);
			}
		}

		//状态
		baseBuild = status == null? baseBuild:
				baseBuild
				.eq(TCsqFund::getStatus, status);
		baseBuild.neq(TCsqFund::getStatus, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());

		Iterator<String> iterator = trendPubkeys.iterator();
		while(iterator.hasNext()) {
			String next = iterator.next();
			baseBuild.like(TCsqFund::getTrendPubKeys, "%" + next + "%");	//倾向
			if(iterator.hasNext()) {
//				baseBuild.and();	//同时包含指定的trendPubkeys
			}
		}

		baseBuild.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqFund::getCreateTime));

		List<TCsqFund> tCsqFundList = fundDao.selectWithBuildPage(baseBuild, pageNum, pageSize);
		long total = IdUtil.getTotal();

		//统计trendPubkeys的收入
		List<TCsqFund> csqFunds = fundDao.selectWithBuild(baseBuild);
		List<Long> fundIds = csqFunds.stream()
			.map(TCsqFund::getId).collect(Collectors.toList());
		List<TCsqOrder> orders = csqOrderDao.selectInToIdAndToTypeAndStatusDesc(fundIds, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		Double pubKeyTotal = orders.stream()
			.map(TCsqOrder::getPrice).reduce(0d, Double::sum);

		QueryResult<TCsqFund> queryResult = getQueryResult(tCsqFundList, total);
		HashMap<String, Object> resMap = new HashMap<>();
		resMap.put("pubKeyTotal", pubKeyTotal);
		resMap.put("queryResult", queryResult);

		return resMap;
	}

	@Override
	public void modifyFundManager(Long managerId, TCsqFund fund) {
		// check manager
//		TCsqUser tCsqUser = userDao.selectByPrimaryKey(managerId);
		Long fundId = fund.getId();
		TCsqFund csqFund = getFundIfNotNullFundId(fundId);
		Integer currentStatus = csqFund.getStatus();
		Integer status = fund.getStatus();
		if(status != null) {	//针对状态的修改
			if(CsqFundEnum.STATUS_ACTIVATED.getVal() == currentStatus && CsqFundEnum.STATUS_PUBLIC.getVal() == status) {	//进行公开基金
				Double totalIn = csqFund.getSumTotalIn();
				if(!checkIsOkForPublic(csqFund)) {
					if(totalIn < CsqFundEnum.PUBLIC_MINIMUM) {	//未达到标准
						//给予提示
					}
					if(checkFundCompletion(csqFund)) {
						//给予提示
					}
					if(CsqFundEnum.IS_SHOWN_NO.getVal() == csqFund.getIsShown()) {	//当前为不可展示
//						fund.setIsShown(CsqFundEnum.IS_SHOWN_YES.getVal());	//TODO 是否修改为显示
					}
					//发送基金开放提示
					csqMsgService.sendServiceMsgForFund(csqFund, csqFund.getUserId());
				}
			}
			//注意如果是用于强制关闭基金，应当修改可展示状态为不可展示，而不是修改status为未公开, 否则关闭之后满足条件继续开放
			//注意修改为关闭之后，除非管理员再次发送开放指令，否则基金即使达成条件不会被开放
			if(CsqFundEnum.STATUS_PUBLIC.getVal() == currentStatus && CsqFundEnum.STATUS_ACTIVATED.getVal() == status) {	//TODO 如果选择了其他状态，是否认为将其置为不可展示
//				fund.setIsShown(CsqFundEnum.IS_SHOWN_NO.getVal());
			}
		}

		Integer isShown = fund.getIsShown();
		if(isShown != null) {	//针对可展示状态的修改

		}

		//若 status =2 直接处理成【公开】
		fund.setUserId(null);	//可能与基金持有者不同的修改人
		fundDao.update(fund);
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
				csqServiceService.synchronizeService(csqFund);
			}
		});
	}

	@Override
	public Map<String, Object> getTotalBalance() {
		List<TCsqFund> csqFunds = fundDao.selectAll();
		Double reduce = csqFunds.stream()
			.filter(a -> CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal() != a.getStatus())
			.map(TCsqFund::getBalance).reduce(0d, Double::sum);

		Map<String, Object> map = new HashMap<>();
		map.put("fundTotal", reduce);
		return map;
	}

	private TCsqFund getFundIfNotNullFundId(Long fundId) {
		if(fundId == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "fundId不能为空!");
		}
		return fundDao.selectByPrimaryKey(fundId);
	}

	private QueryResult<TCsqFund> getQueryResult(List<TCsqFund> tCsqFundList, long total) {
		QueryResult<TCsqFund> tCsqFundQueryResult = new QueryResult<>();
		tCsqFundQueryResult.setResultList(tCsqFundList);
		tCsqFundQueryResult.setTotalCount(total);

		return tCsqFundQueryResult;
	}

}
