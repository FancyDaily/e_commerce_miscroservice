package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;

import java.util.List;

public interface CsqPaymentService {
	/**
	 * 查询流水信息
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	QueryResult<TCsqUserPaymentRecord > findWaters(Integer pageNum, Integer pageSize, Long userId);
}
