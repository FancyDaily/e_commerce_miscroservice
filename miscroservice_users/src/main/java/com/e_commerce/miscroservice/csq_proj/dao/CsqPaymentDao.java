package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;

import java.util.List;

public interface CsqPaymentDao {
	/**
	 * 查询流水
	 * @param userId
	 * @return
	 */
	List<TCsqUserPaymentRecord> findWaters(Long userId);
}
