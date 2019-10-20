package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;

import java.util.List;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:39
 */
public interface LpglEstateService {

	QueryResult list(Integer pageNum, Integer pageSize);

	Map<String, Map<Integer, Map<Double, Map<Integer, List<TLpglHouse>>>>> houseMap(Long estateId, Integer buildingNum);

	void save(String name);

	void modify(Long estateId, String name, String isValid);

	/**
	 *
	 * @param estateId
	 * @param buildingNum
	 * @return
	 */
	AjaxResult houseTopic(Long estateId, Integer buildingNum);

	AjaxResult houseContent(Long estateId, String buildingNum);
}
