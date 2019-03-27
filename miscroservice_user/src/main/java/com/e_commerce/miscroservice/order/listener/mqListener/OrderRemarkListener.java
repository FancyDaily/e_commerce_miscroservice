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
@Component
public class OrderRemarkListener extends MqListenerConvert {

	Log logger = Log.getInstance(OrderEndListener.class);

	@Autowired
	private OrderRelationService relationService;

	@RequestMapping("/testRemark")
	public String test(String transferData) {
		transferTo(transferData);
		return "ok";
	}

	@Override
	protected void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		// 获取参数
		List<Long> userIds = (List) paramMap.get("userIds");
		Long appraiserId = (Long) paramMap.get("appraiserId");
		Long orderId = (Long) paramMap.get("orderId");
		// 评价
		try {
			relationService.remarkOrder(appraiserId, userIds, orderId, 5, 5, 5, "默认评价", "默认评价");
		} catch (Exception e) {
			logger.error("用户ID {} 评价失败，订单ID 为 {}", appraiserId, orderId);
		}
	}
}
