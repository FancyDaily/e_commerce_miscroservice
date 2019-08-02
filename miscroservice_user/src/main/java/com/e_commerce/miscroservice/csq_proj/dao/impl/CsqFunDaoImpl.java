package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 16:50
 */
@Component
public class CsqFunDaoImpl implements CsqFundDao {

	@Override
	public int insert(TCsqFund... csqFund) {
		return MybatisPlus.getInstance().save(csqFund);
	}

	@Override
	public List<TCsqFund> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
		.eq(TCsqFund::getUserId, userId)
		.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqFund> selectByUserIdAndStatus(Long userId, int val) {
		return MybatisPlus.getInstance().findAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
		.eq(TCsqFund::getUserId, userId)
		.eq(TCsqFund::getStatus, val)
		.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqFund selectByPrimaryKey(Long fundId) {
		return MybatisPlus.getInstance().findOne(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
		.eq(TCsqFund::getId, fundId)
		.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int update(TCsqFund csqFund) {
		return MybatisPlus.getInstance().update(csqFund, new MybatisPlusBuild(TCsqFund.class)
		.eq(TCsqFund::getId, csqFund.getId()));
	}

	@Override
	public List<TCsqFund> selectByUserIdInStatusDesc(Long userId, Integer... option) {
		MybatisPlusBuild mybatisPlusBuild = new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqFund::getUserId, userId);
		mybatisPlusBuild = option.length >= 1? mybatisPlusBuild.in(TCsqFund::getStatus, option): mybatisPlusBuild;
		mybatisPlusBuild.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqFund::getCreateTime));
		return MybatisPlus.getInstance().findAll(new TCsqFund(), mybatisPlusBuild);
	}

	@Override
	public List<TCsqFund> selectInIds(List<Long> fundIds) {
		return MybatisPlus.getInstance().findAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqFund::getId, fundIds));
	}

	@Override
	public List<TCsqFund> selectByUserIdAndInStatus(Long userId, List<Integer> asList) {
		return MybatisPlus.getInstance().findAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getUserId, userId)
			.in(TCsqFund::getStatus, asList)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqFund> selectByUserIdAndNotEqStatus(Long userId, Integer status) {
		return MybatisPlus.getInstance().findAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getUserId, userId)
			.neq(TCsqFund::getStatus, status)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqFund selectByUserIdAndPrimaryKey(Long userId, Long fundId) {
		return MybatisPlus.getInstance().findOne(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getUserId, userId)
			.eq(TCsqFund::getId, fundId)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqFund> selectByUserIdInStatusDescPage(Long userId, Integer[] option, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqFund::getUserId, userId);
		mybatisPlusBuild = option.length >= 1? mybatisPlusBuild.in(TCsqFund::getStatus, option): mybatisPlusBuild;
		mybatisPlusBuild = mybatisPlusBuild.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqFund::getCreateTime));

		IdUtil.setTotal(mybatisPlusBuild);
		return MybatisPlus.getInstance().findAll(new TCsqFund(), mybatisPlusBuild.page(pageNum, pageSize));
	}

}
