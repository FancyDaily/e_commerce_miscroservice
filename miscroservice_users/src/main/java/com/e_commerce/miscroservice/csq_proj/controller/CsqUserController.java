package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.CsqWechatConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
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

}
