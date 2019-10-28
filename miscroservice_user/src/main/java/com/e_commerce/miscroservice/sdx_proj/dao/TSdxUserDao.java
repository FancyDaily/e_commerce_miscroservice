package com.e_commerce.miscroservice.sdx_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;

import java.util.List;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-25 14:02
 */
public interface TSdxUserDao {

	TCsqUser findById(Long userId);

	int update(TCsqUser byId);

	List<TCsqUser> selectInIds(List<Long> userIds);

	Map<Long, List<TCsqUser>> groupingByIdInIds(List<Long> userIds);
}
