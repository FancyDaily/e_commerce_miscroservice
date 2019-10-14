package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglEstate;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglEstateDao {

	List<TLpglEstate> selectAll();

	List<TLpglEstate> selectAllPage(Integer pageNum, Integer pageSize);

}
