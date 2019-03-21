package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TGroup;

import java.util.List;

public interface GroupDao {
    List<TGroup> selectByCompanyIdAndAuth(Long companyId, Integer groupAuthDefault);

	/**
	 * 查询分组列表
	 * @param companyId
	 * @return
	 */
	List<TGroup> listGroup(Long companyId);

	/**
	 * 插入一条分组
	 * @param group
	 */
	int insert(TGroup group);

	/**
	 * 根据主键查询济洛路
	 * @param id
	 * @return
	 */
	TGroup selectByPrimaryKey(Long id);

	/**
	 * 根据主键修改
	 * @param updateGroup
	 * @return
	 */
	int updateByPrimaryKeySelective(TGroup updateGroup);
}
