package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxCommonsDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxFocusPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SdxCommonsDaoImpl implements SdxCommonsDao {

	//根据userid查询书友表信息；
	@Override
	public List<TSdxFocusPo> findTSdxFocusPoByUserId(long userId) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxFocusPo::getUserId, userId);
		return MybatisPlus.getInstance().findAll(TSdxFocusPo.builder().build(), build);
	}

	//查询关注我的书友列表（互相关注）
	@Override
	public List<TSdxFocusPo> findTSdxFocusPoByUserId2(long userId) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxFocusPo::getBookFriendId, userId);
		return MybatisPlus.getInstance().findAll(TSdxFocusPo.builder().build(), build);
	}

	//根据ID查询用户信息表；
	@Override
	public TCsqUser findTCsqUserByUserId(Long id) {
		return MybatisPlus.getInstance().findOne(new TCsqUser(), baseBuild()
			.eq(TCsqUser::getId, id)
		);
	}

	//根据用户ID查询好友动态表
	@Override
	public List<TSdxTrendsPo> findsdxCommonsDaoByUserId(Long id) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxTrendsPo.class);
		build.eq(TSdxTrendsPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxTrendsPo::getUserId, id);
		return MybatisPlus.getInstance().findAll(TSdxTrendsPo.builder().build(), build);
	}


	//根据动态ID查询下面的评论
	@Override
	public List<TSdxCommonsPo> findsdxCommonsDaoByTrendsId(Long id) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxCommonsPo.class);
		build.eq(TSdxCommonsPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxCommonsPo::getTrendsId, id);
		return MybatisPlus.getInstance().findAll(TSdxCommonsPo.builder().build(), build);
	}

	//根据用户ID查询动态表信息；
	@Override
	public List<TSdxTrendsPo> findTSdxTrendsPoByUserId(Long userId) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxTrendsPo.class);
		build.eq(TSdxTrendsPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxTrendsPo::getUserId, userId);
		return MybatisPlus.getInstance().findAll(TSdxTrendsPo.builder().build(), build);
	}

	//根据书籍信息编号查询动态表得到userID
	@Override
	public List<TSdxTrendsPo> findTSdxTrendsPoByBookInfoId(Long bookInfoId) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxTrendsPo.class);
		build.eq(TSdxTrendsPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxTrendsPo::getBookInfoId, bookInfoId);
		return MybatisPlus.getInstance().findAll(TSdxTrendsPo.builder().build(), build);
	}


	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxFocusPo.class)
			.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
	}
}
