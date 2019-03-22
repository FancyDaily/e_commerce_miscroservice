package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.exception.colligate.NoAuthChangeException;
import com.e_commerce.miscroservice.user.vo.BaseGroupView;

import java.util.List;

public interface GroupService {
	/**
	 * 分组的组织列表
	 * @param user 当前用户
	 * @return
	 */
	List<BaseGroupView> listGroup(TUser user);

	/**
	 * 新建分组
	 * @param group
	 * @param user
	 */
	void insert(TGroup group, TUser user);

	/**
	 * 更新分组
	 * @param group
	 * @param user 当前用户
	 */
	void updateGroup(TGroup group, TUser user) throws NoAuthChangeException;

	/**
	 * 删除分组
	 * @param groupId
	 * @param user
	 */
	void deleteGroup(Long groupId, TUser user) throws NoAuthChangeException;
}
