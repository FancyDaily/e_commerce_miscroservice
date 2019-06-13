package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基金Controller
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:33
 */
@RestController
@Log
public class FundController {

	@Autowired
	private CsqFundService fundService;

	/**
	 * 申请前检查
	 * @return
	 */
	@RequestMapping("fund/apply/check")
	public Object beforeApplyForFund() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			fundService.checkBeforeApplyForAFund(userId);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "申请前检查", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("申请前检查", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 申请基金
	 * @param amount
	 * @param orderNo
	 * @param publishId
	 * @return
	 */
	@RequestMapping("fund/apply")
	public Object applyForAFund(Long amount,
								@RequestParam(required = false) Long fundId,
								@RequestParam(required = false) String orderNo,
								@RequestParam(required = false) Long publishId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
//			fundService.applyForAFund(userId, fundId, amount, publishId, orderNo);
			fundService.applyForAFund(userId, orderNo);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "申请基金", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("申请基金", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 修改基金
	 * @param publishId
	 * @return
	 */
	@RequestMapping("fund/modify")
	@Consume(TCsqFund.class)
	public Object modifyMyFund(Long publishId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqFund fund = (TCsqFund) ConsumeHelper.getObj();
		try {
			fundService.modifyFund(fund);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "修改基金", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改基金", e);
			result.setSuccess(false);
		}
		return result;
	}


}
