package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 17:33
 */
public interface CsqUserAuthDao {

	int insert(TCsqUserAuth... csqUserAuth);

	TCsqUserAuth selectByUserId(Long corpUserId);

	TCsqUserAuth selectByPrimaryKey(Long userAuthId);

	int update(TCsqUserAuth userAuth);

	List<TCsqUserAuth> selectByUserIdAndStatus(Long userId, int code);

	TCsqUserAuth selectByUserIdAndType(Long userId, int type);

	TCsqUserAuth selectByUserIdAndTypeAndStatus(Long userId, int code, int code1);

	TCsqUserAuth findByCardId(String cardId);

	int insertOrUpdate(TCsqUserAuth userAuth);

	MybatisPlusBuild baseBuild();

	List<TCsqUserAuth> selectWithBuildPage(MybatisPlusBuild mybatisPlusBuild, Integer pageNum, Integer pageSize);
}
