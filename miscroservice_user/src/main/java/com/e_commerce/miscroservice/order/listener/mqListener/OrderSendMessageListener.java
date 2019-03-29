package com.e_commerce.miscroservice.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 马晓晨
 * @date 2019/3/27
 */
//@Component
public class OrderSendMessageListener extends MqListenerConvert {
	Log logger = Log.getInstance(OrderSendMessageListener.class);

	@Autowired
	private OrderRelationService relationService;

	@Override
	protected void transferTo(String transferData) {
		logger.info("OrderSendMessageListener MQ监听器开始执行 >>>>>>");
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		// 获取参数
		Long orderId = paramMap.getLong("orderId");
		// 类型 1、提前俩小时发送消息  2、提前 一小时发送消息  3、在开始时间发送消息
		Integer type = paramMap.getInteger("type");
		if (Objects.equals(type, 1)) {
			// 提前俩小时发送将要开始的消息
			relationService.noChooseByTwoHour(orderId);
		} else if (Objects.equals(type, 2)) {
			// 订单开始一小时有人成单 提醒用户的消息
			relationService.oneHourByStart(orderId);
		} else if (Objects.equals(type, 3)) {
			// 到开始时间无人报名提醒发布者  到开始时间未签到提醒服务者
			relationService.noSignWhenStart(orderId);
			relationService.noUserEnrollByStart(orderId);
		}
	}
}
