package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 16:50
 */
public interface FundDao {
	int insert(TCsqFund... csqFund);

	List<TCsqFund> selectByUserId(Long userId);
}
