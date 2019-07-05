package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqKeyValue;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 17:39
 */
public interface CsqKeyValueDao {

	List<TCsqKeyValue> selectByKeyAndType(Long userId, int code);

	List<TCsqKeyValue> selectByKeyAndTypeDesc(Long userId, int code);

	int save(TCsqKeyValue... build);
}
