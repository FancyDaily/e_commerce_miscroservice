package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat.WeChatPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付Controller
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:40
 */
@RequestMapping("csq/pay")
@RestController
@Log
public class CsqPayController {

	@Autowired
	private CsqPayService csqPayService;

	@Autowired
	private WeChatPay weChatPay;

	/**
	 * 微信支付(发起)
	 * @return
	 */
	@RequestMapping("preOrder")
	public Object preOrder(String orderNo,
						   @RequestParam(required = true) Long entityId,
						   @RequestParam(required = true) Integer entityType,
						   @RequestParam(required = true) Double fee,
						   HttpServletRequest httpServletRequest) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("微信支付(发起), userId={}, orderNo={}, entityId={}, entityType={}, fee={}", orderNo, entityId, entityType, fee);
			csqPayService.preOrder(userId, orderNo, entityId, entityType, fee, httpServletRequest);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "微信支付(发起)", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("微信支付(发起)", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 微信回调函数
	 * @return
	 */
	@RequestMapping("wxNotify")
	public Object wxNotify(HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		try {
			csqPayService.wxNotify(request);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "微信回调函数", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("微信回调函数", e);
			result.setSuccess(false);
		}
		return result;
	}

}
