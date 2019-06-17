package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqService;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 13:46
 */
public interface CsqServiceDao {

	List<TCsqService> selectInIds(List<Long> csqServiceIds);
}
