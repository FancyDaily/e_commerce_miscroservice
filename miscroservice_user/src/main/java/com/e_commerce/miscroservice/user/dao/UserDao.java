package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUser;

import java.util.List;

public interface UserDao {

    TUser selectByPrimaryKey(Long id);

    List<TUser> queryByIds(List<Long> idList);

	int updateByPrimaryKey(TUser tUser);

}
