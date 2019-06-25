package com.e_commerce.miscroservice.csq_proj.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:41
 */
public interface CsqPayService {
	Object preFundOrder(Long userId, Double amount);

	Map<String, String> preOrder(Long userId, String orderNo, Long entityId, Integer entityType, Double fee, HttpServletRequest httpServletRequest) throws Exception;

	void wxNotify(HttpServletRequest request) throws Exception;
}
