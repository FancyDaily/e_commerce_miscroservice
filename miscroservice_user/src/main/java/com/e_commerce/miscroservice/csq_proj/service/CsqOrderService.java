package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 11:19
 */
public interface CsqOrderService {
	void checkPaid(String orderNo);

	void checkPaid(TCsqOrder csqOrder);

	String generateOrderNo();
}
