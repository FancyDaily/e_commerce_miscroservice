package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.user.po.TUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class CsqUserDaoImpl implements CsqUserDao {

	@Override
	public TCsqUser selectByPrimaryKey(Long id) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getId, id)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUser> queryByIds(List<Long> idList) {
		return MybatisPlus.getInstance().findAll(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.in(TCsqUser::getId, idList)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int updateByPrimaryKey(TCsqUser tCsqUser) {
		return MybatisPlus.getInstance().update(tCsqUser, new MybatisPlusBuild(tCsqUser.getClass())
		.eq(TCsqUser::getId, tCsqUser.getId()));
	}

	@Override
	public int insert(TCsqUser... user) {
		return MybatisPlus.getInstance().save(user);
	}

	@Override
	public int insert(List<TCsqUser> user) {
		return MybatisPlus.getInstance().save(user);
	}

	@Override
	public List<TCsqUser> selectByVxOpenId(String openId) {
		return MybatisPlus.getInstance().findAll(new TCsqUser(),new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getVxOpenId,openId)
			.eq(TCsqUser::getIsValid,AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqUser selectByVxOpenIdAndAccountType(String openid, Integer accountType) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getVxOpenId, openid)
			.eq(TCsqUser::getAccountType, accountType)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqUser selectByUserTel(String telephone) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getUserTel, telephone)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqUser selectByUserTelAndAccountType(String telephone, Integer accountType) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getUserTel, telephone)
			.eq(TCsqUser::getAccountType, accountType)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUser> selectInIds(List<Long> userIds) {
		return MybatisPlus.getInstance().findAll(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.in(TUser::getId, userIds)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqUser selectByUserTelAndPasswordAndAccountType(String telephone, String password, Integer accountType) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getUserTel, telephone)
			.eq(TCsqUser::getPassword, password)
			.eq(TCsqUser::getAccountType, accountType)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUser> selectAll() {
		return MybatisPlus.getInstance().findAll(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqUser selectByOldId(String oldIdStr) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getOldId, oldIdStr));
	}

	@Override
	public int update(List<TCsqUser> asList) {
		List<Long> ids = asList.stream()
			.map(TCsqUser::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(asList, new MybatisPlusBuild(TCsqUser.class)
			.in(TCsqUser::getId, ids));
	}

	@Override
	public TCsqUser selectByNameAndNotNullUserTel(String name) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), byNameAndNotNullUserTelBuild(name)
		);
	}

	private MybatisPlusBuild byNameAndNotNullUserTelBuild(String name) {
		return new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUser::getName, name)
			.isNotNull(TCsqUser::getUserTel);
	}

	@Override
	public TCsqUser selectByNameAndNotNullUserTelAndNullOpenid(String name) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), byNameAndNotNullUserTelBuild(name)
			.isNull(TCsqUser::getVxOpenId));
	}

}
