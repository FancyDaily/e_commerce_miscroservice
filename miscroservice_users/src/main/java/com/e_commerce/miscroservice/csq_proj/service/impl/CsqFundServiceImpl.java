package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserEnum;
import com.e_commerce.miscroservice.commons.enums.application.UserEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import com.e_commerce.miscroservice.csq_proj.service.CsqOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:58
 */
@Service
public class CsqFundServiceImpl implements CsqFundService {

	@Autowired
	private CsqFundDao fundDao;

	@Autowired
	private CsqUserDao userDao;

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Autowired
	private CsqOrderService csqOrderService;

	@Override
	public void applyForAFund(Long userId, String orderNo) {
		//这其实是一系列支付成功后的操作,应当在notifyUrl指向的接口中被调用
		//基金生命周期：在用户请求微信支付(举例)时，产生待激活状态的实体，在用户支付成功后被激活.
		//check实名状态
		// 6.11 把对实名的check放到编辑名字时
		//6.11	基金申请即通过(付款后激活变为可用)，状态为未公开，达到标准线（eg.10000）时可以申请公开，此间状态为待审核
		TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);	//check订单支付状态
		csqOrderService.checkPaid(tCsqOrder);
	//开始激活基金流程:
		Long fundId = tCsqOrder.getFundId();
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		csqFund.setStatus(CsqFundEnum.STATUS_UNDER_CERT.getVal());	//激活
		fundDao.update(csqFund);
	}

	private void checkSingletonForPerson(TCsqUser user) {
		Long userId = user.getId();
		Integer authType = user.getAuthenticationType();
		if(UserEnum.AUTHENTICATION_TYPE_PERSON.toCode().equals(authType)) {
			List<TCsqFund> tCsqFunds = fundDao.selectByUserId(userId);
			if(!tCsqFunds.isEmpty()) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经有一项基金或基金审核中,请勿重复申请!");
			}
		}
	}

	@Override
	public void modifyFund(Long userId, TCsqFund fund) {
		if(fund == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "所有必要参数为空");
		}
		//check实名
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		if(!CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(tCsqUser.getAuthenticationStatus())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请先进行实名认证!");
		}
		Long fundId = fund.getId();
		TCsqFund csqFund = fundDao.selectByPrimaryKey(fundId);
		Integer currentStatus = csqFund.getStatus();
		//包含[申请公开]基金业务
		Integer status = fund.getStatus();
		if(CsqFundEnum.STATUS_UNDER_CERT.getVal() == currentStatus && CsqFundEnum.STATUS_PUBLIC.getVal() == status) {	//申请公开基金
			Double totalIn = csqFund.getTotalIn();
			if(totalIn < CsqFundEnum.PUBLIC_MINIMUM) {	//未达到标准
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您的基金未达到标准，请再接再厉!");
			}
		}
		fund.setBalance(null);	//若仅用于基本信息修改则不允许修改金额
		fundDao.update(fund);
	}

	@Override
	public boolean checkBeforeApplyForAFund(Long userId) {
		boolean checkResult = false;
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		Integer authenticationStatus = tCsqUser.getAuthenticationStatus();
		Integer authType = tCsqUser.getAuthenticationType();
		boolean singleFunder = CsqUserEnum.AUTHENTICATION_STATUS_NO.toCode().equals(authenticationStatus) ||
			CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus) && CsqUserEnum.AUTHENTICATION_TYPE_PERSON.toCode().equals(authType);
		singleFunder = true;	//按照前期的要求，所有的用户都是singleFunder
		if(singleFunder) {
			checkSingletonForPerson(tCsqUser);
		}
		checkResult = true;
		return checkResult;
	}

}
