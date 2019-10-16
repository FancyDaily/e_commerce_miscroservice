package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
public interface LpglCustomerInfoService {


	QueryResult list(Integer status, Integer pageNum, Integer pageSize);

	void commit(Long id, Long houseId, String telephone, String description);

	void remove(Long id);
}
