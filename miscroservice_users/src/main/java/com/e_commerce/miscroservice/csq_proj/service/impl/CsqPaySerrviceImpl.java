package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:41
 */
@Service
public class CsqPaySerrviceImpl implements CsqPayService {

	@Autowired
	private CsqOrderDao orderDao;

	@Autowired
	private CsqFundDao fundDao;

	@Override
	public Object preFundOrder(Long userId, Double amount) {	//发起支付后的业务流程
		//TODO 用户性质的特异行为
		//用户发起支付后产生的业务行为
		//1.判断是否需要产生基金 -> 判断依据: （对一个用户只能创建一个基金这件事不在此做校验）
		long currentTimeMillis = System.currentTimeMillis();
		TCsqFund csqFund = null;
		List<TCsqFund> tCsqFunds = fundDao.selectByUserIdAndStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
		if(tCsqFunds.isEmpty()) {	//如果不存在
			csqFund = TCsqFund.builder().status(CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal()).build();
			fundDao.insert(csqFund);
		} else {
			csqFund = tCsqFunds.get(0);
		}
		//2.查看是否可以复用未支付的相同金额有效订单
		TCsqOrder csqOrder = orderDao.selectByUserIdAndTypeAndAmountValid(userId, CsqOrderEnum.TYPE_FUND_APPLY.getCode(), amount);	//已对超时作判断
		//TODO 对失活订单置成无效状态(不使用Mq时)
		if(csqOrder==null) {	//创建一个订单
			String orderNo = UUIdUtil.generateOrderNo();
			csqOrder = TCsqOrder.builder().status(CsqOrderEnum.STATUS_UNPAY.getCode())
				.type(CsqOrderEnum.TYPE_FUND_APPLY.getCode())
				.orderTime(currentTimeMillis)
				.orderNo(orderNo)
				.price(amount)
				.fundId(csqFund.getId())
				.userId(userId).build();
		}
		// 把一些参数返回给前端或者上一级方法
		Map resultMap = new HashMap();
		return resultMap;
	}

	public void wxNotify() {

	}

	private void afterApplyFundSuccess() {

	}
}
