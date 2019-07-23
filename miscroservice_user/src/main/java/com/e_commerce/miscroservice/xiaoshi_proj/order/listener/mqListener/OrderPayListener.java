package com.e_commerce.miscroservice.xiaoshi_proj.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.xiaoshi_proj.order.service.OrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author 马晓晨
 * @date 2019/3/27
 */
@Component
public class OrderPayListener extends MqListenerConvert {

	Log logger = Log.getInstance(OrderPayListener.class);
	@Autowired
	private OrderRelationService relationService;

	@Override
	protected void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		logger.info("OrderPayListener MQ监听器开始执行 >>>>>>");
		// 获取参数
		List<Long> userIds = paramMap.getJSONArray("userIds").toJavaList(Long.class);
		List<Long> paymentList = paramMap.getJSONArray("paymentList").toJavaList(Long.class);
		Long orderId = paramMap.getLong("orderId");
		List<Long> payUserIds = paramMap.getJSONArray("payUserIds").toJavaList(Long.class);
		//支付
		for (Long payUserId : payUserIds) {
			try {
				relationService.payOrder(orderId, userIds, paymentList,payUserId, 2);
			} catch (MessageException e) {
				logger.info(e.getMessage());
			}
		}
	}
}
