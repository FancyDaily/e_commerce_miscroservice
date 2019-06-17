package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;

import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:44
 */
public interface CsqUserService {
	/**
	 * 实名认证检查
	 * @param user
	 */
	void checkAuth(TCsqUser user);

	/**
	 * 实名认证检查
	 * @param userId
	 */
	void checkAuth(Long userId);

	/**
	 * 根据openid登录
	 * @param openid
	 * @param uid
	 * @return
	 */
	Map<String, Object> openidLogin(String openid, String uid);

	/**
	 * 绑定手机号码
	 * @param userId
	 * @param telephone
	 * @param smsCode
	 */
	void bindCellPhone(Long userId, String telephone, String smsCode);

	/**
	 * 提交实名认证请求-个人
	 * @param csqUserAuth
	 * @param smsCode
	 */
	void sendPersonAuth(TCsqUserAuth csqUserAuth, String smsCode);

	/**
	 * 提交sheng
	 * @param csqUserAuth
	 */
	void sendCorpAuth(TCsqUserAuth csqUserAuth);
}
