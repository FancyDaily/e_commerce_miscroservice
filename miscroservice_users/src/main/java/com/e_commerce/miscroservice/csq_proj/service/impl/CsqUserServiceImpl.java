package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserAuthEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserEnum;
import com.e_commerce.miscroservice.commons.enums.application.UserEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserAuthDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService.DEFAULT_PASS;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:45
 */
@Service
public class CsqUserServiceImpl implements CsqUserService {

	@Autowired
	private CsqUserDao csqUserDao;

	@Autowired
	private AuthorizeRpcService authorizeRpcService;

	@Autowired
	private UserService userService;

	@Autowired
	private CsqUserAuthDao csqUserAuthDao;

	@Override
	public void checkAuth(TCsqUser user) {
		if (user == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户不存在");
		}
		Integer authenticationStatus = user.getAuthenticationStatus();
		if (!UserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请先进行实名认证!");
		}
	}

	@Override
	public void checkAuth(Long userId) {
		if (userId == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数用户编号为空");
		}
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		checkAuth(tCsqUser);
	}

	@Override
	public Map<String, Object> openidLogin(String openid, String uuid) {
		TCsqUser tCsqUser = csqUserDao.selectByVxOpenIdAndAccountType(openid, CsqUserEnum.ACCOUNT_TYPE_PERSON.toCode());
		if (tCsqUser == null) {    //进行注册
			tCsqUser = new TCsqUser();
			tCsqUser.setUuid(uuid);
			tCsqUser = register(tCsqUser);
		}
		//登录
		String token = tCsqUser.getToken();
		if (token == null) {
			String namePrefix = UserUtil.getApplicationNamePrefix(ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode());
			Token load = authorizeRpcService.load(namePrefix + tCsqUser.getId(),
				DEFAULT_PASS, uuid);
			if (load != null && load.getToken() != null) {
				token = load.getToken();
				tCsqUser.setToken(token);
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("token", token);
		resultMap.put("user", tCsqUser);
		return resultMap;
	}

	@Override
	public void bindCellPhone(Long userId, String telephone, String smsCode) {
		//检查短信
		userService.checkSMS(telephone, smsCode);
		//获取用户信息
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		Integer accountType = tCsqUser.getAccountType();
		//检查手机号是否已经绑定其他账号
		TCsqUser existUser = csqUserDao.selectByUserTelAndAccountType(telephone, accountType);    //机构与个人的【绑定手机号】平行互不干扰
		if (existUser != null) {
			if (userId.equals(existUser.getId())) {
				return;
			}
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该手机已绑定其他账号，如需解绑，请联系客服!");
		}
		tCsqUser.setUserTel(telephone);
		csqUserDao.updateByPrimaryKey(tCsqUser);
	}

	@Override
	public void sendPersonAuth(TCsqUserAuth csqUserAuth, String smsCode) {
		if(StringUtil.isAnyEmpty(csqUserAuth.getCardId(), csqUserAuth.getName(), csqUserAuth.getPhone())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "必填参数不全！");
		}
		Long userId = csqUserAuth.getUserId();
		//判断是否已经实名
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		Integer authenticationStatus = tCsqUser.getAuthenticationStatus();
		if(CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经实名过，无需重复实名!");	//或改为return
		}
		csqUserAuth.setStatus(CsqUserAuthEnum.STATUS_UNDER_CERT.getCode());	//待审核(默认)
		csqUserAuthDao.insert(csqUserAuth);
		//TODO 若提交即实名，此处将用户实名状态改变
	}

	@Override
	public TCsqUser findCsqUserById(Long userId) {

		return csqUserDao.selectByPrimaryKey(userId);
	}

	private TCsqUser register(TCsqUser csqUser) {
		//默认头像等...
		csqUser.setUserHeadPortraitPath(CsqUserEnum.DEFAULT_HEADPORTRAITURE_PATH);
		csqUserDao.insert(csqUser);
		//注册到认证中心
		String namePrefix = UserUtil.getApplicationNamePrefix(ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode());
		Token token = authorizeRpcService.reg(namePrefix + csqUser.getId(), DEFAULT_PASS, csqUser.getId().toString(), csqUser.getUuid(), Boolean.FALSE);

		if (token != null && token.getToken() != null) {
			csqUser.setToken(token.getToken());
		}
		return csqUser;
	}

}
