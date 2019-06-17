package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.service.CsqFundService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 基金Controller
 * TodoList: 审核、详情、列表、提审(包含基金修改里面)
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
			boolean b = fundService.checkBeforeApplyForAFund(userId);
			result.setData(b);
			result.setSuccess(true);
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
			log.info("申请基金, amount={},fundId={},orderNo={},publishId={}");
//			fundService.applyForAFund(userId, fundId, amount, publishId, orderNo);
			fundService.applyForAFund(userId, orderNo);
			result.setSuccess(true);
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
	 * @param fundId
	 * @param publishId
	 * @return
	 */
	@RequestMapping("fund/modify")
	@Consume(TCsqFund.class)
	public Object modifyMyFund(@RequestParam Long fundId, Long publishId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqFund fund = (TCsqFund) ConsumeHelper.getObj();
		try {
			log.info("修改基金, publishId={}", publishId);
			fundService.modifyFund(userId ,fund);
			result.setSuccess(true);
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

	/**
	 * 审核 - 基金公开
	 * @param fundId
	 * @return
	 */
	@RequestMapping("fund/cert")
	public Object certFund(Long fundId, Integer option) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("审核 - 基金公开, fundId={}, option={}", fundId, option);
			fundService.certFund(userId, fundId, option);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "审核 - 基金公开", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("审核 - 基金公开", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 基金详情
	 * @param fundId
	 * @return
	 */
	@RequestMapping("fund/detail")
	public Object FundDetail(Long fundId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("基金详情, fundId={}", fundId);
			CsqFundVo csqFundVo = fundService.fundDetail(fundId);
			result.setData(csqFundVo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "基金详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("基金详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 分享基金
	 * @param fundId
	 * @return
	 */
	@RequestMapping("fund/share")
	public Object shareFund(Long fundId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			Map<String, Object> shareMap = fundService.share(userId, fundId);
			result.setData(shareMap);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "分享基金", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("分享基金", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 基金列表
	 * @param pageNum
	 * @param pageSize
	 * @param option
	 * @return
	 */
	@RequestMapping("fund/list")
	public Object fundList(Integer pageNum, Integer pageSize,Integer... option) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			fundService.list(userId, pageNum, pageSize, option);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "分享基金", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("分享基金", e);
			result.setSuccess(false);
		}
		return result;
	}

}
