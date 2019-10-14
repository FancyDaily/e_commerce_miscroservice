package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
public interface LpglHouseService {

	QueryResult list(Long estateId, Integer pageNum, Integer pageSize);

	int save(TLpglHouse tLpglHouse);

}
