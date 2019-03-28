package com.e_commerce.miscroservice.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @author 马晓晨
 * @date 2019/3/27
 */
//@Component
public class OrderPayListener extends MqListenerConvert {

	Log logger = Log.getInstance(OrderEndListener.class);
	@Autowired
	private OrderRelationService relationService;

	@RequestMapping("/testPay")
	public String test(String transferData) {
		transferTo(transferData);
		return "ok";
	}

	@Override
	protected void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		// 获取参数
		List<Long> userIds = (List)paramMap.get("userIds");
		List<Long> paymentList = (List)paramMap.get("paymentList");
		Long orderId = (Long)paramMap.get("orderId");
		List<Long> payUserIds = (List)paramMap.get("payUserIds");
		//支付
		for (Long payUserId : payUserIds) {
			try {
				relationService.payOrder(orderId, userIds, paymentList,payUserId, 2);
			} catch (Exception e) {
				logger.error("id为{}的用户为id{}的用户在订单ID为{}的互助中支付失败", payUserId, userIds.get(0), orderId);
			}
		}
	}
}
