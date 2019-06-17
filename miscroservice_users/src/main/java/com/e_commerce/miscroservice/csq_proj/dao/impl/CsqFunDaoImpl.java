package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 16:50
 */
@Component
public class CsqFunDaoImpl implements CsqFundDao {

	private MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqFund.class)
			.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public int insert(TCsqFund... csqFund) {
		return MybatisPlus.getInstance().save(csqFund);
	}

	@Override
	public List<TCsqFund> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().finAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
		.eq(TCsqFund::getUserId, userId)
		.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqFund> selectByUserIdAndStatus(Long userId, int val) {
		return MybatisPlus.getInstance().finAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
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
		return MybatisPlus.getInstance().finAll(new TCsqFund(), baseWhereBuild()
			.eq(TCsqFund::getUserId, userId)
			.in(TCsqFund::getStatus, option)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqFund::getCreateTime)));
	}

}
