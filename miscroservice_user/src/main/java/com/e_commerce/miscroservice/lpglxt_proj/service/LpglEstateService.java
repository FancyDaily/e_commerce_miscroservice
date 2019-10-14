package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.lpglxt_proj.vo.LpglHouseMapVo;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:39
 */
public interface LpglEstateService {

	QueryResult list(Integer pageNum, Integer pageSize);

	List<LpglHouseMapVo> houseMap(Long estateId, Integer buildingNum);
}
