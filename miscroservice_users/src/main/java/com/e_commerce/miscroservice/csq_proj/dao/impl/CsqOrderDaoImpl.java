package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:13
 */
@Component
public class CsqOrderDaoImpl implements CsqOrderDao {

	private MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public TCsqOrder selectByOrderNo(String orderNo) {
		return MybatisPlus.getInstance().findOne(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
		.eq(TCsqOrder::getOrderNo, orderNo)
		.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int update(TCsqOrder tCsqOrder) {
		return MybatisPlus.getInstance().update(tCsqOrder, new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getId, tCsqOrder.getId()));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatus(Long userId, int toCode, int code) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, toCode)
			.eq(TCsqOrder::getInVoiceStatus, code));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusDesc(Long userId, int toCode, int code) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, toCode)
			.eq(TCsqOrder::getInVoiceStatus, code)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime)));
	}

	@Override
	public List<TCsqOrder> selectInOrderNos(String... orderNo) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.in(TCsqOrder::getOrderNo, orderNo));
	}

	@Override
	public int update(List<TCsqOrder> toUpdateList) {
		List<Long> toUpdateIds = toUpdateList.stream()
			.map(TCsqOrder::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(toUpdateList, baseWhereBuild()
			.in(TCsqOrder::getId, toUpdateIds));
	}

	@Override
	public List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIds(Long fromId, int fromType, int toType, List<Long> orderIds) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getFromId, fromId)
			.eq(TCsqOrder::getFromType, fromType)
			.eq(TCsqOrder::getToType, toType)
			.in(TCsqOrder::getId, orderIds));
	}

	@Override
	public List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIdsAndStatus(Long fromId, int fromType, int toType, List<Long> tOrderIds, int status) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getFromId, fromId)
			.eq(TCsqOrder::getFromType, fromType)
			.eq(TCsqOrder::getToType, toType)
			.in(TCsqOrder::getId, tOrderIds)
			.eq(TCsqOrder::getStatus, status));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndToTypeDesc(Long userId, int toCode) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getToType, toCode)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime)));
	}

	@Override
	public List<TCsqOrder> selectByToIdAndToTypeAndUpdateTimeBetweenDesc(Long toId, int toCode, long startStamp, long endStamp) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getToId, toId)
			.eq(TCsqOrder::getToType, toCode)
			.between(TCsqOrder::getUpdateTime, startStamp, endStamp)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getUpdateTime)));
	}

	@Override
	public TCsqOrder selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(Long userId, Long fromId, int fromtype, Long toId, int toType, Double amount, Integer status) {
		return MybatisPlus.getInstance().findOne(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromId, fromId)
			.eq(TCsqOrder::getToId, toId)
			.eq(TCsqOrder::getToId, toType)
			.eq(TCsqOrder::getPrice, amount)
			.eq(TCsqOrder::getStatus, status)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime)));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndToTypeInvoiceStatusDesc(Long userId, int fromType, int toType, int status) {
		return MybatisPlus.getInstance().finAll(new TCsqOrder(), baseWhereBuild()
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, fromType)
			.eq(TCsqOrder::getToType, toType)
			.eq(TCsqOrder::getStatus, status)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getUpdateTime)));
	}

}