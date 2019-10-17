package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;
import com.e_commerce.miscroservice.user.po.TUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglUserDaoImpl implements LpglUserDao {

	@Override
	public TLpglUser selectByPrimaryKey(Long id) {
		return MybatisPlus.getInstance().findOne(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getId, id)
			.eq(TLpglUser::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int updateByPrimaryKey(TLpglUser tCsqUser) {
		return MybatisPlus.getInstance().update(tCsqUser, new MybatisPlusBuild(tCsqUser.getClass())
		.eq(TLpglUser::getId, tCsqUser.getId()));
	}

	@Override
	public int insert(TLpglUser... user) {
		return MybatisPlus.getInstance().save(user);
	}

	@Override
	public int insert(List<TLpglUser> user) {
		return MybatisPlus.getInstance().save(user);
	}

	@Override
	public List<TLpglUser> selectByVxOpenId(String openId) {
		return MybatisPlus.getInstance().findAll(new TLpglUser(),new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getVxOpenId,openId)
			.eq(TLpglUser::getIsValid,AppConstant.IS_VALID_YES));
	}

	@Override
	public TLpglUser selectByUserTel(String telephone) {
		return MybatisPlus.getInstance().findOne(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getUserTel, telephone)
			.eq(TLpglUser::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TLpglUser> selectInIds(List<Long> userIds) {
		return MybatisPlus.getInstance().findAll(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class)
			.in(TUser::getId, userIds)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TLpglUser> selectAll() {
		return MybatisPlus.getInstance().findAll(new TLpglUser(), baseBuild());
	}

	@Override
	public int update(List<TLpglUser> asList) {
		List<Long> ids = asList.stream()
			.map(TLpglUser::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(asList, new MybatisPlusBuild(TLpglUser.class)
			.in(TLpglUser::getId, ids));
	}

	@Override
	public MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TLpglUser> selectByName(String name) {
		return MybatisPlus.getInstance().findAll(new TLpglUser(), baseBuild()
			.eq(TLpglUser::getName, name));
	}

	@Override
	public List<TLpglUser> selectByBuild(MybatisPlusBuild baseBuild) {
		return MybatisPlus.getInstance().findAll(new TLpglUser(), baseBuild);
	}

	@Override
	public List<TLpglUser> selectByBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize) {
		IdUtil.setTotal(baseBuild);
		return MybatisPlus.getInstance().findAll(new TLpglUser(), baseBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TLpglUser> selectByName(String searchParam, boolean isLike) {
		MybatisPlusBuild mybatisPlusBuild =
			isLike? baseBuild().like(TLpglUser::getName, "%" + searchParam + "%") : baseBuild().eq(TLpglUser::getName, searchParam);

		return MybatisPlus.getInstance().findAll(new TLpglUser(), mybatisPlusBuild);
	}

	@Override
	public List<TLpglUser> selectInNames(List<String> userNames) {
		return MybatisPlus.getInstance().findAll(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class)
			.in(TLpglUser::getName, userNames)
		);
	}

	@Override
	public List<TLpglUser> selectByGroupId(Long groupId) {
		return MybatisPlus.getInstance().findAll(new TLpglUser(), baseBuild()
			.eq(TLpglUser::getGroupId, groupId)
		);
	}

}
