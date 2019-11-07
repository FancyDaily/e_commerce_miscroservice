package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-25 14:02
 */
@Component
public class SdxUserDaoImpl implements SdxUserDao {

	@Autowired
	private CsqUserDao csqUserDao;

	@Override
	public TCsqUser findById(Long userId) {
		/*return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES)
		);*/
		return csqUserDao.selectByPrimaryKey(userId);
	}

	@Override
	public int update(TCsqUser byId) {
		/*return MybatisPlus.getInstance().update(byId, new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getId, byId.getId())
		);*/
		return csqUserDao.update(Arrays.asList(byId));
	}

	@Override
	public List<TCsqUser> selectInIds(List<Long> userIds) {
		return csqUserDao.selectInIds(userIds);
	}

	@Override
	public Map<Long, List<TCsqUser>> groupingByIdInIds(List<Long> userIds) {
		if(userIds == null || userIds.isEmpty()) return new HashMap<>();
		List<TCsqUser> tCsqUsers = selectInIds(userIds);
		return tCsqUsers.stream()
			.collect(Collectors.groupingBy(TCsqUser::getId));
	}

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
	public TCsqUser selectByUserTelAndPasswordAndAccountType(String telephone, String password, Integer accountType) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getUserTel, telephone)
			.eq(TCsqUser::getPassword, password)
			.eq(TCsqUser::getAccountType, accountType)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUser> selectAll() {
		return MybatisPlus.getInstance().findAll(new TCsqUser(), baseBuild());
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
		return baseBuild()
			.eq(TCsqUser::getName, name)
			.isNotNull(TCsqUser::getUserTel);
	}

	@Override
	public MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public TCsqUser selectByNameAndNotNullUserTelAndNullOpenid(String name) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), byNameAndNotNullUserTelBuild(name)
			.isNull(TCsqUser::getVxOpenId));
	}

	@Override
	public List<TCsqUser> selectByName(String name) {
		return MybatisPlus.getInstance().findAll(new TCsqUser(), baseBuild()
			.eq(TCsqUser::getName, name));
	}

	@Override
	public List<TCsqUser> selectByBuild(MybatisPlusBuild baseBuild) {
		return MybatisPlus.getInstance().findAll(new TCsqUser(), baseBuild);
	}

	@Override
	public List<TCsqUser> selectByBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize) {
		IdUtil.setTotal(baseBuild);
		return MybatisPlus.getInstance().findAll(new TCsqUser(), baseBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqUser> selectByName(String searchParam, boolean isLike) {
		MybatisPlusBuild mybatisPlusBuild =
			isLike? baseBuild().like(TCsqUser::getName, "%" + searchParam + "%") : baseBuild().eq(TCsqUser::getName, searchParam);

		return MybatisPlus.getInstance().findAll(new TCsqUser(), mybatisPlusBuild);
	}

	@Override
	public List<TCsqUser> selectInNames(List<String> userNames) {
		return MybatisPlus.getInstance().findAll(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.in(TCsqUser::getName, userNames)
		);
	}

}
