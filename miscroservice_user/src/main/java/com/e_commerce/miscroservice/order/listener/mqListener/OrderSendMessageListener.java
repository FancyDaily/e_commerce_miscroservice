package com.e_commerce.miscroservice.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * @author 马晓晨
 * @date 2019/3/27
 */
//@Component
public class OrderSendMessageListener extends MqListenerConvert {
	Log logger = Log.getInstance(OrderEndListener.class);

	@Autowired
	private OrderRelationService relationService;

	@RequestMapping("/testSendMessage")
	public String test(String transferData) {
		transferTo(transferData);
		return "ok";
	}
	@Override
	protected void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		// 获取参数
		Long orderId = Long.parseLong((String)paramMap.get("orderId"));
		// 类型 1、提前俩小时发送消息  2、提前 一小时发送消息  3、在开始时间发送消息
		Integer type = Integer.parseInt((String)paramMap.get("type"));
		if (Objects.equals(type, 1)) {
			// 提前俩小时发送消息
		} else if (Objects.equals(type, 2)) {
			// //提前一小时发送消息
		} else if (Objects.equals(type, 3)) {
			// 在开始时间发送消息
		}
	}
}
