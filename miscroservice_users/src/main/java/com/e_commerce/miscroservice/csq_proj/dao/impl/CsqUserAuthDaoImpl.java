package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserAuthDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 17:34
 */
@Component
public class CsqUserAuthDaoImpl implements CsqUserAuthDao {

	private MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public int insert(TCsqUserAuth... csqUserAuth) {
		return MybatisPlus.getInstance().save(csqUserAuth);
	}

	@Override
	public TCsqUserAuth selectByUserId(Long corpUserId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserAuth(), baseWhereBuild()
			.eq(TCsqUser::getId, corpUserId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserAuth::getCreateTime)));
	}

	@Override
	public TCsqUserAuth selectByPrimaryKey(Long userAuthId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserAuth(), baseWhereBuild()
			.eq(TCsqUserAuth::getId, userAuthId));
	}

	@Override
	public int update(TCsqUserAuth userAuth) {
		return MybatisPlus.getInstance().update(userAuth, baseWhereBuild()
		.eq(TCsqUser::getId, userAuth.getId()));
	}
}
