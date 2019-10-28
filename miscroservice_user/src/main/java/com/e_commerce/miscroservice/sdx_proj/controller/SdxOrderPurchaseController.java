package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 书袋熊购书订单
 * @Author: FangyiXu
 * @Date: 2019-10-25 16:36
 */
@RestController
@Log
@RequestMapping("sdx/order/purchase")
public class SdxOrderPurchaseController {

	@Autowired
	TSdxBookInfoService sdxBookInfoService;

	/**
	 * 平台书籍去向列表
	 * @return
	 */
	@RequestMapping("donate/goto/list")
	public Object donateGotoList() {
		return null;
	}

}
