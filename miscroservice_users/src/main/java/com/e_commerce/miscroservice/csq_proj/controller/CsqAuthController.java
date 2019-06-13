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

/**
 * 从善桥实名认证Controller
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:33
 */
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
	@RequestMapping("send/person")
	public Object personAuth(String name, String cardId, String phone, String smsCode) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqUserAuth csqUserAuth = (TCsqUserAuth) ConsumeHelper.getObj();
		csqUserAuth.setUserId(userId);
		try {
			log.info("实名认证 - 个人, name={}, cardId={}, phone={}, smsCode={}", name, cardId, phone, smsCode);
			csqUserService.sendPersonAuth(csqUserAuth, smsCode);
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

	@RequestMapping("send/corp")
	public Object corpAuth() {
		return null;
	}
}
