package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglRole;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglRoleDao {

	List<TLpglRole> selectInNames(List<String> roleNames);
}
