package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 从善桥实名认证Controller
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:33
 */
@RequestMapping("csq/auth")
@RestController
@Log
public class CsqAuthController {

	@Autowired
	private CsqUserService csqUserService;

	/**
	 * 实名认证 - 个人
	 * @param name
	 * @param cardId
	 * @param phone
	 * @param smsCode
	 * @return
	 */
	@Consume(TCsqUserAuth.class)
	@RequestMapping("person/submit")
	public AjaxResult personAuth(String name, String cardId, String phone, String smsCode) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId(2000L);
		TCsqUserAuth csqUserAuth = (TCsqUserAuth) ConsumeHelper.getObj();
		csqUserAuth.setUserId(userId);
		try {
			log.info("实名认证 - 个人, name={}, cardId={}, phone={}, smsCode={}", name, cardId, phone, smsCode);
			csqUserService.sendPersonAuth(csqUserAuth, smsCode);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "实名认证 - 个人", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("实名认证 - 个人", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 组织注册与实名提交
	 * @param telephone
	 * @param password
	 * @param validCode
	 * @param name
	 * @param licenseId
	 * @param licensePic
	 * @return
	 */
	@Consume(TCsqUserAuth.class)
	@RequestMapping("corp/submit")
	public AjaxResult certCorpSubmit(String telephone, String password, String validCode, String name, String licenseId, String licensePic) {
		AjaxResult result = new AjaxResult();
		TCsqUserAuth csqUserAuth = (TCsqUserAuth) ConsumeHelper.getObj();
		try {
			csqUserService.registerAndSubmitCert(telephone, password, validCode, csqUserAuth);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "组织注册与实名提交", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("组织注册与实名提交", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 组织实名认证审核
	 * @param userAuthId
	 * @param option
	 * @return
	 */
	@RequestMapping("corp/do")
	public AjaxResult certCorpDo(Long userAuthId, Integer option) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("组织实名认证审核, ");
			csqUserService.certCorp(userAuthId, option);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "组织实名认证审核", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("组织实名认证审核", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 个人实名认证审核
	 * @param userAuthId
	 * @param option
	 * @return
	 */
	@RequestMapping("person/do")
	public AjaxResult certPersonDo(Long userAuthId, Integer option) {
		AjaxResult result = new AjaxResult();
		try {
			csqUserService.certPerson(userAuthId, option);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "个人实名认证审核", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("个人实名认证审核", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 实名状态校验
	 * @return
	 */
	@RequestMapping("check")
	public AjaxResult authCheck() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId(2000L);
		try {
			csqUserService.checkAuth(userId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "实名状态校验", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("实名状态校验", e);
			result.setSuccess(false);
		}
		return result;
	}

}
