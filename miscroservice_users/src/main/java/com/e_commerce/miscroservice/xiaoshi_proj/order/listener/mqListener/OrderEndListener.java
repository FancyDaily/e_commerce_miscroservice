package com.e_commerce.miscroservice.xiaoshi_proj.order.listener.mqListener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.NoEnoughCreditException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.xiaoshi_proj.order.service.OrderService;
import com.e_commerce.miscroservice.xiaoshi_proj.product.controller.ProductCommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 马晓晨
 * @date 2019/3/25
 */
@Component
public class OrderEndListener extends MqListenerConvert {

	Log logger = Log.getInstance(OrderEndListener.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductCommonController productService;

	@Override
	public void transferTo(String transferData) {
		JSONObject paramMap = JSONObject.parseObject(transferData);
		System.out.println(paramMap);
		logger.info("OrderEndListener 监听器开始执行 >>>>>>");
		Long serviceId = paramMap.getLong("serviceId");
		Long orderId = paramMap.getLong("orderId");
		//获取商品信息
		TService service = productService.getProductById(serviceId);
		//下架订单
		orderService.lowerFrameOrder(orderId);
		// 如果是单次互助，直接下架掉
		if (Objects.equals(service.getTimeType(), ProductEnum.TIME_TYPE_FIXED.getValue())) {
			productService.autoLowerFrameService(service, 1);
		} else {
			//派生订单
			try {
				orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_AUTO.getValue(), null);
			} catch (NoEnoughCreditException e) {
				logger.info("没有足够的授信，已做下架处理");
				productService.autoLowerFrameService(service, 2);
			}
		}
	}
}
