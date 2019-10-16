package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglCertDao {

	List<TLpglCert> selectByTypeAndStatusPage(Integer type, Integer status, Integer pageNum, Integer pageSize);

	TLpglCert selectByPrimaryKey(Long certId);

	int update(TLpglCert tLpglCert);

	int insert(TLpglCert build);
}
