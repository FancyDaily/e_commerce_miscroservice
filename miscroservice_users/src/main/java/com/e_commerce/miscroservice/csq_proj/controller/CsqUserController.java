package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.CsqWechatConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 从善桥用户Controller
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

	/**
	 * 与微信校验授权
	 *
	 * @param code
	 * @return
	 */
	@RequestMapping("checkAuth")
	public Object checkAuth(String code) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("与微信校验授权，code={}");
			result.setSuccess(true);
			result.setData(wechatService.checkAuthCode(code, CsqWechatConstant.APP_ID, CsqWechatConstant.APP_SECRET));
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
	 * openid登录
	 * @param openid
	 * @param uuid
	 * @param specialGuest
	 * @return
	 */
	@RequestMapping("login/openid")
	public Object openidLogin(String openid,
							  @RequestParam(required = true) String uuid,
							  @RequestParam(required = true) String specialGuest) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("openid登录, openid={}, uuid={}, specialGuest={}", openid, uuid, specialGuest);
			Map<String, Object> resultMap = csqUserService.openidLogin(openid, uuid);
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
	 * @param telephone
	 * @return
	 */
	@RequestMapping("phone/bind")
	public Object bindCellphone(String telephone, String smsCode) {
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
	 * @param telephone   手机号
	 *                    <p>
	 *                    {
	 *                    "success": true,
	 *                    "errorCode": "",
	 *                    "msg": "",
	 *                    "data": ""
	 *                    }
	 * @return
	 */
	@PostMapping({"generateSMS", "generateSMS/" + TokenUtil.AUTH_SUFFIX})
	public Object generateSMS(String telephone) {
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
	 * @param telephone
	 * @param smsCode
	 * @return
	 */
	@RequestMapping("checkSMS")
	public Object checkSMS(String telephone, String smsCode) {
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
	 * @param telephone
	 * @param password
	 * @param option 选择个人或组织登录
	 * @return
	 */
	@RequestMapping("login/telephone/pwd")
	public Object loginByTelephone(String telephone, String password, Integer option, String uuid) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("手机号密码登录, telephone={}, password={}, option={}", telephone, password, option);
			csqUserService.loginByTelephone(telephone, password, option, uuid);
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
	 * @param telephone
	 * @param password
	 * @param uuid
	 * @return
	 */
	@RequestMapping("register")
	public Object rigesterByTelephone(String telephone, String password, String uuid) {
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
	 * @return
	 */
	public Object dailyDonateDetail() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("每日一善, userId={}, telephone={}, password={}, option={}", userId);
			csqUserService.dailyDonateDetail(userId);
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
	 * @return
	 */
	@RequestMapping("information")
	public Object myInformation(){
		Long userId = Long.valueOf(IdUtil.getId());
		log.info("访问我的基本信息 ={}",userId);
		AjaxResult ajaxResult = new AjaxResult();
		try {
			Map<String,Object> map = csqUserService.findCsqUserById(userId);
			ajaxResult.setSuccess(true);
			ajaxResult.setData(map);

		}catch (MessageException e){
			log.warn(e.getMessage());
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg(e.getMessage());
		}catch (Exception e){
			ajaxResult.setSuccess(false);
			log.error("查看我的信息出错id={}",userId);
		}
		return ajaxResult;

	}

}
