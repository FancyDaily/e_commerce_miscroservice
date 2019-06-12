package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
public interface CsqUserDao {

	TCsqUser selectByPrimaryKey(Long userId);

	List<TCsqUser> queryByIds(List<Long> idList);

	int updateByPrimaryKey(TCsqUser tCsqUser);

	int insert(TCsqUser user);

	List<TCsqUser> selectByVxOpenId(String openId);

}
