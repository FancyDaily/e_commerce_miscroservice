package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 从善桥实名认证Controller
 *
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
	 *
	 * @param name    名字
	 * @param cardId  身份证
	 * @param phone   手机号
	 * @param smsCode 验证码
	 *                <p>
	 *                {"success":false,"errorCode":"","msg":"您已经实名过，无需重复实名!","data":""}
	 * @return
	 */
	@Consume(CsqUserAuthVo.class)
	@RequestMapping("person/submit")
	@UrlAuth
	public AjaxResult personAuth(String name, String cardId, String phone, String smsCode) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		CsqUserAuthVo csqUserAuth = (CsqUserAuthVo) ConsumeHelper.getObj();
		csqUserAuth.setUserId(userId);
		TCsqUserAuth userAuth = csqUserAuth.copyTCsqUserAuth();
		try {
			log.info("实名认证 - 个人, name={}, cardId={}, phone={}, smsCode={}", name, cardId, phone, smsCode);
			csqUserService.sendPersonAuth(userAuth, smsCode);
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
	 *
	 * @param telephone  手机号
	 * @param password   密码
	 * @param validCode  验证码
	 * @param name       名字
	 * @param licenseId  营业执照
	 * @param licensePic 营业执照图片
	 *                   <p>
	 *                   {"success":false,"errorCode":"","msg":"短信验证码已过期！","data":""}
	 * @return
	 */
	@Consume(CsqUserAuthVo.class)
	@RequestMapping("corp/submit")
	public AjaxResult certCorpSubmit(@RequestParam(required = false) String telephone,
									 @RequestParam(required = false) String password,
									 @RequestParam(required = false) String validCode,
									 @RequestParam(required = false) String name,
									 @RequestParam(required = false) String userHeadPortraitPath,
									 @RequestParam(required = false) String licenseId,
									 @RequestParam String licensePic,
									 @RequestParam String uuid) {
		AjaxResult result = new AjaxResult();
		CsqUserAuthVo csqUserAuth = (CsqUserAuthVo) ConsumeHelper.getObj();
		TCsqUserAuth userAuth = csqUserAuth.copyTCsqUserAuth();
		try {
			log.info("组织注册与实名提交, telephone={}, password={}, validCode={}, name={}, userHeadPortraitPath={}, licenseId={}, licensePic={}"
				, telephone, password, validCode, name, userHeadPortraitPath, licenseId, licensePic);
			Map<String, Object> map = csqUserService.registerAndSubmitCert(telephone, validCode, uuid, userAuth, name, userHeadPortraitPath);
			result.setData(map);
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
	 *
	 * @param userAuthId 认证记录编号
	 * @param option     操作1通过2拒绝
	 *                   <p>
	 *                   {"success":true,"errorCode":"","msg":"","data":""}
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
	 *
	 * @param userAuthId 认证记录编号
	 * @param option     操作1通过2拒绝
	 *                   <p>
	 *                   {"success":true,"errorCode":"","msg":"","data":""}
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
	 * <p>
	 * {"success":true,"errorCode":"","msg":"","data":""}
	 *
	 * @return
	 */
	@RequestMapping("check")
	@UrlAuth
	public AjaxResult authCheck() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
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

	/**
	 * 实名状态获取
	 * <p>
	 * {
	 * "isCorp": true,  当前是否为组织(影响stauts的含义)
	 * "status": -1  实名状态:个人0未实名1已实名、组织-1未实名0审核中1通过2拒绝
	 * }
	 *
	 * @return
	 */
	@RequestMapping("status")
	@UrlAuth
	public AjaxResult authStatus() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			Map<String, Object> resultMap = csqUserService.getAuthStatus(userId);
			result.setData(resultMap);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "实名状态获取", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("实名状态获取", e);
			result.setSuccess(false);
		}
		return result;
	}

}
