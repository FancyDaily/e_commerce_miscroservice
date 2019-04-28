package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.application.TServiceSummary;

/**
 * @author 姜修弘
 * @date 2019/3/18
 */
public interface serviceSummaryDao {


	/**
	 * 插入精彩瞬间
	 * @param serviceSummary
	 * @return
	 */
	long saveServiceSummary(TServiceSummary serviceSummary);

	/**
	 * 根据serviceId 查找精彩瞬间
	 * @param serviceId
	 * @return
	 */
	TServiceSummary selectServiceSummaryByServiceId(Long serviceId);
}
