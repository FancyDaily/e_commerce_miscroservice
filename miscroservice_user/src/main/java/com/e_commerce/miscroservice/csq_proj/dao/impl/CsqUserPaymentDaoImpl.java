package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
	public int insert(List<TCsqUserPaymentRecord> builds) {
		return MybatisPlus.getInstance().save(builds);
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
	public List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOutDescPage(Long entityId, int entityType, int inOut, int pageNum, int pageSize) {
		MybatisPlusBuild mybatisPlusBuild = byEntityIdAndEntityTypeAndInOutDescBuild(entityId, entityType, inOut);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInOrderIdsAndInOutDescPage(Integer pageNum, Integer pageSize, List<Long> orderIds, int toCode) {
		MybatisPlusBuild mybatisPlusBuild = inOrderIdsAndInOutDescBuild(orderIds, toCode);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild.page(pageNum, pageSize)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByNotNullExtend() {
		/*return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.isNotNull(TCsqUserPaymentRecord::getExtend));*/
		List<TCsqUserPaymentRecord> all = MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES));
		return all.stream()
			.filter(a -> a.getExtend() != null).collect(Collectors.toList());
	}

	@Override
	public int update(List<TCsqUserPaymentRecord> toUpdater) {
		List<Long> toUpdaterIds = toUpdater.stream()
			.map(TCsqUserPaymentRecord::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(toUpdater, new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.in(TCsqUserPaymentRecord::getId, toUpdaterIds));

	}

	@Override
	public List<TCsqUserPaymentRecord> selectInEntityType(int... entityType) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqUserPaymentRecord::getEntityType, entityType));
	}

	@Override
	public TCsqUserPaymentRecord selectByPrimaryKey(Long recordId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getId, recordId)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInPrimaryKeys(List<Long> recordIds) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.in(TCsqUserPaymentRecord::getId, recordIds)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInOrderIds(List<Long> orderIds) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqUserPaymentRecord::getOrderId, orderIds)
		);
	}

	@Override
	public MybatisPlusBuild baseBuild() {
		MybatisPlusBuild baseBuild = new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES);
		return baseBuild;
	}

	@Override
	public List<TCsqUserPaymentRecord> selectWithBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize) {
		IdUtil.setTotal(baseBuild);
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), baseBuild.page(pageNum, pageSize)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectWithBuild(MybatisPlusBuild baseBuild) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), baseBuild
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInEntityIdsAndEntityTypeAndInOrOut(List<Long> serviceIds, int entityType, int iOrOut) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), inEntityIdsAndEntityTypeAndInOrOut(serviceIds, entityType, iOrOut)
		);
	}

	private MybatisPlusBuild inEntityIdsAndEntityTypeAndInOrOut(List<Long> serviceIds, int entityType, int iOrOut) {
		return baseBuild()
			.in(TCsqUserPaymentRecord::getEntityId, serviceIds)
			.eq(TCsqUserPaymentRecord::getEntityType, entityType)
			.eq(TCsqUserPaymentRecord::getInOrOut, iOrOut);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectInEntityIdsAndEntityTypeAndInOrOutDesc(List<Long> serviceIds, int entityType, int iOrOut) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), inEntityIdsAndEntityTypeAndInOrOut(serviceIds, entityType, iOrOut)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime))
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
