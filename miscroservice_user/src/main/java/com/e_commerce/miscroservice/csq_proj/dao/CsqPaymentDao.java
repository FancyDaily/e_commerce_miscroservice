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

	/**
	 * 获取用户所有收支
	 * @param userId
	 * @return
	 */
	List<TCsqUserPaymentRecord> selectByUserId(Long userId);
	List<TCsqUserPaymentRecord> selectByUserIdDesc(Long userId);

	/**
	 * 获取收入或支出
	 * @param userId
	 * @param option
	 * @return
	 */
	List<TCsqUserPaymentRecord> selectByUserIdAndInOrOut(Long userId, Integer option);
	List<TCsqUserPaymentRecord> selectByUserIdAndInOrOutDesc(Long userId, Integer option);

	int multiInsert(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords);

	TCsqUserPaymentRecord selectByOrderIdAndNeqId(Long orderId, Long paymentId);
}
