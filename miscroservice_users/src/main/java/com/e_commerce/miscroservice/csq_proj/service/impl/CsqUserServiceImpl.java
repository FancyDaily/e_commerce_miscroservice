package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.UserEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:45
 */
@Service
public class CsqUserServiceImpl implements CsqUserService {

	@Autowired
	private CsqUserDao csqUserDao;

	@Override
	public void checkAuth(TCsqUser user) {
		if(user==null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户不存在");
		}
		Integer authenticationStatus = user.getAuthenticationStatus();
		if (!UserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请先进行实名认证!");
		}
	}

	@Override
	public void checkAuth(Long userId) {
		if(userId == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数用户编号为空");
		}
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		checkAuth(tCsqUser);
	}
}
