package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceReportVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceListVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
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
	 * <p>
	 * {
	 * "success": false,
	 * "errorCode": "",
	 * "msg": "非机构账户不能发布项目",
	 * "data": ""
	 * }
	 *
	 * @return
	 */
	@RequestMapping("checkAuth")
	public AjaxResult checkAuth() {
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
	 *
	 * @param description 描述
	 * @param name        名字
	 *                    <p>
	 *                    {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("publish")
	@Consume(CsqServiceListVo.class)
	public AjaxResult publishService(String description, String name) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		CsqServiceListVo vo = (CsqServiceListVo) ConsumeHelper.getObj();
		TCsqService csqService = vo.copyTCsqService();
		try {
			log.info("发布项目, description={}, name={}", description, name);
			csqServiceService.publish(userId, csqService);
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
	 *
	 * @param option   操作
	 * @param pageNum  页码
	 * @param pageSize 大小
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "id":20,
	 *                 "userId":2000,
	 *                 "fundId":"",
	 *                 "sumTotalPayMine":0,
	 *                 "donaterCnt":"",
	 *                 "donaters":"",
	 *                 "sumTotalOut":"",
	 *                 "trendPubValues":"",
	 *                 "csqUserPaymentRecordVos":"",
	 *                 "csqServiceReportVos":"",
	 *                 "fundStatus":"",
	 *                 "type":0,
	 *                 "typePubKeys":"",
	 *                 "name":"这是项目的名字",
	 *                 "recordId":"",
	 *                 "status":0,
	 *                 "purpose":"",
	 *                 "sumTotalIn":0,
	 *                 "totalInCnt":0,
	 *                 "surplusAmount":0,
	 *                 "expectedAmount":0,
	 *                 "expectedRemainAmount":0,
	 *                 "startDate":"",
	 *                 "endDate":"",
	 *                 "coverPic":"",
	 *                 "description":"这是一段详细描述",
	 *                 "detailPic":"",
	 *                 "beneficiary":"",
	 *                 "creditCard":""
	 *                 }
	 *                 ],
	 *                 "totalCount":4
	 *                 }
	 * @return
	 */
	@RequestMapping("list")
	public AjaxResult listService(Integer option, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("项目列表, option={}, pageNum={}, pageSize={}", option, pageNum, pageSize);
			QueryResult<CsqServiceListVo> list = csqServiceService.list(userId, option, pageNum, pageSize);
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
	 *
	 * @param serviceId 项目编号
	 *                  <p>
	 * {
	 *         "csqService":{
	 *             "id":20,
	 *             "userId":2000,
	 *             "fundId":"",
	 *             "sumTotalPayMine":"",
	 *             "donaterCnt":"",
	 *             "trendPubValues":"",
	 *             "fundStatus":"",
	 *             "type":0,
	 *             "typePubKeys":"",
	 *             "name":"这是项目的名字",
	 *             "recordId":"",
	 *             "status":0,
	 *             "purpose":"",
	 *             "sumTotalIn":0,
	 *             "totalInCnt":0,
	 *             "surplusAmount":0,
	 *             "expectedAmount":0,
	 *             "expectedRemainAmount":0,
	 *             "startDate":"",
	 *             "endDate":"",
	 *             "coverPic":"",
	 *             "description":"这是一段详细描述",
	 *             "donaters":[
	 *
	 *             ],
	 *             "sumTotalOut":0,
	 *             "csqUserPaymentRecordVos":[
	 *
	 *             ],
	 *             "csqServiceReportVos":[
	 *
	 *             ],
	 *             "detailPic":"",
	 *             "beneficiary":"",
	 *             "creditCard":""
	 *         },
	 *         "broadCast":[
	 *
	 *         ],
	 *         "isMine":true,
	 *         "isFund":false
	 *     }
	 * @return
	 */
	@RequestMapping("detail")
	public AjaxResult serviceDetail(Long serviceId) {
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
	 *
	 * @param serviceId 项目编号
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("cert")
	public AjaxResult serviceCert(Long serviceId) {
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
	 *
	 * @param serviceId 项目编号
	 * @param pageNum   页码
	 * @param pageSize  大小
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":{"resultList":[],"totalCount":0}}
	 * @return
	 */
	@RequestMapping("bill/out")
	public AjaxResult billOut(Long serviceId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("项目支出明细, serviceId={}", serviceId);
			QueryResult<CsqUserPaymentRecordVo> tCsqUserPaymentRecords = csqServiceService.billOut(userId, serviceId, pageNum, pageSize);
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
	 *
	 * @param serviceId   项目编号
	 * @param title       标题
	 * @param description 内容
	 * @param pic         图片
	 *                    <p>
	 *                    {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@Consume(CsqServiceReportVo.class)
	@RequestMapping("report/submit")
	public AjaxResult reportSubmit(Long serviceId, String title, String description, String pic) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		CsqServiceReportVo vo = (CsqServiceReportVo) ConsumeHelper.getObj();
		TCsqServiceReport tCsqServiceReport = vo.copyTCsqServiceReport();
		try {
			log.info("发布项目汇报, serviceId={}, title={}, content={}, pic={}", serviceId, title, description, pic);
			csqServiceService.submitReport(userId, serviceId, tCsqServiceReport);
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
	 *
	 * @param orderNo 订单号
	 * @return
	 */
	@RequestMapping("donate")
	public AjaxResult donate(String orderNo) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
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
