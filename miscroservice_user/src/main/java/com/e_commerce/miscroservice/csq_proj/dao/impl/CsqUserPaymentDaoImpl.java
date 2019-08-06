package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
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

	/*private MybatisPlusBuild whereBuildByToType(int... type) {
		return new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqUserPaymentRecord::getToType, type);
	}

	private MybatisPlusBuild whereBuildByFromType(int... type) {
		return new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqUserPaymentRecord::getToType, type);
	}*/

	@Override
	public List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOut(Long entityId, int entityType, int inOut) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUserPaymentRecord::getEntityId, entityId)
			.eq(TCsqUserPaymentRecord::getEntityType, entityType)
			.eq(TCsqUserPaymentRecord::getInOrOut, inOut));
	}

	@Override
	public int insert(TCsqUserPaymentRecord... build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public int multiInsert(List<TCsqUserPaymentRecord> builds) {
		return MybatisPlus.getInstance().save(builds);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOutDesc(Long entityId, int entityType, int inOut) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), byEntityIdAndEntityTypeAndInOutDescBuild(entityId, entityType, inOut));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInOrderIdsAndInOut(List<Long> orderIds, int toCode) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUserPaymentRecord::getInOrOut, toCode)
			.in(TCsqUserPaymentRecord::getOrderId, orderIds));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndInOrOut(Long userId, int toCode) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getInOrOut, toCode)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInOrderIdsAndInOutDesc(List<Long> orderIds, int toCode) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), inOrderIdsAndInOutDescBuild(orderIds, toCode)
			);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOutDescPage(Long entityId, int entityType, int inOut) {
		MybatisPlusBuild mybatisPlusBuild = byEntityIdAndEntityTypeAndInOutDescBuild(entityId, entityType, inOut);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInOrderIdsAndInOutDescPage(Integer pageNum, Integer pageSize, List<Long> orderIds, int toCode) {
		MybatisPlusBuild mybatisPlusBuild = inOrderIdsAndInOutDescBuild(orderIds, toCode);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild.page(pageNum, pageSize)
		);
	}

	private MybatisPlusBuild inOrderIdsAndInOutDescBuild(List<Long> orderIds, int toCode) {
		return new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqUserPaymentRecord::getOrderId, orderIds)
			.eq(TCsqUserPaymentRecord::getInOrOut, toCode)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime));
	}

	private MybatisPlusBuild byEntityIdAndEntityTypeAndInOutDescBuild(Long entityId, int entityType, int inOut) {
		return new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUserPaymentRecord::getEntityId, entityId)
			.eq(TCsqUserPaymentRecord::getEntityType, entityType)
			.eq(TCsqUserPaymentRecord::getInOrOut, inOut)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime));
	}
}