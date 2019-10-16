package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglHouseDao {

	List<TLpglHouse> selectByEstateId(Long estateId);

	List<TLpglHouse> selectByEstateIdPage(Long estateId, Integer pageNum, Integer pageSize);

	List<TLpglHouse> selectAll();

	List<TLpglHouse> selectByEstateIdAndBuildingNum(Long estateId, Integer buildingNum);

	TLpglHouse selectByPrimaryKey(Long houseId);

	int update(TLpglHouse obj);

	int insert(TLpglHouse obj);

	int remove(Long houseId);

	List<TLpglHouse> selectInIds(List<Long> houseIds);

	int insert(List<TLpglHouse> addList);

	int insert(List<TLpglHouse> addList, boolean isMultiple);

	int modify(List<TLpglHouse> modifyList);

	int modify(List<TLpglHouse> modifyList, boolean isMultiple);

	List<TLpglHouse> selectWithListWhereEstateIdAndBuildingNumAndHouseNumCondition(List<TLpglHouse> todoList);
}
