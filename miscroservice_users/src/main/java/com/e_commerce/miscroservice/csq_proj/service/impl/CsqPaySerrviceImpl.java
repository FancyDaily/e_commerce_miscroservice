package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.enums.application.CSqUserPaymentEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat.WeChatPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
		TCsqOrder csqOrder = orderDao.selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(userId, userId, CSqUserPaymentEnum.TYPE_HUMAN.toCode(), fundId, CSqUserPaymentEnum.TYPE_FUND.toCode(), amount, CsqOrderEnum.STATUS_UNPAY.getCode());
		//TODO 对失活订单置成无效状态(不使用Mq时)
		if(csqOrder==null) {	//创建一个订单
			String orderNo = UUIdUtil.generateOrderNo();
			csqOrder = TCsqOrder.builder().status(CsqOrderEnum.STATUS_UNPAY.getCode())
				.userId(userId)
				.fromId(userId)
				.fromType(CSqUserPaymentEnum.TYPE_HUMAN.toCode())
				.toId(fundId)
				.toType(CSqUserPaymentEnum.TYPE_FUND.toCode())
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
		TCsqOrder csqOrder = orderDao.selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(userId, userId, CSqUserPaymentEnum.TYPE_HUMAN.toCode(), entityId, entityType, fee, CsqOrderEnum.STATUS_UNPAY.getCode());
		if(csqOrder==null) {
			boolean expired = System.currentTimeMillis() - csqOrder.getCreateTime().getTime() > 1000l * 60 * 30;
			if (expired) {	//不复用
				orderNo = "";	//生成
				csqOrder = TCsqOrder.builder().userId(userId)
					.orderNo(orderNo)
					.fromId(userId)
					.fromType(CSqUserPaymentEnum.TYPE_HUMAN.toCode())
					.toId(entityId)
					.toType(entityType)
					.price(fee)
					.status(CsqOrderEnum.STATUS_UNPAY.getCode())
	//				.serviceName()
					.build();
			}
		}
		//向微信请求发起支付
		Map<String, String> webParam = wechatPay.createWebParam(orderNo, fee, httpServletRequest);
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

			//支付成功业务流程
			//TODO 应当在生成订单时产生一个(两个)自定义参数以标识是哪种业务类型
			dealWithOrderNo(out_trade_no);
		}

	}

	private void dealWithOrderNo(String out_trade_no) {
	}

	public void wxNotify() {

	}

	private void afterApplyFundSuccess() {

	}
}
