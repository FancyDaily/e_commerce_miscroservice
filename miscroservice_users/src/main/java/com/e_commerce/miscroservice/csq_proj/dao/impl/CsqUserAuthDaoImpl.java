package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserAuthDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 17:34
 */
@Component
public class CsqUserAuthDaoImpl implements CsqUserAuthDao {

	@Override
	public int insert(TCsqUserAuth... csqUserAuth) {
		return MybatisPlus.getInstance().save(csqUserAuth);
	}

	@Override
	public TCsqUserAuth selectByUserId(Long corpUserId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserAuth(), new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUser::getId, corpUserId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserAuth::getCreateTime)));
	}

	@Override
	public TCsqUserAuth selectByPrimaryKey(Long userAuthId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserAuth(), new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUserAuth::getId, userAuthId));
	}

	@Override
	public int update(TCsqUserAuth userAuth) {
		return MybatisPlus.getInstance().update(userAuth, new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
		.eq(TCsqUser::getId, userAuth.getId()));
	}

	@Override
	public List<TCsqUserAuth> selectByUserIdAndStatus(Long userId, int code) {
		return MybatisPlus.getInstance().findAll(new TCsqUserAuth(), new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqUserAuth::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUserAuth::getUserId, userId)
			.eq(TCsqUserAuth::getStatus, code))

			/*new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES).eq(TCsqUserAuth::getUserId, userId)
				.eq(TCsqUserAuth::getStatus, code))*/
		;

	}

	@Override
	public TCsqUserAuth selectByUserIdAndType(Long userId, int type) {
		return MybatisPlus.getInstance().findOne(new TCsqUserAuth(), new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqUserAuth::getUserId, userId)
			.eq(TCsqUserAuth::getType, type)
			.eq(TCsqUserAuth::getIsValid,AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqUserAuth selectByUserIdAndTypeAndStatus(Long userId, int type, int status) {
		return MybatisPlus.getInstance().findOne(new TCsqUserAuth(), new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqUserAuth::getUserId, userId)
			.eq(TCsqUserAuth::getType, type)
			.eq(TCsqUserAuth::getStatus, status)
			.eq(TCsqUserAuth::getIsValid,AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqUserAuth findByCardId(String cardId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserAuth(), new MybatisPlusBuild(TCsqUserAuth.class)
			.eq(TCsqUserAuth::getCardId, cardId)
			.eq(TCsqUserAuth::getIsValid, AppConstant.IS_VALID_YES));
	}
}
