package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
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

	/**
	 * 根据id查找
	 * @param userId
	 * @return
	 */
	Map<String,Object> findCsqUserById(Long userId);

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

	boolean isDailyDonateServiceId(Long serviceId);

	/**
	 * 定制日推
	 * @param weekDayCnt
	 * @param serviceIds
	 */
	void customizeDailyDonateList(Integer weekDayCnt, Long... serviceIds);

	void register(String telephone, String password, String uuid);

	/**
	 * 分享
	 * @param userId
	 * @param entityId
	 * @param option
	 * @return
	 */
	Map<String, Object> share(Long userId, Long entityId, Integer option);

	/**
	 * 平台托管产生的记录
	 * @param userId
	 * @param fromId
	 * @param fromType
	 * @param amount
	 * @param wholeDescription
	 */
	void recordForConsumption(Long userId, Long fromId, Integer fromType, Double amount, String wholeDescription);

	/**
	 * 个人实名认证审核
	 * @param userAuthId
	 * @param option
	 */
	void certPerson(Long userAuthId, Integer option);

	/**
	 * 手机号验证码注册
	 * @param telephone
	 * @param validCode
	 * @param type
	 * @return
	 */
	Map<String, Object> registerBySMS(String telephone, String validCode, Integer type);

	/**
	 * 手机号验证码登录
	 *
	 * @param uuid
	 * @param telephone
	 * @param validCode
	 * @param type
	 * @return
	 */
	Map<String, Object> loginBySMS(String uuid, String telephone, String validCode, Integer type);

	/**
	 * 修改个人基本信息
	 * @param userId
	 * @param csqBasicUserVo
	 */
	void modify(Long userId, CsqBasicUserVo csqBasicUserVo);

	/**
	 * 查看个人基本信息
	 * @param userId
	 * @return
	 */
	CsqBasicUserVo infos(Long userId);
}
