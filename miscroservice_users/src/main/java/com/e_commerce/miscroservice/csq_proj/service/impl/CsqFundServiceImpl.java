package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.UserEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
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

	@Override
	public void applyForAFund(Long userId, Long FundId, Long amount, Long publishId, String orderNo) {
		//这其实是一系列支付成功后的操作

		//check实名状态	//6.11 把对实名的check放到编辑名字时
		//TODO check订单支付状态
		csqOrderDao.selectByUserIdAndFundId(userId, FundId);	//基金生命周期：在用户请求微信支付(举例)时，产生待激活状态的实体，在用户支付成功后被激活.
	//开始创建基金流程:	//6.11	基金申请即通过(付款后激活变为可用)，状态为未公开，达到标准线（eg.10000）时可以申请公开，此间状态为待审核
		//大部分值已经使用了默认值
		TCsqFund csqFund = TCsqFund.builder()
			.userId(userId)
			.trendPubKey(publishId.intValue())
			.status(CsqFundEnum.STATUS_UNDER_CERT.getVal())	//未公开
			.orgName("未命名").build();
		fundDao.insert(csqFund);
	}

	private void checkSingletonForPerson(TUser user) {
		Long userId = user.getId();
		Integer authType = user.getAuthenticationType();
		if(UserEnum.AUTHENTICATION_TYPE_PERSON.toCode().equals(authType)) {
			List<TCsqFund> tCsqFunds = fundDao.selectByUserId(userId);
			if(!tCsqFunds.isEmpty()) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经有基金或基金审核中,请勿重复申请!");
			}
		}
	}

	@Override
	public void modifyFund(TCsqFund fund) {

	}

	@Override
	public boolean checkBeforeApplyForAFund(Long userId) {
		TCsqUser tCsqUser = userDao.selectByPrimaryKey(userId);
		//TODO
		//1.已实名
		//2.
		//  1）个人只能...
		//	(2)机构只能...
		Integer authType = tCsqUser.getAuthenticationType();
//		if(UserEnum.AUTHENTICATION_TYPE_ORG_OR_CORP.toCode().equals(authType))
		return false;
	}
}
