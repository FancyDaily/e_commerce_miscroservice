package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;

import java.util.Map;

public interface CsqPaymentService {
	/**
	 * 查询流水信息
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	QueryResult<TCsqUserPaymentRecord > findWaters(Integer pageNum, Integer pageSize, Long userId);

	/**
	 * 查询我的证书
	 * @param recordId
	 * @param userId
	 * @return
	 */
	Map<String,Object> findMyCertificate(Long recordId, Long userId);

	/**
	 * 收入支出 统计
	 * @param userId
	 * @param inOut
	 * @return
	 */
	Double countMoney(Long userId, Integer inOut);

	void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId);
}
