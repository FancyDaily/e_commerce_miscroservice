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

	/**
	 * 根据id查询流水
	 * @param recordId
	 * @return
	 */
	TCsqUserPaymentRecord findWaterById(Long recordId);



	/**
	 * 收入支出统计
	 * @param userId
	 * @param inOut
	 * @return
	 */
	Double countMoney(Long userId, Integer inOut);

	/**
	 * 插入
	 * @param build
	 */
	int insert(TCsqUserPaymentRecord... build);
}
