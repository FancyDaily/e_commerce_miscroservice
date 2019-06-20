package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.service.CsqOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 11:20
 */
@Transactional(rollbackFor = Throwable.class)
@Service
public class CsqOrderServiceImpl implements CsqOrderService {

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Override
	public void checkPaid(String orderNo) {
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
		Integer status = tCsqOrder.getStatus();
		if(CsqOrderEnum.STATUS_ALREADY_PAY.getCode() != status) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单未支付！");
		}
	}

	@Override
	public void checkPaid(TCsqOrder csqOrder) {
		if(csqOrder == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数CsqOrder为空！");
		}
		checkPaid(csqOrder.getOrderNo());
	}

}
