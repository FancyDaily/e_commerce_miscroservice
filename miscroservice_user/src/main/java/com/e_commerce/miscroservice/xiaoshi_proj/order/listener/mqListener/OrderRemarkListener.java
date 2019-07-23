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
public class OrderRemarkListener extends MqListenerConvert {

	Log logger = Log.getInstance(OrderRemarkListener.class);

	@Autowired
	private OrderRelationService relationService;

	@Override
	protected void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		logger.info("OrderRemarkListener MQ监听器开始执行了 >>>>>>");
		// 获取参数
		List<Long> userIds = paramMap.getJSONArray("userIds").toJavaList(Long.class);
		Long appraiserId = paramMap.getLong("appraiserId");
		Long orderId = paramMap.getLong("orderId");
		// 评价
		try {
			relationService.remarkOrder(appraiserId, userIds, orderId, 5, 5, 5, "默认评价", "默认评价");
		} catch (MessageException e) {
			logger.info(e.getMessage());
		}
	}
}
