package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqPaymenEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat.WeChatPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:41
 */
@Transactional(rollbackFor = Throwable.class)
@Service
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
	//				.serviceName()
					.build();
			}
		}
		//针对不同的实体类型，有不同的支付前逻辑(eg. 产生待激活的基金等)

		String attach = entityType.toString();	//支付目标的类型
		//向微信请求发起支付
		Map<String, String> webParam = wechatPay.createWebParam(orderNo, fee, httpServletRequest, attach);
		return webParam;
	}

	@Override
	public void wxNotify(HttpServletRequest request) throws Exception {
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
			Integer entityType = Integer.valueOf(attach);

			//支付成功业务流程
			// 应当在生成订单时产生一个(两个)自定义参数以标识是哪种业务类型
			dealWithOrderNo(out_trade_no, attach);
		}

	}

	private void dealWithOrderNo(String orderNo, String attach) {
		//对比附加字段是否为该订单的类型
		TCsqOrder tCsqOrder = checkAttach(orderNo, attach);
		//根据attch的类型, 进行不同的支付后逻辑
		afterPaySuccess(attach, tCsqOrder);
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
				dealwithAccountAfterPay(tCsqOrder);
				break;
		}
		tCsqOrder.setStatus(CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		orderDao.update(tCsqOrder);
	}

	private void dealwithAccountAfterPay(TCsqOrder tCsqOrder) {
	}

	private void dealWithServiceAfterPay(TCsqOrder tCsqOrder) {
		//充值
		Long serviceId = tCsqOrder.getToId();
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
//		csqService
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
			//TODO 向payer或者基金拥有者发送 sysMsg
		}
		Double amount = tCsqOrder.getPrice();
		//资金流入 -> 余额增加
		Double formerBalance = fund.getBalance();
		fund.setBalance(formerBalance + amount);
		fundDao.update(fund);
		Long payerId = tCsqOrder.getUserId();
		Long ownerId = fund.getUserId();
		boolean payForSomeOne = !payerId.equals(ownerId);	//为他人支付
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

	public void wxNotify() {

	}

	private void afterApplyFundSuccess() {

	}
}
