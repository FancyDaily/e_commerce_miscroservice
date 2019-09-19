package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.dto.WechatPhoneAuthDto;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDailyDonateVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDonateRecordVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqSceneVo;

import java.util.List;
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
	 * @param wechatPhoneAuthDto
	 * @return
	 */
	Map<String, Object> openidLogin(String openid, String uid, WechatPhoneAuthDto wechatPhoneAuthDto);

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
	void sendPersonAuth(TCsqUserAuth csqUserAuth, String smsCode) throws Exception;

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
	 * @param name
	 * @param userHeadPortraitPath
	 * @param skipLogin
	 */
	Map<String, Object> registerAndSubmitCert(String telephone, String password, String validCode, TCsqUserAuth csqUserAuth, String name, String userHeadPortraitPath, boolean skipLogin);

	/**
	 * 组织实名认证审核
	 * @param userAuthId
	 * @param option
	 * @param content
	 */
	void certCorp(Long userAuthId, Integer option, String content);

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

	void recordForConsumption(Long userId, Long fromId, Integer fromType, Double amount, String wholeDescription, Long timeStamp);

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
	 * @param user
	 * @return
	 */
	Map<String, Object> registerBySMS(String telephone, String validCode, Integer type, TCsqUser user);

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
	 * @param isWechatAuth
	 */
	void modify(Long userId, CsqBasicUserVo csqBasicUserVo, boolean isWechatAuth);

	/**
	 * 查看个人基本信息
	 * @param userId
	 * @return
	 */
	CsqBasicUserVo infos(Long userId);

	/**
	 * 获取实名状态
	 * @param userId
	 * @return
	 */
	Map<String, Object> getAuthStatus(Long userId);

	/**
	 * 获取全局捐赠播报
	 * @return
	 */
	List<CsqDonateRecordVo> globleDonateRecord();

	/**
	 * 邀请回馈
	 * @param userIds
	 * @param scene
	 * @param inviterId
	 */
	void payInviter(Long userIds, String scene, Long inviterId);

	/**
	 * 提交组织实名认证
	 * @param userId
	 * @param userAuth
	 */
	void corpSubmit(Long userId, TCsqUserAuth userAuth);

	/**
	 * 邀请人信息
	 * @param userIds
	 * @return
	 */
	CsqBasicUserVo inviterInfo(Long userIds);

	/**
	 * 根据key获取scene值
	 * @param sceneKey
	 * @return
	 */
	CsqSceneVo getScene(String sceneKey);

	TCsqUser testRegister(TCsqUser csqUser);

	/**
	 * 生成openid手机号匹配者
	 * @param userIds
	 * @param userTel
	 * @return
	 */
	Map<String, Object> generateOpenidMatcher(Long userIds, String userTel);

	boolean isOpenidMatchGenerateAuth(Long userIds);

	Map dealWithOpenidMatcher(Long userIds, String sceneKey, String userTel);

	Object dealWithOpenidMatcher(Long userIds, String sceneKey, String userTel, boolean needPayments);

	TCsqUser findCsqUserByUserTel(String userTel);

	QueryResult<TCsqUser> list(Long managerId, String searchParam, Integer pageNum, Integer pageSize, Boolean fuzzySearch, Integer accountType, Integer availableStatus, Boolean gotBalance, Boolean isFuzzySearch);

	TCsqUserAuth corpCertDetail(Long userAuthId);

	QueryResult<TCsqUserAuth> certCorpList(Integer status, Integer pageNum, Integer pageSize);

	int certCorpCount(Integer status);

	Map qrCode(Long userId, CsqSceneVo scene, String page, Integer uploadCode);

	String share(String name);

	Map<String, Object> platformPreview();
}
