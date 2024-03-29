package com.e_commerce.miscroservice.xiaoshi_proj.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.xiaoshi_proj.order.service.OrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 马晓晨
 * @date 2019/3/29
 */
@Component
public class OrderRemovePunishmentListener extends MqListenerConvert {

	Log logger = Log.getInstance(OrderRemovePunishmentListener.class);

	@Autowired
	private OrderRelationService relationService;

	@Override
	protected void transferTo(String transferData) {
		logger.info("OrderRemovePunishmentListener MQ监听器开始执行 >>>>>>");
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		// 获取参数
		Long userTimeRecordId = paramMap.getLong("userTimeRecordId");
		Long eventId = paramMap.getLong("eventId");
		try {
			relationService.unAcceptGiftForRemove(userTimeRecordId, eventId);
		} catch (MessageException e) {
			logger.info(e.getMessage());
		}
	}
}
