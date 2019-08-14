package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TOldUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:25
 */
public interface CsqOldUserDao {

	int update(ArrayList<TOldUser> toUpdaters);

	List<TOldUser> selectAll();

	TOldUser selectByPrimaryKey(Long valueOf);
}
