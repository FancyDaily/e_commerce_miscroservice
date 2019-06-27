package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.service.CsqServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目Controller
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:37
 */
@RequestMapping("csq/service")
@RestController
@Log
public class CsqServiceController {

	@Autowired
	private CsqServiceService csqServiceService;

	/**
	 * 检查发布权限
	 * @return
	 */
	@RequestMapping("checkAuth")
	public Object checkAuth() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("检查发布权限, userId={}", userId);
			csqServiceService.checkPubAuth(userId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "检查发布权限", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("检查发布权限", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 发布项目
	 * @param description
	 * @return
	 */
	@RequestMapping("publish")
	@Consume(TCsqService.class)
	public Object publishService(String description, String name) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqService service = (TCsqService) ConsumeHelper.getObj();
		try {
			log.info("发布项目, description={}, name={}", description, name);
			csqServiceService.publish(userId, service);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发布项目", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发布项目", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 项目列表
	 * @param option 选项
	 * @return
	 */
	@RequestMapping("list")
	public Object listService(Integer option, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("项目列表, option={}, pageNum={}, pageSize={}", option, pageNum, pageSize);
			QueryResult<TCsqService> list = csqServiceService.list(userId, option, pageNum, pageSize);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 项目详情
	 * @param serviceId
	 * @return
	 */
	@RequestMapping("detail")
	public Object serviceDetail(Long serviceId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("项目详情, serviceId={}", serviceId);
			Map<String, Object> detail = csqServiceService.detail(userId, serviceId);
			result.setData(detail);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 项目审核
	 * @param serviceId
	 * @return
	 */
	@RequestMapping("cert")
	public Object serviceCert(Long serviceId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("项目审核, serviceId={}", serviceId);
			csqServiceService.cert(userId, serviceId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目审核", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目审核", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 项目支出明细
	 * @param serviceId
	 * @return
	 */
	@RequestMapping("bill/out")
	public Object billOut(Long serviceId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("项目支出明细, serviceId={}", serviceId);
			QueryResult<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqServiceService.billOut(userId, serviceId, pageNum, pageSize);
			result.setData(tCsqUserPaymentRecords);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目支出明细", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目支出明细", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 发布项目汇报
	 * @param serviceId
	 * @param title
	 * @param content
	 * @param pic
	 * @return
	 */
	@Consume(TCsqServiceReport.class)
	@RequestMapping("report/submit")
	public Object reportSubmit(Long serviceId, String title, String content, String pic) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqServiceReport serviceReport = (TCsqServiceReport) ConsumeHelper.getObj();
		try {
			log.info("发布项目汇报, serviceId={}, title={}, content={}, pic={}", serviceId, title, content, pic);
			csqServiceService.submitReport(userId, serviceId, serviceReport);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发布项目汇报", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发布项目汇报", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 捐助成功(后续支付完成统一加入到回调函数中
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("donate")
	public Object donate(String orderNo) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId(2000L);
		try {
			log.info("捐助成功, orderNo={}", orderNo);
			csqServiceService.donate(orderNo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "捐助成功", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("捐助成功", e);
			result.setSuccess(false);
		}
		return result;
	}

}
