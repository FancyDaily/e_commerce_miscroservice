package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:41
 */
public interface CsqPayService {
	Object preFundOrder(Long userId, Double amount);

	Map<String, String> preOrder(Long userId, String orderNo, Long entityId, Integer entityType, Double fee, HttpServletRequest httpServletRequest) throws Exception;

	Map<String, String> preOrder(Long userId, String orderNo, Long entityId, Integer entityType, Double fee, HttpServletRequest httpServletRequest, TCsqFund csqfund) throws Exception;

	void wxNotify(HttpServletRequest request, boolean b) throws Exception;

	void preRefund(Long userId, String orderNo, HttpServletRequest request) throws Exception;

	void dealWithRelatedStatistics(TCsqOrder tCsqOrder);

	void afterPayService(Long userId, Integer fromType, Long fromId, Integer toType, Long serviceId, Double amount);

	void afterPayFund(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount);

	/**
	 * 平台内捐助
	 * @param userId
	 * @param fromType
	 * @param fromId
	 * @param toType
	 * @param toId
	 * @param amount
	 */
	void withinPlatFormPay(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount);
}
