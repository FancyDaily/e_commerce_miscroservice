package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
public interface LpglCertService {

	QueryResult underCertList(Integer type, Integer status, Integer pageNum, Integer pageSize, boolean isToday, Long groupId);

	void cert(Long userId, Long certId, Integer status);

	void commitCert(Long userId, Long houseId, Integer type, Double disCountPrice, String description);

	void dealWithMessage(Integer type, TLpglCert build);

	void handOverMessage(Long certId, String... openIds);

	TLpglCert detail(Long certId);
}
