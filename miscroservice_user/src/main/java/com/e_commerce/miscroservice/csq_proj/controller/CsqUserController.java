package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.constant.CsqWechatConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.dto.WechatPhoneAuthDto;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDailyDonateVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDonateRecordVo;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.wechat.entity.WechatSession;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 从善桥用户Controller
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:37
 */
@RestController
@Log
@RequestMapping("csq/user")
public class CsqUserController {

	@Autowired
	private WechatService wechatService;

	@Autowired
	private CsqUserService csqUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;

	/**
	 * 与微信校验授权
	 *
	 * @param code 加密数据
	 * @return
	 */
	@RequestMapping("checkAuth")
	public AjaxResult checkAuth(String code) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("与微信校验授权，code={}");
			WechatSession wechatSession = wechatService.checkAuthCode(code, CsqWechatConstant.APP_ID, CsqWechatConstant.APP_SECRET);
			log.info("openid={}", wechatSession.getOpenid());
			log.info("wechatSession={}", wechatSession);
			result.setData(wechatSession);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "与微信校验授权", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("与微信校验授权", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 手机号验证码注册
	 *
	 * @param telephone 手机号
	 * @param validCode 验证码
	 * @param type      1个人 2组织
	 * @return
	 */
	@Consume(TCsqUser.class)
	@RequestMapping("register/sms")
	public AjaxResult registerBySMS(String telephone, String validCode, Integer type, String name, String userHeadPortraitPath) {
		AjaxResult result = new AjaxResult();
		TCsqUser user = (TCsqUser) ConsumeHelper.getObj();
		try {
			log.info("手机号注册，telephone={}, validCode={}, type={}, name={}, userHeadPortraitPath={}", telephone, validCode, type, name, userHeadPortraitPath);
			Map<String, Object> resultMap = csqUserService.registerBySMS(telephone, validCode, type, user);
			result.setData(resultMap);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "手机号注册", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("手机号注册", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 验证码登录
	 *
	 * @param telephone 手机号
	 * @param validCode 验证码
	 * @param type      类型1个人2组织
	 * @param uuid      类型
	 * @return
	 */
	@RequestMapping("login/sms")
	public AjaxResult loginBySMS(String telephone, String validCode, Integer type,
								 @RequestParam(required = true) String uuid) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("验证码登录，telephone={}, validCode={}, type={}", telephone, validCode, type);
			Map<String, Object> resultMap = csqUserService.loginBySMS(uuid, telephone, validCode, type);
			result.setData(resultMap);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "验证码登录", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("验证码登录", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * openid登录
	 *
	 * @param openid        微信openid
	 * @param uuid          虚拟设备号
	 * @param encryptedData 微信加密数据
	 * @param iv            微信iv
	 * @param sessionKey    微信sessionKey
	 * @return
	 */
	@Consume(WechatPhoneAuthDto.class)
	@RequestMapping("login/openid")
	public AjaxResult openidLogin(String openid,
								  @RequestParam(required = true) String uuid,
								  @RequestParam(required = false) String specialGuest,
								  @RequestParam(required = false) String encryptedData,
								  @RequestParam(required = false) String iv,
								  @RequestParam(required = false) String sessionKey) {
		AjaxResult result = new AjaxResult();
		try {
			WechatPhoneAuthDto wechatPhoneAuthDto = (WechatPhoneAuthDto) ConsumeHelper.getObj();
			log.info("openid登录, openid={}, uuid={}, specialGuest={}", openid, uuid, specialGuest);
			Map<String, Object> resultMap = csqUserService.openidLogin(openid, uuid, wechatPhoneAuthDto);
			result.setData(resultMap);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "openid登录", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("openid登录", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 换绑手机号(更换新手机号阶段或者第一次绑定手机)
	 *
	 * @param telephone 手机号
	 * @param smsCode   验证码
	 *                  <p>
	 *                  {"success":false,"errorCode":"","msg":"该手机已绑定其他账号，如需解绑，请联系客服!","data":""}
	 * @return
	 */
	@RequestMapping("phone/bind")
	@UrlAuth
	public AjaxResult bindCellphone(String telephone, String smsCode) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("换绑手机号, telephone={}, smsCode={}", telephone, smsCode);
			csqUserService.bindCellPhone(userId, telephone, smsCode);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "换绑手机号", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("换绑手机号", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 发送短信验证码
	 *
	 * @param telephone 手机号
	 *                  <p>
	 *                  {
	 *                  "success": true,
	 *                  "errorCode": "",
	 *                  "msg": "",
	 *                  "data": ""
	 *                  }
	 * @return
	 */
	@PostMapping("generateSMS")
	@UrlAuth(withoutPermission = true)
	public AjaxResult generateSMS(String telephone) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("发送短信验证码, telephone={}", telephone);
			Integer application = ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode();
			if (userService.genrateSMSCode(telephone, application).isSuccess()) { // 生成并发送成功
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setErrorCode(AppErrorConstant.ERROR_SENDING_SMS);
				result.setMsg(AppErrorConstant.SMS_NOT_SEND_MESSAGE);
			}
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发送短信验证码", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发送短信验证码", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 校验短信验证码
	 *
	 * @param telephone 手机号
	 * @param smsCode   验证码
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("checkSMS")
	@UrlAuth(withoutPermission = true)
	public AjaxResult checkSMS(String telephone, String smsCode) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("校验短信验证码, telephone={}, smsCode={}", telephone, smsCode);
			userService.checkSMS(telephone, smsCode);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "校验短信验证码", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("校验短信验证码", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 手机号密码登录
	 *
	 * @param telephone 手机号
	 * @param password  密码
	 * @param option    选择个人或组织登录
	 * @param uuid      虚拟设备号
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":{"user":{"id":1292,"totalDonate":"","minutesAgo":"","userAccount":"","name":"9527","userTel":"17826879801","userHeadPortraitPath":"https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png","sex":0,"remarks":""},"token":"eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMzIiLCJwYXNzIjoiWElPQVNISV9BVVRIT1JJWkVfLTE0MDg5MDI5ODEiLCJpbmNyZWFzZUlkIjoxNzcsImlkIjoxMjkyLCJleHAiOjE1NjczMjcwODAsImlhdCI6MTU2MjE0MzA4MH0.yTmDc0sE2ec2ttwKODpxgWcAX4Ap1lwBFsWp9LH7uoo"}}
	 * @return
	 */
	@RequestMapping("login/telephone/pwd")
	public AjaxResult loginByTelephone(String telephone, String password, Integer option, String uuid) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("手机号密码登录, telephone={}, password={}, option={}", telephone, password, option);
			Map<String, Object> map = csqUserService.loginByTelephone(telephone, password, option, uuid);
			result.setData(map);
			result.setSuccess(true);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "手机号密码登录", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("手机号密码登录", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 注册
	 *
	 * @param telephone 手机号
	 * @param password  密码
	 * @param uuid      虚拟设备号
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("register/pwd")
	public AjaxResult rigesterByTelephone(String telephone, String password, String uuid) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("注册, telephone={}, password={}", telephone, password);
			csqUserService.register(telephone, password, uuid);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "注册", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("注册", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 每日一善
	 * <p>
	 * {
	 * "dayCnt": 0,	连续积善天数
	 * "dailyIncome": 0,	今日已筹金额
	 * "donateCnt": 0,	捐款人数
	 * "id": 1,
	 * "userId": 1292,
	 * "fundId": "",
	 * "donaterCnt": "",
	 * "donaters": "",
	 * "sumTotalOut": "",
	 * "trendPubValues": "",
	 * "csqUserPaymentRecords": "",
	 * "reports": "",
	 * "fundStatus": "",
	 * "type": 0,
	 * "typePubKeys": "",
	 * "name": "发布一个项目",	项目名字
	 * "recordId": "",
	 * "status": 0,
	 * "purpose": "",	简要描述(目的描述
	 * "sumTotalIn": 0,
	 * "totalInCnt": 0,
	 * "surplusAmount": 0,
	 * "expectedAmount": 0,	目标金额
	 * "expectedRemainAmount": 0,
	 * "startDate": "",
	 * "endDate": "",
	 * "coverPic": "",	封面图
	 * "description": "你认真的样子好像天桥底下贴膜的",
	 * "detailPic": "",
	 * "beneficiary": "",
	 * "creditCard": ""
	 * }
	 *
	 * @return
	 */
	@RequestMapping("daily/donate/detail")
	@UrlAuth
	public AjaxResult dailyDonateDetail() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("每日一善, userId={}, telephone={}, password={}, option={}", userId);
			CsqDailyDonateVo csqDailyDonateVo = csqUserService.dailyDonateDetail(userId);
			result.setData(csqDailyDonateVo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "每日一善", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("每日一善", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 爱心账户我的信息
	 * <p>
	 * {
	 * "sumTotalIn": 10000,	//基金累积收入
	 * "expected": 10000,	//基金期望收入
	 * "surplusAmount": 289,	//爱心账户余额
	 * "sumDonate": 3094,	//总捐助
	 * "status": 0	//基金状态0、1、3 未公开(筹备中) 2已公开
	 * }
	 *
	 * @return
	 */
	@RequestMapping("information")
	@UrlAuth
	public AjaxResult myInformation() {
		Long userId = IdUtil.getId();
		log.info("访问我的基本信息 ={}", userId);
		AjaxResult ajaxResult = new AjaxResult();
		try {
			Map<String, Object> map = csqUserService.findCsqUserById(userId);
			ajaxResult.setSuccess(true);
			ajaxResult.setData(map);
		} catch (MessageException e) {
			log.warn(e.getMessage());
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			log.error("查看我的信息出错id={}", userId);
			e.printStackTrace();
		}
		return ajaxResult;

	}

	/**
	 * 分享
	 *
	 * @param entityId 实体编号
	 * @param option   操作0个人1基金
	 * @return
	 */
	@RequestMapping("share")
	@UrlAuth
	public AjaxResult share(Long entityId, Integer option) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("分享, userId={}, entityId={}, option={}", userId, entityId, option);
			Map<String, Object> share = csqUserService.share(userId, entityId, option);
			result.setData(share);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "分享", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("分享", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 平台托管消费行为的记录
	 *
	 * @param fromId           来源编号
	 * @param fromType         来源类型
	 * @param amount           金额
	 * @param wholeDescription 完整描述
	 * @return
	 */
	@RequestMapping("recordConsumption")
	public AjaxResult recordForConsumption(Long fromId, Integer fromType, Double amount, String wholeDescription) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("平台托管消费行为的记录, userId={}, fromId={}, fromType={}, amount={}, wholeDescription={}", userId, fromId, fromType, amount, wholeDescription);
			csqUserService.recordForConsumption(userId, fromId, fromType, amount, wholeDescription);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "平台托管消费行为的记录", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("平台托管消费行为的记录", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 查看基本信息
	 * {
	 * "id":2000,
	 * "csqUserAuth":{
	 * "id":13,
	 * "userId":2000,
	 * "type":0,	类型
	 * "cardId":"331021199802140761",	身份证号
	 * "name":"张三",	姓名
	 * "phone":"17826879114",
	 * "licensePic":"",	执照图
	 * "licenseId":"",	执照号
	 * "status":1	状态
	 * },
	 * "totalDonate":"",
	 * "minutesAgo":"",
	 * "userAccount":"",
	 * "name":"你是什么垃圾",	姓名
	 * "userTel":"17826879888",	手机号
	 * "userHeadPortraitPath":"https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",	头像
	 * "sex":0,	性别
	 * "existDayCnt": 123 加入天数
	 * "balanceStatus":1 爱心账户状态 -1~1, 被禁止、待激活、可用
	 * "accountType": 1 账号类型 1~2 (个人，组织 etc.)
	 * "remarks":""	个人简介
	 * "contactPerson":"张学友"  联系人
	 * "contactNo":"11111"  联系方式
	 * }
	 *
	 * @return
	 */
	@RequestMapping("infos")
	@UrlAuth
	public AjaxResult infos() {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("查看基本信息, userId={}, name={}, remarks={}", userId);
			CsqBasicUserVo infos = csqUserService.infos(userId);
			result.setData(infos);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "查看基本信息", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查看基本信息", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 修改个人基本信息
	 *
	 * @param name                 姓名
	 * @param remarks              描述
	 * @param userHeadPortraitPath 头像
	 * @param weiboAccount         微博号
	 * @param wechatPubAccount     微信公众号
	 * @param contactPerson        联系人
	 * @param contactNo            联系方式
	 * @return
	 */
	@RequestMapping("modify")
	@Consume(CsqBasicUserVo.class)
	@UrlAuth
	public AjaxResult modify(String name, String remarks, String userHeadPortraitPath, String weiboAccount, String wechatPubAccount, String contactPerson, String contactNo) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		CsqBasicUserVo csqBasicUserVo = (CsqBasicUserVo) ConsumeHelper.getObj();
		try {
			log.info("修改个人基本信息, userId={}, name={}, remarks={}, userHeadPortraitPath={}, weiboAccount={}, wechatAccount={}, contactPerson={}, contactNo={}", userIds, name, remarks, userHeadPortraitPath, weiboAccount, wechatPubAccount, contactPerson, contactNo);
			csqUserService.modify(userIds, csqBasicUserVo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "修改个人基本信息", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改个人基本信息", e);
			result.setSuccess(false);
		}
		return result;
	}

	@RequestMapping("test/byId")
	public Object findUserById(Long userId) {
		return userDao.selectByPrimaryKey(userId);
	}

	/**
	 * 全局捐赠播报
	 * @return
	 */
	@RequestMapping("globle/donate/record")
	@UrlAuth(withoutPermission = true)
	public Object globleDonateRecord() {
		AjaxResult result = new AjaxResult();
		Long userIds = UserUtil.getTestId();
		try {
			log.info("全局捐赠播报, userId={}", userIds);
			List<CsqDonateRecordVo> objects = csqUserService.globleDonateRecord();
			result.setData(objects);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "全局捐赠播报", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("全局捐赠播报", e);
			result.setSuccess(false);
		}
		return result;
	}
}
