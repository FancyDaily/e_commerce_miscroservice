package com.e_commerce.miscroservice.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.order.service.OrderService;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 马晓晨
 * @date 2019/3/25
 */
@Component
public class OrderEndListener extends MqListenerConvert {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductCommonController productService;

	@Override
	protected void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		Long serviceId = Long.parseLong((String) paramMap.get("serviceId"));
		Long orderId = Long.parseLong((String) paramMap.get("orderId"));
		//获取商品信息
		TService service = productService.getProductById(serviceId);
		//下架订单
		orderService.lowerFrameOrder(orderId);
		// 从service中取消掉可报名日期（暂时不用，派生订单会重新计算可报名日期）
		//派生订单
		orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_AUTO.getValue(), null);
	}
}
