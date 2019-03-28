package com.e_commerce.miscroservice.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.NoEnoughCreditException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.order.service.OrderService;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 马晓晨
 * @date 2019/3/25
 */
//@Component
public class OrderEndListener extends MqListenerConvert {

	Log logger = Log.getInstance(OrderEndListener.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductCommonController productService;

	@RequestMapping("/testEnd")
	public String test(String transferData) {
		transferTo(transferData);
		return "ok";
	}

	@Override
	public void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		Long serviceId = Long.parseLong((String) paramMap.get("serviceId"));
		Long orderId = Long.parseLong((String) paramMap.get("orderId"));
		//获取商品信息
		TService service = productService.getProductById(serviceId);
		//下架订单
		orderService.lowerFrameOrder(orderId);
		// 从service中取消掉可报名日期（暂时不用，派生订单会重新计算可报名日期）
		//派生订单
		try {
			orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_AUTO.getValue(), null);
		} catch (NoEnoughCreditException e) {
			logger.info("没有足够的授信，已做下架处理");
			productService.autoLowerFrameService(service, 2);
		}
	}
}
