package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.UserEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.FundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.service.FundService;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:58
 */
@Service
public class FundServiceImpl implements FundService {

	@Autowired
	private FundDao fundDao;

	@Autowired
	private UserDao userDao;

	@Override
	public void applyForAFund(Long userId, Long amount, Long publishId, String orderNo) {
		TUser user = new TUser();
		/*//check实名状态
		Integer authenticationStatus = user.getAuthenticationStatus();
		if(!UserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请先进行实名认证!");
		}*/   //TODO 6.11 把对实名的check放到编辑名字时

		//check订单支付状态
		//TODO


	//开始创建基金流程:	//TODO 6.11	基金申请即通过，状态为未公开，达到标准线（eg.10000）时可以申请公开，此间状态为待审核
		//大部分值已经使用了默认值
		TCsqFund csqFund = TCsqFund.builder()
			.userId(userId)
			.trendPubKey(publishId.intValue())
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
		TUser user = userDao.selectByPrimaryKey(userId);
		//TODO
		//1.已实名
		//2.
		//  1）个人只能...
		//	(2)机构只能...
		Integer authType = user.getAuthenticationType();
//		if(UserEnum.AUTHENTICATION_TYPE_ORG_OR_CORP.toCode().equals(authType))
		return false;
	}
}
