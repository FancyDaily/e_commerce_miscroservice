package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.LimitQueue;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.AesUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.RandomUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.*;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDonateRecordVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceListVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceMsgParamVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqSimpleServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:41
 */
@Transactional(rollbackFor = Throwable.class)
@Service
@Log
public class CsqPaySerrviceImpl implements CsqPayService {

	@Autowired
	private CsqMsgService csqMsgService;

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Autowired
	private CsqFundDao csqFundDao;

	@Autowired
	private CsqWeChatPay wechatPay;

	@Autowired
	private CsqUserPaymentDao csqUserPaymentDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqUserDao csqUserDao;

	@Autowired
	private CsqKeyValueDao csqKeyValueDao;

	@Autowired
	private CsqServiceService csqServiceService;

	@Autowired
	private CsqPaymentService csqPaymentService;

	@Autowired
	private CsqMsgDao csqMsgDao;

	@Autowired
	private CsqUserService csqUserService;

	@Autowired
	private CsqFundService csqFundService;

	@Autowired
	private CsqPublishService csqPublishService;

	@Autowired
	@Qualifier("csqRedisTemplate")
	HashOperations<String, String, Object> userRedisTemplate;

	@Override
	public Object preFundOrder(Long userId, Double amount) {	//发起支付后的业务流程
		//TODO 用户性质的特异行为
		//用户发起支付后产生的业务行为
		//1.判断是否需要产生基金 -> 判断依据: （对一个用户只能创建一个基金这件事不在此做校验）
		long currentTimeMillis = System.currentTimeMillis();
		TCsqFund csqFund = null;
		List<TCsqFund> tCsqFunds = csqFundDao.selectByUserIdAndStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
		if(tCsqFunds.isEmpty()) {	//如果不存在
			csqFund = TCsqFund.builder().status(CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal()).build();
			csqFundDao.insert(csqFund);
		} else {
			csqFund = tCsqFunds.get(0);
		}
		//2.查看是否可以复用未支付的相同金额有效订单
		Long fundId = csqFund.getId();
		TCsqOrder csqOrder = csqOrderDao.selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(userId, userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), amount, CsqOrderEnum.STATUS_UNPAY.getCode());
		//TODO 对失活订单置成无效状态(不使用Mq时)
		if(csqOrder==null) {	//创建一个订单
			String orderNo = UUIdUtil.generateOrderNo();
			csqOrder = TCsqOrder.builder().status(CsqOrderEnum.STATUS_UNPAY.getCode())
				.userId(userId)
				.fromId(userId)
				.fromType(CsqEntityTypeEnum.TYPE_HUMAN.toCode())
				.toId(fundId)
				.toType(CsqEntityTypeEnum.TYPE_FUND.toCode())
				.orderTime(currentTimeMillis)
				.orderNo(orderNo)
				.price(amount)
				.userId(userId).build();
		}
		// 把一些参数返回给前端或者上一级方法
		Map resultMap = new HashMap();
		return resultMap;
	}

	@Override
	public Map<String, String> preOrder(Long userId, String orderNo, Long entityId, Integer entityType, Double fee, HttpServletRequest httpServletRequest) throws Exception {
		return preOrder(userId, orderNo, entityId, entityType, fee, httpServletRequest, null, false);
	}

	@Override
	public Map<String, String> preOrder(Long userId, String orderNo, Long entityId, Integer entityType, Double fee, HttpServletRequest httpServletRequest, TCsqFund csqfund, boolean isAnonymous) throws Exception {
		//针对不同的实体类型，有不同的支付前逻辑(eg. 产生待激活的基金等)
		TCsqFund csqFund = dealwithFundBeforePay(entityType, userId, csqfund);	//针对创建基金业务
		dealwithAccountTrendPubKeys(userId, entityType, csqfund);
		if(csqfund == null && entityId == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "充值基金时，编号不能为空!");
		}
		entityId = entityId == null? csqFund.getId(): entityId;
		orderNo = dealWithPreOrder(userId, entityId, entityType, fee, isAnonymous);
		String attach = entityType.toString();	//支付目标的类型
		//向微信请求发起支付
		Map<String, String> webParam = wechatPay.createWebParam(orderNo, fee, httpServletRequest, attach, false);
		webParam.put("orderNo", orderNo);
		return webParam;
	}

	private void dealwithAccountTrendPubKeys(Long userId, Integer entityType, TCsqFund csqfund) {
		checkTypeCreateAccount(userId, entityType, csqfund);
		String trendPubKeys = csqfund.getTrendPubKeys();
		dealwithAccountTrendPubKeys(userId, trendPubKeys);
	}

	private void checkTypeCreateAccount(Long userId, Integer entityType, TCsqFund csqfund) {
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		Integer balanceStatus = csqUser.getBalanceStatus();
		if(!CsqUserEnum.BALANCE_STATUS_WAIT_ACTIVATE.toCode().equals(balanceStatus)) {	//不是开启业务
			return;
		}
		//check
		if(CsqEntityTypeEnum.TYPE_ACCOUNT.toCode() == entityType && (csqfund==null || StringUtil.isEmpty(csqfund.getTrendPubKeys()))) {	//表示爱心账户充值
			throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "开通爱心账户时，参数不全！");
		}
	}

	private void dealwithAccountTrendPubKeys(Long userId, String trendPubKeys) {
		TCsqUser build = TCsqUser.builder()
			.id(userId)
			.trendPubKeys(trendPubKeys).build();
		csqUserDao.updateByPrimaryKey(build);	//更新账户倾向
	}

	private String dealWithPreOrder(Long userId, Long entityId, Integer entityType, Double fee, boolean isAnonymous) {
		Integer anonymous = isAnonymous? CsqOrderEnum.IS_ANONYMOUS_TRUE.getCode(): CsqOrderEnum.IS_ANONYMOUS_FALSE.getCode();
		String orderNo;
		orderNo = UUIdUtil.generateOrderNo();	//默认生成新的订单号
		//查看对于同一用户同一业务(entityId、entityType、fee相同)	是否有可复用的订单
		TCsqOrder csqOrder = csqOrderDao.selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(userId, entityId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), entityId, entityType, fee, CsqOrderEnum.STATUS_UNPAY.getCode());
		if(csqOrder!=null) {
			boolean expired = System.currentTimeMillis() - csqOrder.getCreateTime().getTime() > 1000l * 60 * 30;
			if (!expired) {	//复用
				orderNo = csqOrder.getOrderNo();
				return orderNo;
			}
		}
		//插入一条新订单
		csqOrder = TCsqOrder.builder().userId(userId)
				.orderNo(orderNo)	//新的订单号
				.fromId(userId)
				.fromType(CsqEntityTypeEnum.TYPE_HUMAN.toCode())
				.toId(entityId)	//默认当传入entity为null时会获取到一个待激活的基金编号(当前只有申请基金业务这个前提下)
				.toType(entityType)
				.price(fee)
				.status(CsqOrderEnum.STATUS_UNPAY.getCode())
				.orderTime(System.currentTimeMillis())
				.isAnonymous(anonymous)
				.build();
		csqOrderDao.insert(csqOrder);
		return orderNo;
	}

	private TCsqFund dealwithFundBeforePay(Integer entityType, Long userId, TCsqFund csqFund) {
		return dealwithFundBeforePay(entityType, userId, csqFund, false);
	}

	private TCsqFund dealwithFundBeforePay(Integer entityType, Long userId, TCsqFund csqFund, boolean isSkipActivate) {
		if(CsqEntityTypeEnum.TYPE_FUND.toCode() != entityType) {
			return null;
		}

		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		if(csqFund == null) {
			return null;
		}
		//check
		List<TCsqFund> tCsqFunds = new ArrayList<>();
//		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
//		if(CsqUserEnum.ACCOUNT_TYPE_PERSON.toCode().equals(csqUser.getAccountType())) {
//			tCsqFunds = csqFundDao.selectByUserIdInStatusDesc(userId, CsqFundEnum.STATUS_PUBLIC.getVal(), CsqFundEnum.STATUS_ACTIVATED.getVal());
			tCsqFunds = csqFundDao.selectByUserId(userId);
			if(!tCsqFunds.isEmpty()) {
				boolean present = tCsqFunds.stream()
					.anyMatch(a -> CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal() != a.getStatus());	//已经存在一个基金，默认为充值
				if(present) {
					return null;
				}
//			}
		}
		//若已有一个待激活的基金，则返回(注意，这是在保证只有一个有效基金的前提下,若开放多个基金，则以前端传入的"开设/充值标志"为准)
		String name = csqFund.getName() == null? csqUser.getName()==null? "未命名的基金":String.format(csqUser.getName(), CsqFundEnum.DEFAULT_NAME_TEMPLATE): csqFund.getName();
		String trendPubKeys = csqFund.getTrendPubKeys() == null? "" : csqFund.getTrendPubKeys();
		List<TCsqFund> waitActivateList = tCsqFunds.stream()
			.filter(a -> CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal() == a.getStatus() && name.equals(a.getName()) && trendPubKeys.equals(a.getTrendPubKeys()))
			.sorted(Comparator.comparing(TCsqFund::getCreateTime).reversed())
			.limit(1)
			.collect(Collectors.toList());
		if(!waitActivateList.isEmpty()) {
			return waitActivateList.get(0);
		}
		//创建一个待激活的基金
		Integer status = isSkipActivate? CsqFundEnum.STATUS_ACTIVATED.getVal():CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal();
		TCsqFund build = TCsqFund.builder()
			.userId(userId)
			.status(status)
			.name(name)
			.trendPubKeys(trendPubKeys)
			.coverPic(CsqFundEnum.DEFAULT_COVER_PIC)    //默认封面
			.build();
		csqFundDao.insert(build);
		return build;
	}

	@Override
	public void wxNotify(HttpServletRequest request, boolean isPay) throws Exception {
		if(isPay) {
			wxNotifyPay(request);
			return;
		}
		wxNotifyRefund(request);
	}

	@Override
	public void preRefund(Long userId, String orderNo, HttpServletRequest request) throws Exception {
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		if(tCsqOrder == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的订单号!");
		}
		log.info("发起退款, tcsqOrder={}", tCsqOrder.toString());
		checkBeforeRefund(userId, tCsqOrder);
		//TODO 向微信发起退款请求
		Double price = tCsqOrder.getPrice();
		Map<String, String> webParam = wechatPay.createRefundWebParam(orderNo, price, request, null);
	}

	private void checkBeforeRefund(Long userId, TCsqOrder tCsqOrder) {
		if(tCsqOrder == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单号错误！");
		}
		//check 用户权限
		if(!tCsqOrder.getUserId().equals(userId)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您不能对别人的订单发起退款!");
		}
		//订单状态，是否可退款
		Integer status = tCsqOrder.getStatus();
		long time = tCsqOrder.getUpdateTime().getTime();
		long inteval = System.currentTimeMillis() - time;
		long yearMills = 365L * 24 * 60 * 60 * 1000;
		boolean expired = inteval > yearMills;
		if(CsqOrderEnum.STATUS_ALREADY_PAY.getCode() != status || expired) {	//不为已支付 || 超过一年的时间(微信退款的期限为交易时间的一年内
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, expired? "已经超过可退款的时限！": "订单的状态有误");
		}

		//是否有足够的余额供返还
		checkEnoughMoney(tCsqOrder);
	}

	private void checkEnoughMoney(TCsqOrder tCsqOrder) {
		Double balance = 0d;
		Integer entityType = tCsqOrder.getToType();
		Long entityId = tCsqOrder.getToId();
		switch (CsqEntityTypeEnum.getEnum(entityType)) {
			case TYPE_FUND:
				TCsqFund csqFund = csqFundDao.selectByPrimaryKey(entityId);
				balance = csqFund.getBalance();
				break;
			case TYPE_ACCOUNT:
				TCsqUser csqUser = csqUserDao.selectByPrimaryKey(entityId);
				balance = csqUser.getSurplusAmount();
				break;
		}

		Double price = tCsqOrder.getPrice();
		if(balance - price < 0) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "余额以不足以发起退款！");
		}
	}

	private void wxNotifyRefund(HttpServletRequest request) throws Exception {
		//接收微信的回调参数
		Map params = wechatPay.doParseRquest(request);
		if("SUCCESS".equals(params.get("return_code"))) {
			//处理，获得订单号
			String encryptData = (String) params.get("req_info");
			encryptData = new String(Base64.getDecoder().decode(encryptData.getBytes()));
			//对商户key做一次md5，得到解密匙
			String key ="";	//TODO
			key = Md5Util.md5(key);
			String decryptDataXml = AesUtil.decode(encryptData, key);
			Map map = wechatPay.parseXmlStr(decryptDataXml);
			String orderNo = (String) map.get("out_trade_no");
			//退款成功业务逻辑
			dealWithOrderNoRefund(orderNo);
		}

	}

	private void dealWithOrderNoRefund(String orderNo) {
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		//判断是否已经完成业务逻辑
		if(CsqOrderEnum.STATUS_ALREADY_REFUND.getCode() == tCsqOrder.getStatus()) {
			return;
		}

		switch (CsqEntityTypeEnum.getEnum(tCsqOrder.getToType())) {
			case TYPE_ACCOUNT:
				dealWithAccountAfterRefund(orderNo);
				break;
			case TYPE_FUND:
				dealWithFundAfterRefund(orderNo);
				break;
		}

		tCsqOrder.setStatus(CsqOrderEnum.STATUS_ALREADY_REFUND.getCode());
		csqOrderDao.update(tCsqOrder);
	}

	private void dealWithFundAfterRefund(String orderNo) {
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		Long fundId = tCsqOrder.getToId();
		Double price = tCsqOrder.getPrice();
		TCsqFund csqFund = csqFundDao.selectByPrimaryKey(fundId);
		//发生退款 -> 余额减少
		Double formerBalance = csqFund.getBalance();
		csqFund.setBalance(formerBalance - price);
		csqFundDao.update(csqFund);
		//插入流水
		Long userId = tCsqOrder.getUserId();
		TCsqUserPaymentRecord build = TCsqUserPaymentRecord.builder()
			.userId(userId)    //支出人
			.orderId(tCsqOrder.getId())
			.money(price)
			.entityId(userId)
			.entityType(CsqEntityTypeEnum.TYPE_FUND.toCode())    //基金支出
			.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode()).build();
		csqUserPaymentDao.insert(build);
		//sysMSg
		String title = "退款成功!";
		String content = "你的订单号为: " + orderNo + "的订单已退款.";
		TCsqSysMsg build1 = TCsqSysMsg.builder()
			.userId(userId)
			.title(title)
			.type(CsqSysMsgEnum.TYPE_NORMAL.getCode())
			.content(content).build();
		csqMsgDao.insert(build1);
	}

	private void dealWithAccountAfterRefund(String orderNo) {
		//账户的退款，检查余额是否足够退款
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		Long userId = tCsqOrder.getToId();
		Double price = tCsqOrder.getPrice();
		TCsqUser user = csqUserDao.selectByPrimaryKey(userId);
		//发生退款 -> 余额减少
		Double surplusAmount = user.getSurplusAmount();
		user.setSurplusAmount(surplusAmount + price);
		csqUserDao.updateByPrimaryKey(user);
		//插入流水
		TCsqUserPaymentRecord build = TCsqUserPaymentRecord.builder()
			.userId(userId)
			.orderId(tCsqOrder.getId())
			.money(price)
			.entityId(userId)
			.entityType(CsqEntityTypeEnum.TYPE_ACCOUNT.toCode())
			.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode()).build();
		// sysMSg
		String title = "退款成功!";
		String content = "你的订单号为: " + orderNo + "的订单已退款.";
		TCsqSysMsg build1 = TCsqSysMsg.builder()
			.userId(userId)
			.title(title)
			.type(CsqSysMsgEnum.TYPE_NORMAL.getCode())
			.content(content).build();
		csqMsgDao.insert(build1);
	}

	private void wxNotifyPay(HttpServletRequest request) throws Exception {
		//解析参数
		Map param = wechatPay.doParseRquest(request);
		if ("SUCCESS".equals(param.get("return_code"))) {
			// 如果返回成功
			String mch_id = (String) param.get("mch_id"); // 商户号
			String out_trade_no = (String) param.get("out_trade_no"); // 商户订单号
			String total_fee = (String) param.get("total_fee");
			// 查询订单 根据订单号查询订单
			System.out.println("商户号" + mch_id + "out_trade_no" + out_trade_no + "total_fee" + total_fee);

			String attach = (String) param.get("attach");

			//支付成功业务流程
			// 应当在生成订单时产生一个(两个)自定义参数以标识是哪种业务类型
			log.info("微信回调, orderNo={}, attch={}", out_trade_no, attach);
			dealWithOrderNoPay(out_trade_no, attach);
		}
	}

	@Override
	public void testDealWithOrderNoPay(String orderNo, String attach) {
		dealWithOrderNoPay(orderNo, attach);
	}

	private void dealWithOrderNoPay(String orderNo, String attach) {
		//防止重复调用
		if(checkStatus(orderNo)) {
			return;
		}
		//对比附加字段是否为该订单的类型
		TCsqOrder tCsqOrder = checkAttach(orderNo, attach);
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				//根据attch的类型, 进行不同的支付后逻辑
				afterPaySuccess(attach, tCsqOrder);
			}
		});
	}

	private boolean checkStatus(String orderNo) {
		boolean isDone = false;
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		if(CsqOrderEnum.STATUS_ALREADY_PAY.getCode() == tCsqOrder.getStatus()) {
			isDone = true;
		}
		return isDone;
	}

	private void afterPaySuccess(String attach, TCsqOrder tCsqOrder) {
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(tCsqOrder.getUserId());
		//强制开通
		switch (CsqEntityTypeEnum.getEnum(Integer.valueOf(attach))) {	//attach: 为哪种类型充值
			case TYPE_FUND:
				activeAccount(csqUser, tCsqOrder);
				dealWithFundAfterPay(tCsqOrder);
				break;
			case TYPE_SERVICE:
				dealWithServiceAfterPay(tCsqOrder);
				break;
			case TYPE_ACCOUNT:
				csqUser = dealWithAccountAfterPay(tCsqOrder);
				break;
		}
		afterPayUser(tCsqOrder, csqUser);	//现金支付才有的累积捐赠总额增加等逻辑

		tCsqOrder.setStatus(CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		csqOrderDao.update(tCsqOrder);

		dealWithRelatedStatistics(tCsqOrder);	//统计与维护

		//处理流水
		csqPaymentService.savePaymentRecord(tCsqOrder);
	}

	private void activeAccount(TCsqUser csqUser, TCsqOrder tCsqOrder) {
		if (CsqUserEnum.BALANCE_STATUS_WAIT_ACTIVATE.toCode().equals(csqUser.getBalanceStatus())) {
			csqUser.setBalanceStatus(CsqUserEnum.BALANCE_STATUS_AVAILABLE.toCode());
			csqMsgService.insertTemplateMsg(CsqSysMsgTemplateEnum.ACCOUNT_ACTIVATE, csqUser.getId());
			//若类型为基金开通，则表明当前为爱心账户未开通情况下去开通爱心账户并开通基金，需要获取基金的意向
			Integer toType = tCsqOrder.getToType();
			if(CsqEntityTypeEnum.TYPE_FUND.toCode() == toType) {	//如果为基金类型(此时可能是充值或者开通,可以不必判断
				Long toId = tCsqOrder.getToId();
				TCsqFund tCsqFund = csqFundDao.selectByPrimaryKey(toId);
//				Integer status = tCsqFund.getStatus();
//				if()
				String trendPubKeys = tCsqFund.getTrendPubKeys();
				csqUser.setTrendPubKeys(trendPubKeys);
			}
		}
	}

	private void afterPayUser(TCsqOrder tCsqOrder, TCsqUser csqUser) {
		if(!checkIsFromUser(tCsqOrder)) {	//现金充值校验
			return;
		}
//		Long userId = tCsqOrder.getUserId();
//		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		//累积捐助金额增加
		csqUser.setSumTotalPay(csqUser.getSumTotalPay() + tCsqOrder.getPrice());
		//累积捐助次数增加
		csqUser.setPayNum(csqUser.getPayNum() + 1);
		csqUserDao.updateByPrimaryKey(csqUser);
	}

	private boolean checkIsFromUser(TCsqOrder tCsqOrder) {
		boolean isFromUser = false;
		Integer fromType = tCsqOrder.getFromType();
		if(CsqEntityTypeEnum.TYPE_HUMAN.toCode() == fromType) {	//如果为现金充值
			isFromUser = true;
		}
		return isFromUser;
	}

	private void dealWithRelatedStatistics(TCsqOrder tCsqOrder) {
		//处理统计相关的字段维护
		Long userId = tCsqOrder.getUserId();
		Integer fromType = tCsqOrder.getFromType();
		Long fromId = tCsqOrder.getFromId();
		Integer toType = tCsqOrder.getToType();
		Long toId = tCsqOrder.getToId();
		Double amount = tCsqOrder.getPrice();

		afterPayService(userId, fromType, fromId, toType, toId, amount);
		afterPayFund(userId, fromType, fromId, toType, toId, amount);
		afterFromFundToService(userId, fromType, fromId, toType, toId);

		insertDailyDonateRecords(tCsqOrder);
	}

	private void insertDailyDonateRecords(TCsqOrder tCsqOrder) {
		Integer toType = tCsqOrder.getToType();
		//check
		//判断是否为向基金或者项目捐款	TODO 是否要排除自己发布的
		if(!Arrays.asList(CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqEntityTypeEnum.TYPE_SERVICE.toCode()).contains(toType)) {
			return;
		}
		Long userId = tCsqOrder.getUserId();	//注意此时若用于代捐会有问题
		Long toId = tCsqOrder.getToId();

		//若为基金获取双生项目Id
		boolean isFund;
		if(isFund = CsqEntityTypeEnum.TYPE_FUND.toCode() == toType) {
			toId = csqFundService.getServiceId(toId);
		}

		//判断是否为日推的项目/基金
		if(!csqUserService.isDailyDonateServiceId(toId)) {
			return;
		}
		//插入相关记录
		//判断今天是否已经插入过
		List<TCsqKeyValue> dailyDonateList = csqKeyValueDao.selectByKeyAndTypeDesc(userId, CsqKeyValueEnum.TYPE_DAILY_DONATE.getCode());	//最新的一条
		if(!dailyDonateList.isEmpty() && DateUtil.isToday(dailyDonateList.get(0).getCreateTime().getTime())) {	//今天已经有一条记录
			return;
		}

		CsqSimpleServiceVo csqSimpleServiceVo = CsqSimpleServiceVo.builder()
			.id(toId)
			.type(isFund ? CsqServiceEnum.TYPE_FUND.getCode() : CsqServiceEnum.TYPE_SERIVE.getCode()).build();
		String jsonString = JSONObject.toJSONString(csqSimpleServiceVo);

		TCsqKeyValue build = TCsqKeyValue.builder()
			.mainKey(userId)
			.type(CsqKeyValueEnum.TYPE_DAILY_DONATE.getCode())
			.theValue(jsonString)
			.build();
		csqKeyValueDao.save(build);
	}

	@Override
	public String withinPlatFormPay(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, TCsqFund csqFund, boolean isAnonymous) {
		//check参数
		List<Integer> types = Arrays.stream(CsqEntityTypeEnum.values())
			.map(CsqEntityTypeEnum::toCode).collect(Collectors.toList());
		if(!types.contains(fromType) || !types.contains(toType)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的实体类型");
		}
		//check from账户的资质(比如基金的开放状态，未开放不允许捐助
		//针对基金充值业务
		checkBeforeFundCharge(fromType, fromId);

		//针对创建基金业务
		if(toId == null && CsqEntityTypeEnum.TYPE_FUND.toCode() == toType) {
			csqFund = dealwithFundBeforePay(toType, userId, csqFund, false);	//基金的支付前逻辑。若可能，包含[创建已激活(未公开)基金]
			if(csqFund !=null) {
				toId = csqFund.getId();
			}
		}

		if(csqFund == null && toId == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "充值基金时，编号不能为空!");
		}

		//处理余额变动
		dealWithSurplusAmount(fromType, fromId, amount);


		String orderNo = UUIdUtil.generateOrderNo();
		//创建已支付的订单
		TCsqOrder order = TCsqOrder.builder()
			.userId(userId)
			.fromType(fromType)
			.fromId(fromId)
			.toType(toType)
			.toId(toId)
			.price(amount)
			.orderNo(orderNo)
			.isAnonymous(isAnonymous? CsqOrderEnum.IS_ANONYMOUS_TRUE.getCode(): CsqOrderEnum.IS_ANONYMOUS_FALSE.getCode())	//匿名
			.status(CsqOrderEnum.STATUS_ALREADY_PAY.getCode())	//状态为已支付
			.orderTime(System.currentTimeMillis()).build();
		csqOrderDao.insert(order);
		Long orderId = order.getId();

		final Long finalToId = toId;
		//处理一些维护字段等
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				csqPaymentService.savePaymentRecord(userId, fromType, fromId, toType, finalToId, amount, orderId);	//流水全处理
				dealWithFundAfterPay(order);
				dealWithServiceAfterPay(order);
				dealWithRelatedStatistics(order);
			}

		});
		return orderNo;
		/*TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				afterPaySuccess(toType.toString(), order);
			}

		});*/
	}

	private void checkBeforeFundCharge(Integer fromType, Long fromId) {
		if(CsqEntityTypeEnum.TYPE_FUND.toCode() == fromType) {
			//基金开放状态
			TCsqFund tCsqFund = csqFundDao.selectByPrimaryKey(fromId);
			Integer status = tCsqFund.getStatus();
			if(CsqFundEnum.STATUS_PUBLIC.getVal() != status) {	//未开放
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前基金未开放，请选择其他支付方式!");
			}
		}
	}

	private void dealWithSurplusAmount(Integer fromType, Long fromId, Double amount) {
		//余额变动
		if(CsqEntityTypeEnum.TYPE_ACCOUNT.toCode() == fromType) {    //账户余额
			//check余额
			TCsqUser csqUser = csqUserDao.selectByPrimaryKey(fromId);
			Double surplusAmount = csqUser.getSurplusAmount();
			if(surplusAmount < amount) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "余额不足!");
			}
			csqUser.setSurplusAmount(surplusAmount - amount);
			csqUserDao.updateByPrimaryKey(csqUser);
		}

		if(CsqEntityTypeEnum.TYPE_FUND.toCode() == fromType) {	//基金
			//check余额
			TCsqFund theCsqFund = csqFundDao.selectByPrimaryKey(fromId);
			Double balance = theCsqFund.getBalance();
			if(balance < amount) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "余额不足!");
			}
			theCsqFund.setBalance(balance - amount);
			csqFundDao.update(theCsqFund);
		}
	}

	private void afterFromFundToService(Long userId, Integer fromType, Long fromId, Integer toType, Long toId) {
		//基金累积资助次数  from 基金 to 项目
		if(CsqEntityTypeEnum.TYPE_FUND.toCode() == fromType && CsqEntityTypeEnum.TYPE_SERVICE.toCode() == toType) {
			TCsqFund csqFund = csqFundDao.selectByPrimaryKey(fromId);
			Integer helpCnt = csqFund.getHelpCnt();
			csqFund.setHelpCnt(++ helpCnt);	//累积捐助次数增加
			csqFundDao.update(csqFund);
		}
	}

	private TCsqUser dealWithAccountAfterPay(TCsqOrder tCsqOrder) {
		Long ownerId = tCsqOrder.getToId();
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(ownerId);
		activeAccount(csqUser, tCsqOrder);
		//充值
		Double price = tCsqOrder.getPrice();
		Double surplusAmount = csqUser.getSurplusAmount();
		csqUser.setSurplusAmount(surplusAmount + price);
//		csqUserDao.updateByPrimaryKey(csqUser);
		return csqUser;
	}

	private void dealWithServiceAfterPay(TCsqOrder tCsqOrder) {
		if(!checkisToService(tCsqOrder)) {
			return;
		}

		//充值
		Long serviceId = tCsqOrder.getToId();
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		Double price = tCsqOrder.getPrice();
		Double surplusAmount = csqService.getSurplusAmount();
		csqService.setSurplusAmount(surplusAmount + price);
		csqServiceDao.update(csqService);

		//流水
		Long payerId = tCsqOrder.getUserId();
		Long ownerId = csqService.getUserId();

		/*//处理服务支付后逻辑
		afterPayService(tCsqOrder.getUserId(), tCsqOrder.getFromType(), tCsqOrder.getFromId(), toType, tCsqOrder.getToId(), tCsqOrder.getPrice());*/
	}

	private boolean checkisToService(TCsqOrder tCsqOrder) {
		Integer toType = tCsqOrder.getToType();
		if(CsqEntityTypeEnum.TYPE_SERVICE.toCode() == toType) {
			return true;
		}
		return false;
	}

	private void dealWithFundAfterPay(TCsqOrder tCsqOrder) {
		if(!checkIsToFund(tCsqOrder)) {
			return;
		}
		//根据订单信息得到
		// -> 基金开户了结
		// -> 仅充值
		Long fundId = tCsqOrder.getToId();
		TCsqFund fund = csqFundDao.selectByPrimaryKey(fundId);
		Integer status = fund.getStatus();
		Long userId = tCsqOrder.getUserId();
		if(CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal() == status) {	//待激活
			//基金开户
			fund.setStatus(CsqFundEnum.STATUS_ACTIVATED.getVal());
			//生成平台认证基金编号
			String fundNo = UUIdUtil.generateOrderNo();	// 生成
			fund.setFundNo(fundNo);
			//创建双生项目
			TCsqService csqService = getFundTypeService(fundId, fund);
			csqServiceDao.insert(csqService);
			//向payer或者基金拥有者发送 sysMsg => 您的基金已经建立
			TCsqSysMsg build = TCsqSysMsg.builder().type(CsqSysMsgEnum.TYPE_NORMAL.getCode())
				.userId(userId)
				.type(CsqSysMsgEnum.TYPE_NORMAL.getCode())
				.title("您的专项基金已经建立")
				.content("只属于您的专项基金现已建立，您可以自由充值和捐赠项目。如有需要，可以使用托管服务,具体细则详见xxxxx.")
				.dateString(DateUtil.timeStamp2Date(System.currentTimeMillis())).build();
//			csqMsgDao.insert(build);	//8.1.2019 删除创建成功消息

			recommendService(userId, null, fund);	//TODO
		}
		Double amount = tCsqOrder.getPrice();
		//资金流入 -> 余额增加
		Double formerBalance = fund.getBalance();
		Double currentBalance = formerBalance + amount;
		fund.setBalance(currentBalance);
		//计算是否满足开放条件
		if(csqFundService.checkIsOkForPublic(fund)) {
			//若满足 -> 提审(略) -> 开放
			fund.setStatus(CsqFundEnum.STATUS_PUBLIC.getVal());
			csqMsgService.sendServiceMsgForFund(fund, userId);
		}
		csqFundDao.update(fund);
		Long payerId = userId;
		Long ownerId = fund.getUserId();
	}

	private boolean checkIsToFund(TCsqOrder tCsqOrder) {
		boolean flag = false;
		Integer toType = tCsqOrder.getToType();
		if(CsqEntityTypeEnum.TYPE_FUND.toCode() == toType) {
			flag = true;
		}
		return flag;
	}

	private TCsqService getFundTypeService(Long fundId, TCsqFund fund) {
		TCsqService csqService = fund.copyCsqService();
		csqService.setId(null);
		csqService.setFundId(fundId);
		csqService.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		csqService.setType(CsqServiceEnum.TYPE_FUND.getCode());
		csqService.setFundStatus(fund.getStatus());
		csqService.setName(fund.getName());
		csqService.setSurplusAmount(fund.getBalance());
		return csqService;
	}

	private void wasted(TCsqOrder tCsqOrder, Long payerId, Long ownerId) {
		//former dealWithUserPaymentRecord
		/*boolean payForSomeOne = !payerId.equals(ownerId);	//为他人支付
		Double amount = tCsqOrder.getPrice();
		//插入流水
		String description;
		String name = null;
		String type = null;
		Integer toType = tCsqOrder.getToType();
		Integer fromType = tCsqOrder.getFromType();
		Long toId = tCsqOrder.getToId();
		CsqEntityTypeEnum anEnum = CsqEntityTypeEnum.getEnum(toType);
		boolean isDonate = false;	//是否为捐助(默认充值
		switch (anEnum) {
			//充值
			case TYPE_ACCOUNT:
				name = "爱心账户";
				break;
			//捐助
			case TYPE_SERVICE:
				TCsqService csqService = csqServiceDao.selectByPrimaryKey(toId);
				name = csqService.getName();
				type = "项目";
				isDonate = true;
				break;
			//不确定类型
			case TYPE_FUND:
				TCsqFund csqFund = csqFundDao.selectByPrimaryKey(toId);
				name = csqFund.getName();
				type = "基金会";
				if(!csqFund.getUserId().equals(payerId)) {	//为他人支付, 流水中(或)描述为捐助
					isDonate = true;
				}
		}
		StringBuilder builder = new StringBuilder();
		description = builder.append("向")
			.append(name)
			.append(type)
			.append(isDonate? "捐助": "充值")
			.toString();
		List<TCsqUserPaymentRecord> toInserter = new ArrayList<>();

		String incomeDescription = "爱心账户充值";
		boolean isFromHuman = CsqEntityTypeEnum.TYPE_HUMAN.toCode() == fromType;
		if(isFromHuman) {	//如果是现金支付、则恒插入一条向payer爱心账户的充值流水
			TCsqUserPaymentRecord toBuild = TCsqUserPaymentRecord.builder()
			.orderId(tCsqOrder.getId())
			.userId(ownerId)
			.entityId(ownerId)
			.entityType(CsqEntityEnum.TYPE_ACCOUNT.toCode())
			.description(incomeDescription)	//收入方描述统一显示被充值
			.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())
			.money(amount).build();
		}
		//收入流水
		TCsqUserPaymentRecord toBuild = TCsqUserPaymentRecord.builder()
			.orderId(tCsqOrder.getId())
			.userId(ownerId)
			.entityId(toId)
			.entityType(toType)
			.description(incomeDescription)	//收入方描述统一显示被充值
			.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())
			.money(amount).build();
		toInserter.add(toBuild);
		//支出流水
		TCsqUserPaymentRecord fromBuild = TCsqUserPaymentRecord.builder()
		.orderId(tCsqOrder.getId())
		.userId(payerId)
		.entityId(payerId)
		.entityType(fromType)
		.description(description)
		.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode())
		.money(amount).build();
		toInserter.add(fromBuild);
		csqUserPaymentDao.multiInsert(toInserter);*/
	}

	private TCsqOrder checkAttach(String orderNo, String attach) {
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		Integer expectedAttach = tCsqOrder.getToType();
		if(!expectedAttach.toString().equals(attach)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "返回参数有误！");
		}
		return tCsqOrder;
	}

//	@Override
	private void afterPayService(Long userId, Integer fromType, Long fromId, Integer toType, Long serviceId, Double amount) {
		if(CsqEntityTypeEnum.TYPE_SERVICE.toCode() != toType) {
			return;
		}

		//向项目支付成功后逻辑(除余额变动、流水外)
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		Integer totalInCnt = csqService.getTotalInCnt();	//累积收入次数
		csqService.setTotalInCnt(++totalInCnt);

		Double expectedAmount = csqService.getExpectedAmount();
		Double remain = expectedAmount - amount;
		csqService.setSumTotalIn(csqService.getSumTotalIn() + amount);	//累积收到金额
		csqService.setExpectedRemainAmount(remain < 0 ? 0:remain);	//剩余期望金额
		csqServiceDao.update(csqService);

		//向缓存更新最新的捐助播报信息
		flushRedisDonate(userId, serviceId, amount);
	}

	private void flushRedisDonate(Long userId, Long serviceId, Double amount) {
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		String name = csqService.getName();
		//往"捐助播报"的缓存中添加记录,key: csqServiceId, value: TCsqService
		//构建一个DonateVo
		int maximum = 10;
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		CsqDonateRecordVo vo = CsqDonateRecordVo.builder().donateAmount(amount)
			.userHeadPortraitPath(csqUser.getUserHeadPortraitPath())
			.name(csqUser.getName())
			.toName(name)
			.createTime(System.currentTimeMillis()).build();

		Object exist = userRedisTemplate.get(CsqRedisEnum.CSQ_GLOBAL_DONATE_BROADCAST.getMsg(), serviceId.toString());

		LimitQueue<CsqDonateRecordVo> donateQueue;
		if(exist == null) {
			donateQueue = new LimitQueue<>(maximum);	//创建带上限的队列
		} else {
			donateQueue = (LimitQueue<CsqDonateRecordVo>) exist;
		}
		donateQueue.offer(vo);
		userRedisTemplate.put(CsqRedisEnum.CSQ_GLOBAL_DONATE_BROADCAST.getMsg(), serviceId.toString(), donateQueue);	//服务专项队列
		userRedisTemplate.put(CsqRedisEnum.CSQ_GLOBAL_DONATE_BROADCAST.getMsg(), CsqRedisEnum.ALL.getMsg(), donateQueue);	//全局队列
	}

//	@Override
	private void afterPayFund(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount) {
		if(CsqEntityTypeEnum.TYPE_FUND.toCode() != toType) {
			return;
		}
		//向基金捐款/充值之后
		//check
		TCsqFund csqFund = csqFundDao.selectByPrimaryKey(toId);
		Integer totalInCnt = csqFund.getTotalInCnt();//累积流入次数
		csqFund.setTotalInCnt(++totalInCnt);
		Double sumTotalIn = csqFund.getSumTotalIn();
		csqFund.setSumTotalIn(sumTotalIn + amount);
		csqFundDao.update(csqFund);

		//同步基金对应的项目
		csqServiceService.synchronizeService(csqFund);
	}

	private void recommendService(Long userId, Long fundId, TCsqFund csqFund) {
		if(csqFund == null) {
			csqFund = csqFundDao.selectByPrimaryKey(fundId);
			if(csqFund == null) {
				return;
			}
		}

		String trendPubKeys = csqFund.getTrendPubKeys();
		trendPubKeys = trendPubKeys == null? "": trendPubKeys;
		String[] trendKeyArray = trendPubKeys.split(",");
		TCsqService csqService = null;
		Long serviceId = null;
		for(String a:trendKeyArray) {
			List<TCsqService> tCsqServices = csqServiceDao.selectLikeByPubKeysAndUserIdNeq(a, userId);
			if(!tCsqServices.isEmpty()) {
				//获取0 ~ tcsqservices.size()-1的随机数
				Random random = new Random();
				int index = random.nextInt(tCsqServices.size() - 1);
				csqService = tCsqServices.get(index);
				serviceId = csqService.getId();
				break;
			}
		}

		if(serviceId == null) {
			return;
		}

		TCsqSysMsg build = TCsqSysMsg.builder()
			.userId(userId)
			.title(CsqSysMsgTemplateEnum.SERVICE_RECOMMEND.getTitle())
			.content(CsqSysMsgTemplateEnum.SERVICE_RECOMMEND.getContent())
			.type(CsqSysMsgEnum.TYPE_SREVICE.getCode())
			.serviceId(serviceId)
			.build();

		csqMsgDao.insert(build);
	}

}
