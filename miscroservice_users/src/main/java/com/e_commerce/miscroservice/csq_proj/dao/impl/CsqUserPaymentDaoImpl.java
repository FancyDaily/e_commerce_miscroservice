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

	/*private MybatisPlusBuild whereBuildByToType(int... type) {
		return baseWhereBuild()
			.in(TCsqUserPaymentRecord::getToType, type);
	}

	private MybatisPlusBuild whereBuildByFromType(int... type) {
		return baseWhereBuild()
			.in(TCsqUserPaymentRecord::getToType, type);
	}*/

	@Override
	public List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOut(Long entityId, int entityType, int inOut) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), baseWhereBuild()
			.eq(TCsqUserPaymentRecord::getEntityId, entityId)
			.eq(TCsqUserPaymentRecord::getEntityType, entityType)
			.eq(TCsqUserPaymentRecord::getInOrOut, inOut));
	}

	@Override
	public int insert(TCsqUserPaymentRecord... build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOutDesc(Long entityId, int entityType, int inOut) {
		return MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(), baseWhereBuild()
			.eq(TCsqUserPaymentRecord::getEntityId, entityId)
			.eq(TCsqUserPaymentRecord::getEntityType, entityType)
			.eq(TCsqUserPaymentRecord::getInOrOut, inOut)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime)));
	}
}
