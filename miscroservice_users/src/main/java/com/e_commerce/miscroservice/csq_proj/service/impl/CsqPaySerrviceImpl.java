package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.AesUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat.WeChatPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:41
 */
@Transactional(rollbackFor = Throwable.class)
@Service
@Log
public class CsqPaySerrviceImpl implements CsqPayService {

	@Autowired
	private CsqOrderDao orderDao;

	@Autowired
	private CsqFundDao fundDao;

	@Autowired
	private WeChatPay wechatPay;

	@Autowired
	private CsqUserPaymentDao csqUserPaymentDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqUserDao csqUserDao;

	@Override
	public Object preFundOrder(Long userId, Double amount) {	//发起支付后的业务流程
		//TODO 用户性质的特异行为
		//用户发起支付后产生的业务行为
		//1.判断是否需要产生基金 -> 判断依据: （对一个用户只能创建一个基金这件事不在此做校验）
		long currentTimeMillis = System.currentTimeMillis();
		TCsqFund csqFund = null;
		List<TCsqFund> tCsqFunds = fundDao.selectByUserIdAndStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
		if(tCsqFunds.isEmpty()) {	//如果不存在
			csqFund = TCsqFund.builder().status(CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal()).build();
			fundDao.insert(csqFund);
		} else {
			csqFund = tCsqFunds.get(0);
		}
		//2.查看是否可以复用未支付的相同金额有效订单
		Long fundId = csqFund.getId();
		TCsqOrder csqOrder = orderDao.selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(userId, userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), amount, CsqOrderEnum.STATUS_UNPAY.getCode());
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
		return preOrder(userId, orderNo, entityId, entityType, fee, httpServletRequest, null);
	}

	@Override
	public Map<String, String> preOrder(Long userId, String orderNo, Long entityId, Integer entityType, Double fee, HttpServletRequest httpServletRequest, TCsqFund csqfund) throws Exception {
		//查看对于同一用户同一业务(entityId、entityType、fee相同)	是否有可复用的订单
		TCsqOrder csqOrder = orderDao.selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(userId, userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), entityId, entityType, fee, CsqOrderEnum.STATUS_UNPAY.getCode());
		if(csqOrder==null) {
			boolean expired = System.currentTimeMillis() - csqOrder.getCreateTime().getTime() > 1000l * 60 * 30;
			if (expired) {	//不复用
				orderNo = "";	//生成
				csqOrder = TCsqOrder.builder().userId(userId)
					.orderNo(orderNo)
					.fromId(userId)
					.fromType(CsqEntityTypeEnum.TYPE_HUMAN.toCode())
					.toId(entityId)
					.toType(entityType)
					.price(fee)
					.status(CsqOrderEnum.STATUS_UNPAY.getCode())
					.build();
				orderDao.insert(csqOrder);
			}
		}
		//针对不同的实体类型，有不同的支付前逻辑(eg. 产生待激活的基金等)
		dealwithFundBeforePay(csqOrder, csqfund);

		String attach = entityType.toString();	//支付目标的类型
		//向微信请求发起支付
		Map<String, String> webParam = wechatPay.createWebParam(orderNo, fee, httpServletRequest, attach);
		return webParam;
	}

	private void dealWithBeforePay(TCsqOrder order, TCsqFund csqFund) {
		dealwithFundBeforePay(order, csqFund);
	}

	private void dealwithFundBeforePay(TCsqOrder order, TCsqFund csqFund) {
		if(csqFund == null) {
			return;
		}
		Long userId = order.getUserId();

		//创建一个待激活的基金
		String name = csqFund.getName();
		String trendPubKeys = csqFund.getTrendPubKeys();
		TCsqFund build = TCsqFund.builder()
			.userId(userId)
			.status(CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal())
			.name(name)
			.trendPubKeys(trendPubKeys)
			.coverPic(CsqFundEnum.DEFAULT_COVER_PIC)    //默认封面
			.build();
		fundDao.insert(build);
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
		TCsqOrder tCsqOrder = orderDao.selectByOrderNo(orderNo);
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
		switch (CsqEntityTypeEnum.getType(entityType)) {
			case TYPE_FUND:
				TCsqFund csqFund = fundDao.selectByPrimaryKey(entityId);
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
		TCsqOrder tCsqOrder = orderDao.selectByOrderNo(orderNo);
		//判断是否已经完成业务逻辑
		if(CsqOrderEnum.STATUS_ALREADY_REFUND.getCode() == tCsqOrder.getStatus()) {
			return;
		}

		switch (CsqEntityTypeEnum.getType(tCsqOrder.getToType())) {
			case TYPE_ACCOUNT:
				dealWithAccountAfterRefund(orderNo);
				break;
			case TYPE_FUND:
				dealWithFundAfterRefund(orderNo);
				break;
		}

		tCsqOrder.setStatus(CsqOrderEnum.STATUS_ALREADY_REFUND.getCode());
		orderDao.update(tCsqOrder);
	}

	private void dealWithFundAfterRefund(String orderNo) {
		TCsqOrder tCsqOrder = orderDao.selectByOrderNo(orderNo);
		Long fundId = tCsqOrder.getToId();
		Double price = tCsqOrder.getPrice();
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		//发生退款 -> 余额减少
		Double formerBalance = csqFund.getBalance();
		csqFund.setBalance(formerBalance - price);
		fundDao.update(csqFund);
		//插入流水
		Long userId = tCsqOrder.getUserId();
		TCsqUserPaymentRecord build = TCsqUserPaymentRecord.builder()
			.userId(userId)    //支出人
			.orderId(tCsqOrder.getId())
			.money(price)
			.entityId(userId)
			.entityType(CsqEntityTypeEnum.TYPE_FUND.toCode())    //基金支出
			.inOrOut(CsqPaymenEnum.INOUT_OUT.toCode()).build();
		csqUserPaymentDao.insert(build);
		//TODO sysMSg

	}

	private void dealWithAccountAfterRefund(String orderNo) {
		//账户的退款，检查余额是否足够退款
		TCsqOrder tCsqOrder = orderDao.selectByOrderNo(orderNo);
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
			.inOrOut(CsqPaymenEnum.INOUT_OUT.toCode()).build();
		//TODO sysMSg

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
			dealWithOrderNoPay(out_trade_no, attach);
		}
	}

	private void dealWithOrderNoPay(String orderNo, String attach) {
		//防止重复调用
		if(checkStatus(orderNo)) {
			return;
		}
		//对比附加字段是否为该订单的类型
		TCsqOrder tCsqOrder = checkAttach(orderNo, attach);
		//根据attch的类型, 进行不同的支付后逻辑
		afterPaySuccess(attach, tCsqOrder);
	}

	private boolean checkStatus(String orderNo) {
		boolean isDone = false;
		TCsqOrder tCsqOrder = orderDao.selectByOrderNo(orderNo);
		if(CsqOrderEnum.STATUS_ALREADY_PAY.getCode() == tCsqOrder.getStatus()) {
			isDone = true;
		}
		return isDone;
	}

	private void afterPaySuccess(String attach, TCsqOrder tCsqOrder) {
		switch (CsqEntityTypeEnum.getType(Integer.valueOf(attach))) {
			case TYPE_FUND:
				dealWithFundAfterPay(tCsqOrder);
				break;
			case TYPE_SERVICE:
				dealWithServiceAfterPay(tCsqOrder);
				break;
			case TYPE_ACCOUNT:
				dealWithAccountAfterPay(tCsqOrder);
				break;
		}
		tCsqOrder.setStatus(CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		orderDao.update(tCsqOrder);
	}

	private void dealWithAccountAfterPay(TCsqOrder tCsqOrder) {
		Long ownerId = tCsqOrder.getToId();
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(ownerId);
		//爱心账户开户
		csqUser.setBalanceStatus(CsqUserEnum.BALANCE_STATUS_AVAILABLE.toCode());
		csqUserDao.updateByPrimaryKey(csqUser);
		//充值
		Double price = tCsqOrder.getPrice();
		Double surplusAmount = csqUser.getSurplusAmount();
		csqUser.setSurplusAmount(surplusAmount + price);
		csqUserDao.updateByPrimaryKey(csqUser);
		//TODO 一些次数统计，比如充值次数

		//流水
		Long payerId = tCsqOrder.getUserId();
		//处理流水
		dealWithUserPaymentRecord(tCsqOrder, payerId, ownerId);
	}

	private void dealWithServiceAfterPay(TCsqOrder tCsqOrder) {
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
		//处理流水
		dealWithUserPaymentRecord(tCsqOrder, payerId, ownerId);
	}

	private void dealWithFundAfterPay(TCsqOrder tCsqOrder) {
		//不再对入餐作判断
		//根据订单信息得到
		// -> 基金开户了结
		// -> 仅充值
		Long fundId = tCsqOrder.getToId();
		TCsqFund fund = fundDao.selectByPrimaryKey(fundId);
		Integer status = fund.getStatus();
		if(CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal() == status) {	//待激活
			//基金开户
			fund.setStatus(CsqFundEnum.STATUS_ACTIVATED.getVal());
			//创建双生项目
			TCsqService csqService = getFundTypeService(fundId, fund);
			csqServiceDao.insert(csqService);
			//TODO 向payer或者基金拥有者发送 sysMsg
		}
		Double amount = tCsqOrder.getPrice();
		//资金流入 -> 余额增加
		Double formerBalance = fund.getBalance();
		fund.setBalance(formerBalance + amount);
		fundDao.update(fund);
		Long payerId = tCsqOrder.getUserId();
		Long ownerId = fund.getUserId();
		//处理流水
		dealWithUserPaymentRecord(tCsqOrder, payerId, ownerId);
	}

	private TCsqService getFundTypeService(Long fundId, TCsqFund fund) {
		TCsqService csqService = fund.copyCsqService();
		csqService.setId(null);
		csqService.setFundId(fundId);
		csqService.setStatus(CsqServiceEnum.STATUS_INITIAL.getCode());
		csqService.setType(CsqServiceEnum.TYPE_FUND.getCode());
		csqService.setFundStatus(fund.getStatus());
		return csqService;
	}

	private void dealWithUserPaymentRecord(TCsqOrder tCsqOrder, Long payerId, Long ownerId) {
		boolean payForSomeOne = !payerId.equals(ownerId);	//为他人支付
		Double amount = tCsqOrder.getPrice();
		//插入流水
		List<TCsqUserPaymentRecord> toInserter = new ArrayList<>();
		TCsqUserPaymentRecord toBuild = TCsqUserPaymentRecord.builder()
			.orderId(tCsqOrder.getId())
			.userId(ownerId)
			.entityId(tCsqOrder.getToId())
			.entityType(tCsqOrder.getToType())
			.inOrOut(CsqPaymenEnum.INOUT_IN.toCode())
			.money(amount).build();
		toInserter.add(toBuild);
		if(payForSomeOne) {	//TODO 为他人支付(当为自己支付，或只有一条被充值的记录)
			TCsqUserPaymentRecord fromBuild = TCsqUserPaymentRecord.builder()
				.orderId(tCsqOrder.getId())
				.userId(payerId)
				.entityId(payerId)
				.entityType(tCsqOrder.getFromType())
				.inOrOut(CsqPaymenEnum.INOUT_OUT.toCode())
				.money(amount).build();
			toInserter.add(fromBuild);
		}
		csqUserPaymentDao.multiInsert(toInserter);
	}

	private TCsqOrder checkAttach(String orderNo, String attach) {
		TCsqOrder tCsqOrder = orderDao.selectByOrderNo(orderNo);
		Integer expectedAttach = tCsqOrder.getToType();
		if(!expectedAttach.equals(attach)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "返回参数有误！");
		}
		return tCsqOrder;
	}

}
