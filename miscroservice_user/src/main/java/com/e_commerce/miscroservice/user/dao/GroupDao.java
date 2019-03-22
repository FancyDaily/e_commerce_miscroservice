package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TGroup;

import java.util.List;

public interface GroupDao {

    /**
     * 根据组织id、权限查询
     * @param companyId
     * @param groupAuthDefault
     * @return
     */
    List<TGroup> selectByCompanyIdAndAuth(Long companyId, Integer groupAuthDefault);

    /**
     * 根据组织id查询
     * @param companyId
     * @return
     */
    List<TGroup> selectByCompanyId(Long companyId);

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
	 * 根据主键查询
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
