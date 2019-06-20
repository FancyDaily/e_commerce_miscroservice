package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDailyDonateVo;

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

	Map<String, Object> loginByTelephone(String telephone, String password, Integer option, String uuid);

	/**
	 * 注册组织账号与提交组织账号认证申请
	 * @param telephone
	 * @param password
	 * @param validCode
	 * @param csqUserAuth
	 */
	void registerAndSubmitCert(String telephone, String password, String validCode, TCsqUserAuth csqUserAuth);

	/**
	 * 组织实名认证审核
	 * @param userAuthId
	 * @param option
	 */
	void certCorp(Long userAuthId, Integer option);

	/**
	 * 日行一善
	 * @param userId
	 */
	CsqDailyDonateVo dailyDonateDetail(Long userId);

	/**
	 * 定制日推
	 * @param weekDayCnt
	 * @param serviceIds
	 */
	void customizeDailyDonateList(Integer weekDayCnt, Long... serviceIds);

	void register(String telephone, String password, String uuid);
}
