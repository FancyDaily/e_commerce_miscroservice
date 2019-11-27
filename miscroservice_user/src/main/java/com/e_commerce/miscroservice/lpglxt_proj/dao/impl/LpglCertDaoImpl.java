package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCertDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglCertDaoImpl implements LpglCertDao {

	@Override
	public List<TLpglCert> selectByTypeAndStatusPage(Integer type, Integer status, Integer pageNum, Integer pageSize, boolean isToday) {
		MybatisPlusBuild eq = baseBuild()
			.eq(TLpglCert::getType, type)
			.eq(TLpglCert::getStatus, status);

		long currentTimeMillis = System.currentTimeMillis();
		long startStamp = DateUtil.getStartStamp(currentTimeMillis);
		long endStamp = DateUtil.getEndStamp(currentTimeMillis);
		eq = isToday? eq.gte(TLpglCert::getCreateTime, startStamp)
			.lt(TLpglCert::getCreateTime, endStamp) : eq;
		IdUtil.setTotal(eq);
		return MybatisPlus.getInstance().findAll(new TLpglCert(), eq.page(pageNum, pageSize)
		);
	}

	@Override
	public TLpglCert selectByPrimaryKey(Long certId) {
		return MybatisPlus.getInstance().findOne(new TLpglCert(), baseBuild()
			.eq(TLpglCert::getId, certId)
		);
	}

	@Override
	public int update(TLpglCert tLpglCert) {
		return MybatisPlus.getInstance().update(tLpglCert, baseBuild()
			.eq(TLpglCert::getId, tLpglCert.getId())
		);
	}

	@Override
	public int insert(TLpglCert build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public List<TLpglCert> selectByTypeAndStatusInApplyUserIdsPage(Integer type, Integer status, Integer pageNum, Integer pageSize, boolean isToday, List<Long> userIds) {
		return selectByTypeInStatusInApplyUserIdsPage(type, Arrays.asList(status), pageNum, pageSize, isToday, userIds);
	}

	@Override
	public List<TLpglCert> selectByTypeInStatusInApplyUserIdsPage(Integer type, List<Integer> status, Integer pageNum, Integer pageSize, boolean isToday, List<Long> userIds) {
		MybatisPlusBuild eq = baseBuild()
			.eq(TLpglCert::getType, type);
		eq = status != null? eq.in(TLpglCert::getStatus, status) : eq;

		long currentTimeMillis = System.currentTimeMillis();
		long startStamp = DateUtil.getStartStamp(currentTimeMillis);
		long endStamp = DateUtil.getEndStamp(currentTimeMillis);
		eq = isToday? eq.gte(TLpglCert::getCreateTime, startStamp)
			.lt(TLpglCert::getCreateTime, endStamp) : eq;
		eq = userIds == null || userIds.isEmpty()? eq: eq.in(TLpglCert::getApplyUserId, userIds);

		IdUtil.setTotal(eq);
		return MybatisPlus.getInstance().findAll(new TLpglCert(), eq.page(pageNum, pageSize)
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglCert.class)
			.eq(TLpglCert::getIsValid, AppConstant.IS_VALID_YES);
	}
}
