package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import org.springframework.stereotype.Component;
import sun.security.krb5.internal.PAEncTSEnc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:13
 */
@Component
public class CsqOrderDaoImpl implements CsqOrderDao {

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
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, toCode)
			.eq(TCsqOrder::getInVoiceStatus, code));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusDesc(Long userId, int toCode, int code) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, toCode)
			.eq(TCsqOrder::getInVoiceStatus, code)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime)));
	}

	@Override
	public List<TCsqOrder> selectInOrderNos(String... orderNo) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), inOrderNosBuild(orderNo));
	}

	private MybatisPlusBuild inOrderNosBuild(String[] orderNo) {
		return new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqOrder::getOrderNo, orderNo);
	}

	@Override
	public int update(List<TCsqOrder> toUpdateList) {
		List<Long> toUpdateIds = toUpdateList.stream()
			.map(TCsqOrder::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(toUpdateList, new MybatisPlusBuild(TCsqOrder.class)
			.in(TCsqOrder::getId, toUpdateIds));
	}

	@Override
	public int update(List<TCsqOrder> toUpdateList, List<Long> toUpdateIds) {
		return MybatisPlus.getInstance().update(toUpdateList, new MybatisPlusBuild(TCsqOrder.class)
			.in(TCsqOrder::getId, toUpdateIds));
	}

	@Override
	public List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIds(Long fromId, int fromType, int toType, List<Long> orderIds) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getFromId, fromId)
			.eq(TCsqOrder::getFromType, fromType)
			.eq(TCsqOrder::getToType, toType)
			.in(TCsqOrder::getId, orderIds));
	}

	@Override
	public List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIdsAndStatus(Long fromId, int fromType, int toType, List<Long> tOrderIds, int status) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getFromId, fromId)
			.eq(TCsqOrder::getFromType, fromType)
			.eq(TCsqOrder::getToType, toType)
			.in(TCsqOrder::getId, tOrderIds)
			.eq(TCsqOrder::getStatus, status));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndToTypeDesc(Long userId, int toCode) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getToType, toCode)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime)));
	}

	@Override
	public List<TCsqOrder> selectByToIdAndToTypeAndOrderTimeBetweenDesc(Long toId, int toCode, long startStamp, long endStamp) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getToId, toId)
			.eq(TCsqOrder::getToType, toCode)
			.between(TCsqOrder::getOrderTime, startStamp, endStamp)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getUpdateTime)));
	}

	@Override
	public TCsqOrder selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(Long userId, Long fromId, int fromtype, Long toId, int toType, Double amount, Integer status) {
		return MybatisPlus.getInstance().findOne(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromId, fromId)
			.eq(TCsqOrder::getFromType, fromtype)
			.eq(TCsqOrder::getToId, toId)
			.eq(TCsqOrder::getToType, toType)
			.eq(TCsqOrder::getPrice, amount)
			.eq(TCsqOrder::getStatus, status)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime)));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndToTypeInvoiceStatusAndStatusDesc(Long userId, int fromType, int toType, int status) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, fromType)
			.eq(TCsqOrder::getToType, toType)
			.eq(TCsqOrder::getInVoiceStatus, status)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getUpdateTime)));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndToTypeInvoiceStatusAndStatusDesc(Long userId, int fromType, int toType, int invoiceStatus, int status) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, fromType)
			.eq(TCsqOrder::getToType, toType)
			.eq(TCsqOrder::getInVoiceStatus, invoiceStatus)
			.eq(TCsqOrder::getStatus, status)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getUpdateTime)));
	}

	@Override
	public List<TCsqOrder> selectByToIdAndToTypeAndStatusDesc(Long entityId, int toCode, int status) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getToId, entityId)
			.eq(TCsqOrder::getToType, toCode)
			.eq(TCsqOrder::getStatus, status)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getUpdateTime)));
	}

	@Override
	public int insert(TCsqOrder csqOrder) {
		return MybatisPlus.getInstance().save(csqOrder);
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndToTypeInToIdAndStatus(Long userId, int toType, int toIds, int Status) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getToType, toType)
			.in(TCsqOrder::getToId, toIds)
			.eq(TCsqOrder::getStatus, Status));
	}

	@Override
	public List<TCsqOrder> selectByUserIdInToIdAndStatus(Long userId, List<Long> toIds, int Status) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getUserId, userId)
			.in(TCsqOrder::getToId, toIds)
			.eq(TCsqOrder::getStatus, Status));
	}

	@Override
	public List<TCsqOrder> selectInIds(List<Long> orderIds) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.in(TCsqOrder::getId, orderIds)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusAndStatusDesc(Long userId, int toCode, int code, int code1) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), ByUserIdAndFromTypeAndInvoiceStatusAndStatusDescBuild(userId, toCode, code, code1));
	}

	private MybatisPlusBuild ByUserIdAndFromTypeAndInvoiceStatusAndStatusDescBuild(Long userId, int toCode, int code, int code1) {
		return new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getFromType, toCode)
			.eq(TCsqOrder::getInVoiceStatus, code)
			.eq(TCsqOrder::getStatus, code1)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime));
	}

	@Override
	public List<TCsqOrder> selectByToIdAndToTypeAndStatusAndOrderTimeBetweenDesc(Long toId, int toType, int status, long startStamp, long endStamp) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getToId, toId)
			.eq(TCsqOrder::getToType, toType)
			.eq(TCsqOrder::getStatus, status)
			.between(TCsqOrder::getOrderTime, startStamp, endStamp)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndToTypeAndToIdDesc(Long userId, int toCode, Long serviceId) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getToType, toCode)
			.eq(TCsqOrder::getToId, serviceId)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqOrder> selectByUserIdDesc(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqOrder> selectByUserIdInToTypeDesc(Long userId, int toCode, int toCode1) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), byUserIdInToTypeDescBuild(userId));
	}

	@Override
	public List<TCsqOrder> selectByFromIdAndFromTypeInOrderIdsAndStatus(Long fundId, int toCode, List<Long> tOrderIds, Integer code) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), byFromIdAndFromTypeInOrderIdsAndStatusBuild(fundId, toCode, tOrderIds, code));
	}

	@Override
	public TCsqOrder selectByPrimaryKey(Long orderId) {
		return MybatisPlus.getInstance().findOne(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqOrder::getId, orderId));
	}

	@Override
	public List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusAndStatusDescPage(Integer pageNum, Integer pageSize, Long userId, int toCode, Integer code, Integer code1) {
		MybatisPlusBuild mybatisPlusBuild = ByUserIdAndFromTypeAndInvoiceStatusAndStatusDescBuild(userId, toCode, code, code1);
		IdUtil.setTotal(mybatisPlusBuild);
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqOrder> selectInOrderNosPage(String[] split, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = inOrderNosBuild(split);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqOrder(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqOrder> selectByUserIdInToTypeDescPage(Integer pageNum, Integer pageSize, Long userId, int toCode, int toCode1) {
		MybatisPlusBuild mybatisPlusBuild = byUserIdInToTypeDescBuild(userId);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqOrder(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqOrder> selectByFromIdAndFromTypeInOrderIdsAndStatusPage(Integer pageNum, Integer pageSize, Long fundId, int toCode, List<Long> tOrderIds, Integer code) {
		MybatisPlusBuild build = byFromIdAndFromTypeInOrderIdsAndStatusBuild(fundId, toCode, tOrderIds, code);
		IdUtil.setTotal(build);

		return MybatisPlus.getInstance().findAll(new TCsqOrder(), build.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqOrder> selectByUserIdInToTypeAndStatusDesc(Long userId, int toCode, int toCode1) {
		return MybatisPlus.getInstance().findAll(new TCsqOrder(), byUserIdInToTypeBuild(userId)
			.eq(TCsqOrder::getStatus, CsqOrderEnum.STATUS_ALREADY_PAY.getCode())
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime)));
	}

	private MybatisPlusBuild byFromIdAndFromTypeInOrderIdsAndStatusBuild(Long fundId, int toCode, List<Long> tOrderIds, Integer code) {
		return new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getFromId, fundId)
			.eq(TCsqOrder::getFromType, toCode)
			.in(TCsqOrder::getId, tOrderIds)
			.eq(TCsqOrder::getStatus, code);
	}

	private MybatisPlusBuild byUserIdInToTypeDescBuild(Long userId) {
		return byUserIdInToTypeBuild(userId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqOrder::getCreateTime));
	}

	private MybatisPlusBuild byUserIdInToTypeBuild(Long userId) {
		return new MybatisPlusBuild(TCsqOrder.class)
			.eq(TCsqOrder::getUserId, userId)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqOrder::getToType, Arrays.asList(CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqEntityTypeEnum.TYPE_FUND.toCode()));
	}

}
