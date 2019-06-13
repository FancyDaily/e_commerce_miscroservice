package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:13
 */
@Component
public class CsqOrderDaoImpl implements CsqOrderDao {
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
}
