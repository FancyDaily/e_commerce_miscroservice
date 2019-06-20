package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
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
	public TCsqOrder selectByUserIdAndFundId(Long userId, Long fundId) {
		return MybatisPlus.getInstance().findOne(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
		.eq(TCsqOrder::getUserId, userId)
		.eq(TCsqOrder::getFundId, fundId)
		.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqOrder selectByOrderNo(String orderNo) {
		return MybatisPlus.getInstance().findOne(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
		.eq(TCsqOrder::getOrderNo, orderNo)
		.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TCsqOrder selectByUserIdAndTypeAndAmountValid(Long userId, int type, Double amount) {
		Long mills = System.currentTimeMillis() - CsqOrderEnum.INTERVAL;
		return MybatisPlus.getInstance().findOne(new TCsqOrder(), new MybatisPlusBuild(TCsqOrder.class)
		.eq(TCsqOrder::getUserId, userId)
		.eq(TCsqOrder::getType, type)
		.eq(TCsqOrder::getPrice, amount)
		.gt(TCsqOrder::getOrderTime, mills)
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

}
