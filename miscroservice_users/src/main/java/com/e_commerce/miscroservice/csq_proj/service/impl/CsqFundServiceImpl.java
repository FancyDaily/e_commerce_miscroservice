package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundDonateVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import com.e_commerce.miscroservice.csq_proj.service.CsqOrderService;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		//check实名
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		if(!CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(tCsqUser.getAuthenticationStatus())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请先进行实名认证!");
		}
		Long fundId = fund.getId();
		if(fundId == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "fundId不能为空!");
		}
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		Integer currentStatus = csqFund.getStatus();
		//包含[申请公开]基金业务
		Integer status = fund.getStatus();
		if(status != null) {
			if(CsqFundEnum.STATUS_ACTIVATED.getVal() == currentStatus && CsqFundEnum.STATUS_PUBLIC.getVal() == status) {	//申请公开基金
				Double totalIn = csqFund.getSumTotalIn();
				if(totalIn < CsqFundEnum.PUBLIC_MINIMUM) {	//未达到标准
					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您的基金未达到标准，请再接再厉!");
				}
			}
		}
		//若 status =2 直接处理成【公开】
		fund.setBalance(null);	//若仅用于基本信息修改则不允许修改金额
		fundDao.update(fund);
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
	public CsqFundVo fundDetail(Long fundId) {
		if(fundId == null) {
			throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "参数基金编号为空!");
		}
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		if(csqFund == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "");
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
		List<TCsqOrder> resultList = getGotoListNonePage(fundId, tCsqUserPaymentRecords);
		csqFundVo.setGoToList(resultList);
		return csqFundVo;
	}

	@Override
	public QueryResult getGotoList(Long fundId, Integer pageNum, Integer pageSize) {
		QueryResult result = new QueryResult();
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqOrder> gotoListNonePage = getGotoListNonePage(fundId, null);
		result.setTotalCount(startPage.getTotal());
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

	private List<TCsqOrder> getGotoListNonePage(Long fundId, List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		if(tCsqUserPaymentRecords==null) {
			//根据fundId获取值
			tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOut(fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqUserPaymentEnum.INOUT_IN.toCode());
		}
		List<Long> tOrderIds = tCsqUserPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getOrderId)
			.collect(Collectors.toList());
		if(tCsqUserPaymentRecords.isEmpty()) {
			return new ArrayList<>();
		}
			List<TCsqOrder> tCsqOrders = csqOrderDao.selectByFromIdAndFromTypeAndToTypeInOrderIdsAndStatus(fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqEntityTypeEnum.TYPE_SERVICE.toCode(), tOrderIds, CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		List<Long> csqServiceIds = tCsqOrders.stream().map(TCsqOrder::getToId).collect(Collectors.toList());
		List<TCsqService> tCsqServices = csqServiceIds.isEmpty()? new ArrayList<>(): csqServiceDao.selectInIds(csqServiceIds);
		Map<Long, List<TCsqService>> collect = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));
		return tCsqOrders.stream()
			.map(a -> {
				a.setDate(DateUtil.timeStamp2Date(a.getUpdateTime().getTime(), "yyyy/MM/dd"));
				List<TCsqService> tempList = collect.get(a.getId());
				if (tempList != null && !tempList.isEmpty()) {
					TCsqService tempService = tempList.get(0);
					a.setServiceName(tempService.getName());
				}
				return a;
			}).collect(Collectors.toList());
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
		Page<Object> startPage = startPage(pageNum, pageSize);
		List<TCsqFund> tCsqFunds = fundDao.selectByUserIdInStatusDesc(userId, option);
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
		tCsqFundQueryResult.setTotalCount(startPage.getTotal());
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
		return getGotoList(fundId, pageNum, pageSize);
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

	private Page<Object> startPage(Integer pageNum, Integer pageSize) {
		pageNum = pageNum == null? 1:pageNum;
		pageSize = pageSize == null? 0:pageSize;
		return PageHelper.startPage(pageNum, pageSize);
	}

	@Override
	public boolean isMine(Long fundId, Long userId) {
		return fundDao.selectByUserIdAndPrimaryKey(userId, fundId) != null;
	}

}
