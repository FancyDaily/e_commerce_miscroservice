package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付Controller
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:40
 */
@RequestMapping("csq/pay")
@RestController
@Log
public class CsqPayController {

	/**
	 * 这是一个demo
	 * @return
	 */
	public Object demo() {
		return null;
	}
}
