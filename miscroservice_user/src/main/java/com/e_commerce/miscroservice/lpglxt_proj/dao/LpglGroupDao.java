package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TlpglGroup;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglGroupDao {

	List<TlpglGroup> selectByEstateIdPage(Long estateId, Integer pageNum, Integer pageSize);

	int insert(TlpglGroup build);

	int update(TlpglGroup build);
}
