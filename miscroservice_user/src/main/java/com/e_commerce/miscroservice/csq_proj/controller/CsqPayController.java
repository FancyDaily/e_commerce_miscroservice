package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付Controller
 *
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
	 * 微信支付、正元支付(发起)
	 *
	 * @param orderNo      订单号
	 * @param entityId     被充值对象编号
	 * @param entityType   被充值对象类型2爱心账户3基金4项目
	 * @param fee          费用
	 * @param name         基金名
	 * @param trendPubKeys 趋向
	 * @param isAnonymous 是否匿名
	 * @param isActivity 是否活动相关0否1是
	 * @param isYunmaPay 是否为正元支付
	 * @param yunmaId 正元支付userId
	 * @return
	 */
	@Consume(CsqFundVo.class)
	@RequestMapping("preOrder")
	@UrlAuth
	public Object preOrder(@RequestParam(required = false) String orderNo,
						   @RequestParam(required = true) Long entityId,
						   @RequestParam(required = true) Integer entityType,
						   @RequestParam(required = true) Double fee,
						   @RequestParam(required = false) String name,
						   @RequestParam(required = false) String trendPubKeys,
						   @RequestParam(required = false) boolean isAnonymous,
						   @RequestParam(required = false) Integer isActivity,
						   @RequestParam(required = false) boolean isYunmaPay,
						   @RequestParam(required = false) Long yunmaId,
						   HttpServletRequest httpServletRequest) {
		AjaxResult result = new AjaxResult();
		CsqFundVo vo = (CsqFundVo) ConsumeHelper.getObj();
		TCsqFund csqFund = vo.copyTCsqFund();
		try {
			log.info("微信支付(发起), userId={}, orderNo={}, entityId={}, entityType={}, fee={}, name={}, trendPubKeys={}, isActivity={}, isYunmaPay={}, yunmaId={}", vo.getUserId(), orderNo, entityId, entityType, fee, name, trendPubKeys, isActivity, isYunmaPay, yunmaId);
			entityId = entityId == -1 ? null : entityId;
			Map<String, String> stringStringMap = csqPayService.preOrder(vo.getUserId(), orderNo, entityId, entityType, fee, httpServletRequest, csqFund, isAnonymous, isActivity, isYunmaPay, yunmaId);
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
	 *
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
	 * 正元回调函数 - 支付
	 *
	 * @return
	 */
	@RequestMapping("yunmaNotify/pay")
	public Object yunmaNotify(HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		try {
			csqPayService.yunmaNotify(request, true);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "正元回调函数 - 支付", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("正元回调函数 - 支付", e);
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 微信退款发起 -> TODO
	 *
	 * @param orderNo 订单号
	 * @return
	 */
	@RequestMapping("refund/pre")
	@UrlAuth
	public Object askForRefund(String orderNo,
							   HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
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
	 * 微信回调函数 - 退款 -> TODO
	 *
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

	/**
	 * 平台内充值/捐助
	 *
	 * @param fromType     来源类型
	 * @param fromId       来源编号
	 * @param entityType   去向类型
	 * @param entityId     去向编号
	 * @param fee          金额
	 * @param name         基金名字
	 * @param trendPubKeys 捐助方向
	 * @param isAnonymous 是否匿名
	 * @param isActivity 是否活动相关0否1是
	 * @return
	 */
	@Consume(CsqFundVo.class)
	@RequestMapping("pay/inner")
	@UrlAuth
	public AjaxResult withinPlatformPay(Integer fromType,
										Long fromId,
										Integer entityType,
										Long entityId,
										Double fee,
										String name,
										String trendPubKeys,
										boolean isAnonymous,
										Integer isActivity) {
		AjaxResult result = new AjaxResult();
		CsqFundVo vo = (CsqFundVo) ConsumeHelper.getObj();
		TCsqFund csqFund = vo.copyTCsqFund();
		try {
			log.info("平台内充值/捐助, userId={}, fromType={}, fromId={}, entityType={}, entityId={}, fee={}, name={}, trendPubKeys={}, isAnonymous={}, isActivity={}", vo.getUserId(), fromType, fromId, entityType, entityId, fee, name, trendPubKeys, isAnonymous, isActivity);
			entityId = entityId == -1 ? null : entityId;
			String orderNo = csqPayService.withinPlatFormPay(vo.getUserId(), fromType, fromId, entityType, entityId, fee, csqFund, isAnonymous, isActivity);
			result.setData(orderNo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "平台内充值/捐助", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("平台内充值/捐助", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 测试回调
	 *
	 * @param orderNo 订单号
	 * @param attach  附加字段
	 * @return
	 */
	@RequestMapping("test/notiffy")
	public AjaxResult testNotify(String orderNo, String attach) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("测试回调, userId={}, orderNo={}, attach={}", userId, orderNo, attach);
			csqPayService.testDealWithOrderNoPay(orderNo, attach);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "测试回调", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("测试回调", e);
			result.setSuccess(false);
		}
		return result;
	}

}
