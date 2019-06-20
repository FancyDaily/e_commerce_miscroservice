package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:35
 */
@Component
public class CsqUserPaymentDaoImpl implements CsqUserPaymentDao {

	private MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES);
	}

	private MybatisPlusBuild whereBuildByToType(int... type) {
		return baseWhereBuild()
			.in(TCsqUserPaymentRecord::getToType, type);
	}

	private MybatisPlusBuild whereBuildByFromType(int... type) {
		return baseWhereBuild()
			.in(TCsqUserPaymentRecord::getToType, type);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByToType(int... type) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), whereBuildByToType(type));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByFromTypeAndToTypeDesc(int fromType, int toType) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getFromType, fromType)
			.eq(TCsqUserPaymentRecord::getToType, toType)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime))
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByToTypeDesc(int type) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), whereBuildByToType(type)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime)));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByToTypeAndUserId(long userId, int... type) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), whereBuildByToType(type)
			.eq(TCsqUserPaymentRecord::getUserId, userId));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByFromTypeAndServiceId(int toCode, Long serviceId) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), whereBuildByFromType(toCode)
			.eq(TCsqUserPaymentRecord::getServiceId, serviceId));
	}

	@Override
	public int insert(TCsqUserPaymentRecord build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByFromTypeAndServiceIdDesc(int toCode, Long serviceId) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), whereBuildByFromType(toCode)
			.eq(TCsqUserPaymentRecord::getServiceId, serviceId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime)));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByToTypeAndToId(int toCode, Long fundId) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), whereBuildByToType(toCode)
			.eq(TCsqUserPaymentRecord::getFundId, fundId));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByToTypeAndToIdAndCreateTimeBetween(int toCode, Long fundId) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), whereBuildByToType(toCode)
			.eq(TCsqUserPaymentRecord::getFundId, fundId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime)));
	}
}
