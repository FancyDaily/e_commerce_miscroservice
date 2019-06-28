package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

	/**
	 * 微信支付(发起)
	 * @return
	 */
	@Consume(TCsqFund.class)
	@RequestMapping("preOrder")
	public Object preOrder(String orderNo,
						   @RequestParam(required = true) Long entityId,
						   @RequestParam(required = true) Integer entityType,
						   @RequestParam(required = true) Double fee,
						   @RequestParam(required = true) String name,
						   @RequestParam(required = false) String trendPubKeys,
						   HttpServletRequest httpServletRequest) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqFund csqFund = (TCsqFund) ConsumeHelper.getObj();
		try {
			log.info("微信支付(发起), userId={}, orderNo={}, entityId={}, entityType={}, fee={}", orderNo, entityId, entityType, fee);
			Map<String, String> stringStringMap = csqPayService.preOrder(userId, orderNo, entityId, entityType, fee, httpServletRequest, csqFund);
			result.setData(stringStringMap);
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
	 * 微信回调函数 - 支付
	 * @return
	 */
	@RequestMapping("wxNotify/pay")
	public Object wxNotify(HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		try {
			csqPayService.wxNotify(request, true);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "微信回调函数 - 支付", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("微信回调函数 - 支付", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 微信退款发起
	 * @return
	 */
	@RequestMapping("refund/pre")
	public Object askForRefund(@RequestParam(required = true) String orderNo,
							   HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			csqPayService.preRefund(userId, orderNo, request);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "微信退款发起", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("微信退款发起", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 微信回调函数 - 退款
	 * @param request
	 * @return
	 */
	@RequestMapping("wxNotify/refund")
	public Object wxnotifyRefund(HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		try {
			csqPayService.wxNotify(request, false);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "微信回调函数 - 退款", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("微信回调函数 - 退款", e);
			result.setSuccess(false);
		}
		return result;
	}

}
