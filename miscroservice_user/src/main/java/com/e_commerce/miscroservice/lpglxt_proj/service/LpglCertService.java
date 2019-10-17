package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
public interface LpglCertService {

	QueryResult underCertList(Integer type, Integer status, Integer pageNum, Integer pageSize);

	void cert(Long userId, Long certId, Integer status);

	void commitCert(Long houseId, Integer type, Double disCountPrice, String description);

}
