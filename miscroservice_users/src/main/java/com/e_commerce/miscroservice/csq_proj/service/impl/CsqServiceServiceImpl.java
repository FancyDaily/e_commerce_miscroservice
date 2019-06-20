package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import com.e_commerce.miscroservice.csq_proj.service.CsqServiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundVo;
import com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 14:02
 */
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

	@Override
	public void publish(Long userId, TCsqService service) {
		//check用户是否满足发布项目的前置条件
		//1.组织账户
		//2.已通过机构认证
		TCsqUser user = userDao.selectByPrimaryKey(userId);
		checkPubAuth(user);	//最好放在填写之前

		//插入一条记录
		service.setId(null);
//		service.setStatus(CsqServiceEnum.STATUS_UNDER_CERT.getCode());
		service.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		service.setType(CsqServiceEnum.TYPE_SERIVE.getCode());
		csqServiceDao.insert(service);
	}

	@Override
	public QueryResult<TCsqService> list(Long userId, Integer option, Integer pageNum, Integer pageSize) {
		Integer OPTION_ALL = 0;
		Integer OPTION_MINE = 2;

		Integer OPTION_DONATED = 1;
		option = option == null? OPTION_ALL:option;

		Page<Object> startPage = new Page<>();
		List<TCsqService> tCsqServices = new ArrayList<>();
		if(OPTION_MINE.equals(option)) {
			if(userId == null) {
				QueryResult<TCsqService> objectQueryResult = new QueryResult<>();
				objectQueryResult.setResultList(new ArrayList<>());
				//或抛出登录信号
				return objectQueryResult;
			}
			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = csqServiceDao.selectMine(userId);
		} else if(OPTION_ALL.equals(option)) {
			startPage = PageHelper.startPage(pageNum, pageSize);
			tCsqServices = csqServiceDao.selectAll();
		} else if(OPTION_DONATED.equals(option)){
			//找到我捐助过的项目记录
			List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndToTypeDesc(userId, CSqUserPaymentEnum.TYPE_SERVICE.toCode());
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

		//处理数据 -> 如果为基金，获取即时的信息
		List<Long> fundIds = tCsqServices.stream()
			.filter(a -> CsqServiceEnum.TYPE_FUND.getCode() == a.getType())	//基金类型
			.map(TCsqService::getFundId)
			.collect(Collectors.toList());
		List<TCsqFund> csqFunds = csqFundDao.selectInIds(fundIds);	//得到目标基金信息
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
		QueryResult<TCsqService> result = new QueryResult<>();
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
		if(userId.equals(tCsqService.getUserId())) {
			isMine = true;
		}
		//进入查询过程
		Integer type = tCsqService.getType();

		if(CsqServiceEnum.TYPE_FUND.getCode() == type) {
			//查询基金
			isFund = true;
			Long fundId = tCsqService.getFundId();
			CsqFundVo csqFundVo = csqFundService.fundDetail(fundId);
			resultMap.put("csqService", csqFundVo);
		} else {
			Double sumTotalIn = tCsqService.getSumTotalIn();
			Double surplusAmount = tCsqService.getSurplusAmount();
			tCsqService.setSumTotalOut(sumTotalIn - surplusAmount);	//剩余金额
			//捐入流水
			List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = paymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(serviceId, CSqUserPaymentEnum.TYPE_SERVICE.toCode(), CSqUserPaymentEnum.INOUT_IN.toCode());	//TODO 分页
			//统计捐款数，获取top10
			Map<Long, List<TCsqUserPaymentRecord>> collect1 = tCsqUserPaymentRecords.stream()
				.collect(Collectors.groupingBy(TCsqUserPaymentRecord::getUserId));
			Map<String, Object> functionMap = new HashMap<>();
			for(Map.Entry entry:collect1.entrySet()) {
				Long theUserId = (Long) entry.getKey();
				List<TCsqUserPaymentRecord> value = (List<TCsqUserPaymentRecord>) entry.getValue();
				Double totalMoney = value.stream()
					.map(TCsqUserPaymentRecord::getMoney)
					.reduce(0d, (a, b) -> a + b);
				functionMap.put(theUserId.toString(), totalMoney);
				Double max = (Double) functionMap.get("max");
				if(totalMoney >= max) {
					functionMap.put("max", totalMoney);
					List<Long> maxer = (List<Long>) functionMap.get(totalMoney.toString());
					if(totalMoney.equals(max)) {
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
			if(max != null) {
				List<Long> maxer = (List<Long>) functionMap.get(max);
				if(maxer!=null) {
					donaters = userDao.selectInIds(maxer);
				}
			}
			donaters = donaters.stream()
				.map(a -> {
					Double totalDonate = (Double) functionMap.get(a.getId());
					if (totalDonate != null) {
						a.setTotalDonate(totalDonate);
					}
					return a;
				}).collect(Collectors.toList());
			tCsqService.setDonaters(donaters);
			//捐助列表
			List<Long> userIds = tCsqUserPaymentRecords.stream()
				.map(TCsqUserPaymentRecord::getUserId)
				.collect(Collectors.toList());
			List<TCsqUser> tCsqUsers = userDao.selectInIds(userIds);
			Map<Long, List<TCsqUser>> collect = tCsqUsers.stream().collect(Collectors.groupingBy(TCsqUser::getId));
			List<TCsqUserPaymentRecord> csqUserPaymentRecords = tCsqUserPaymentRecords.stream()
				.map(a -> {
						Timestamp createTime = a.getCreateTime();
						long interval = System.currentTimeMillis() - createTime.getTime();
						int minutes = DateUtil.millsToMinutes(interval);
						List<TCsqUser> tCsqUsers1 = collect.get(a.getUser());
						if (tCsqUsers1 != null) {
							TCsqUser tCsqUser = tCsqUsers1.get(0);
							tCsqUser.setMinutesAgo(minutes);
						}
						return a;
					}
				).collect(Collectors.toList());
			tCsqService.setCsqUserPaymentRecords(csqUserPaymentRecords);
			// 若为已捐款，则还需要项目汇报信息（PC端开发时再添加）
			resultMap.put("csqService", tCsqService);
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
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqUserPaymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(serviceId, CSqUserPaymentEnum.TYPE_SERVICE.toCode(), CSqUserPaymentEnum.INOUT_OUT.toCode());
		Map<Long, List<TCsqService>> serviceMap = getServiceMap(tCsqUserPaymentRecords);
		List<TCsqUserPaymentRecord> userPaymentRecords = tCsqUserPaymentRecords.stream()
			.map(a -> {
				List<TCsqService> tCsqServices = serviceMap.get(a.getEntityId());
				TCsqService tCsqService = tCsqServices.get(0);
				a.setDate(com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				a.setServiceName(tCsqService.getName());
				return a;
			}).collect(Collectors.toList());
		QueryResult<TCsqUserPaymentRecord> queryResult = new QueryResult<>();
		queryResult.setResultList(userPaymentRecords);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

	private Map<Long, List<TCsqService>> getServiceMap(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		List<Long> serviceIds = tCsqUserPaymentRecords.stream()
			.filter(a -> a.getEntityType()==CSqUserPaymentEnum.TYPE_SERVICE.toCode())
			.map(TCsqUserPaymentRecord::getEntityId)
			.collect(Collectors.toList());
		List<TCsqService> tCsqServices = csqServiceDao.selectInIds(serviceIds);
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
		if(tCsqOrder == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单号错误!");
		}
		if(tCsqOrder.getStatus() == CsqOrderEnum.STATUS_ALREADY_PAY.getCode()) {
			return;
		}
		//插入流水
		//防止重复插入
		Double price = tCsqOrder.getPrice();
		TCsqUserPaymentRecord build1 = TCsqUserPaymentRecord.builder()
			.userId(tCsqOrder.getUserId())
			.orderId(tCsqOrder.getId())
			.money(price)
			.entityId(tCsqOrder.getFromId())	//来源
			.entityType(tCsqOrder.getFromType())
			.inOut(CSqUserPaymentEnum.INOUT_OUT.toCode()).build();

		TCsqUserPaymentRecord build2 = build1;
		Long serviceId = tCsqOrder.getToId();
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		build2.setUserId(csqService.getUserId());
		build2.setEntityId(serviceId);
		build2.setEntityType(tCsqOrder.getToType());
		csqUserPaymentDao.insert(build1, build2);
		//TODO 捐助人的个人捐助次数、基金捐助次数（如果来源为基金）、项目受助次数等增加

		tCsqOrder.setStatus(CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		csqOrderDao.update(tCsqOrder);

	}

	private void checkPubAuth(TCsqUser user) {
		Integer accountType = user.getAccountType();
		if(!CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode().equals(accountType)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "非机构账户不能发布项目");
		}
		Integer authenticationStatus = user.getAuthenticationStatus();
		if(!CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您还未通过机构实名认证，无法发布");
		}
	}

	private TCsqService transferAttrs(TCsqService target, TCsqFund origin) {
		target.setSumTotalIn(origin.getSumTotalIn());
		target.setTotalInCnt(origin.getTotalInCnt());
		target.setSurplusAmount(origin.getBalance());
		target.setName(origin.getName());
		target.setDesc(origin.getDescription());
		target.setCoverPic(origin.getCoverPic());
		target.setTypePubKeys(origin.getTrendPubKeys());
		return target;
	}

}
