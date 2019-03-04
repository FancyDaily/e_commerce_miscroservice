package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 求助服务
 * 求助服务controller层
 */
@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 测试
	 *
	 * @return
	 */
	@RequestMapping("/hello")
	public String hello() {
		return "hello";
	}

}
