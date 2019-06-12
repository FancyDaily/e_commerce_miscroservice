package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
	public void preFundOrder(Long userId, Double amount) {
		//用户发起支付后产生的业务行为
		//1.判断是否需要产生基金 -> 判断依据: （对一个用户只能创建一个基金这件事不在此做校验）
		List<TCsqFund> tCsqFunds = fundDao.selectByUserIdAndStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
		if(!tCsqFunds.isEmpty()) {	//如果存在
			//TODO

		}

		//2.查看是否可以服用未支付的有效订单(需要对比金额)
	}
}
