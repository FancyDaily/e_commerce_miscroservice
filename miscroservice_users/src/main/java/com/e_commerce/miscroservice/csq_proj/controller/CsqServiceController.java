package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceDetailVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceReportVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceListVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import com.e_commerce.miscroservice.csq_proj.service.CsqServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	 * @param name           项目名
	 * @param recordNo       备案编号
	 * @param typePubKeys    项目类型
	 * @param purpose        目的描述
	 * @param expectedAmount 期望金额
	 * @param coverPic       封面图
	 * @param description    描述
	 * @param detailPic      内容图
	 * @param beneficiary    受益人
	 * @param certificatedNo 身份证/机构代码
	 * @param creditCard     银行卡号
	 * @param personInCharge 负责人
	 *                       <p>
	 *                       {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("publish")
	@Consume(CsqServiceDetailVo.class)
	public AjaxResult publishService(String name,
									 String recordNo,
									 String typePubKeys,
									 String purpose,
									 Double expectedAmount,
									 String coverPic,
									 String description,
									 String detailPic,
									 String beneficiary,
									 String certificatedNo,
									 String creditCard,
									 String personInCharge) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		CsqServiceListVo vo = (CsqServiceListVo) ConsumeHelper.getObj();
		TCsqService csqService = vo.copyTCsqService();
		try {
			log.info("发布项目, name={}, recordNo={}, typePubKeys={}, " +
				"purpose={}, expectedAmount={}, coverPic={}, description={}, " +
				"detailPic={}, beneficiary={}, certificatedNo={}, creditCard={}, personInCharge={}",
				name, recordNo, typePubKeys, purpose, expectedAmount, coverPic, description, detailPic, beneficiary, certificatedNo, creditCard, personInCharge);
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
	 * @param option   操作 0全部 1已捐助 2我发布
	 * @param pageNum  页码
	 * @param pageSize 大小
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "id":20,
	 *                 "userId":2000,
	 *                 "fundId":"",
	 *                 "sumTotalPayMine":0,	我捐助了多少
	 *                 "donaterCnt":"",	贡献者数量
	 *                 "trendPubValues":"",	趋向值
	 *                 "fundStatus":"",	基金状态
	 *                 "type":0,	类型
	 *                 "typePubKeys":"",	类型值
	 *                 "name":"这是项目的名字",	名字
	 *                 "recordId":"",	备案编号
	 *                 "status":0,	状态
	 *                 "purpose":"",	目的
	 *                 "sumTotalIn":0,	总计收入
	 *                 "totalInCnt":0,	总计捐助次数
	 *                 "surplusAmount":0,	剩余账户余额
	 *                 "expectedAmount":0,	期待金额
	 *                 "expectedRemainAmount":0,	剩余期待金额
	 *                 "startDate":"",	开始日期
	 *                 "endDate":"",	结束日期
	 *                 "coverPic":"",	封面图
	 *                 "description":"这是一段详细描述"	描述
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
	 *                  {
	 *                  "serviceVo":{
	 *                  "raiseStatus":1,
	 *                  "id":2,
	 *                  "userId":1292,
	 *                  "fundId":"",
	 *                  "sumTotalPayMine":"",
	 *                  "donaterCnt":"",
	 *                  "trendPubValues":"",
	 *                  "fundStatus":"",
	 *                  "type":0,
	 *                  "typePubKeys":"",
	 *                  "name":"发布一个项目",	//项目名
	 *                  "recordId":"",	//备案号
	 *                  "status":0,	//状态
	 *                  "purpose":"",	//目的描述
	 *                  "sumTotalIn":0,	//已筹得金额
	 *                  "totalInCnt":0,	//捐款人次
	 *                  "surplusAmount":0,	//项目账户余额
	 *                  "expectedAmount":0,	//期望金额
	 *                  "expectedRemainAmount":0,	//剩余期望金额
	 *                  "startDate":"",
	 *                  "endDate":"",
	 *                  "coverPic":"",	//封面图
	 *                  "description":"你认真的样子好像天桥底下贴膜的",	//描述
	 *                  "donaters":[
	 *                  <p>
	 *                  ],	//捐助人列表
	 *                  "sumTotalOut":0,	//总支出
	 *                  "csqUserPaymentRecordVos":[
	 *                  <p>
	 *                  ],	//流水列表
	 *                  "csqServiceReportVos":[
	 *                  {
	 *                  "id":4,
	 *                  "serviceId":2,
	 *                  "title":"这是项目汇报的标题",
	 *                  "description":"",
	 *                  "pic":"1313dada3"
	 *                  }
	 *                  ],	//项目汇报列表
	 *                  "detailPic":"",	//详情图片
	 *                  "beneficiary":"",	//负责人
	 *                  "creditCard":""	//银行卡
	 *                  },
	 *                  "broadCast":[
	 *                  <p>
	 *                  ],
	 *                  "isMine":false,	//是否是我发布的
	 *                  "isFund":false	//是否为基金
	 *                  }
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
	public AjaxResult billOut(@RequestParam Long serviceId, Integer pageNum, Integer pageSize) {
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

	/**
	 * 项目汇报列表
	 *
	 * @param serviceId 项目编号
	 * @param pageNum   页码
	 * @param pageSize  大小
	 * @return
	 */
	@RequestMapping("reportList")
	public AjaxResult reportList(Long serviceId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("项目汇报列表, serviceId={}, pageNum={}, pageSize={}", serviceId, pageNum, pageSize);
			QueryResult<CsqServiceReportVo> queryResult = csqServiceService.reportList(serviceId, pageNum, pageSize);
			result.setData(queryResult);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目汇报列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目汇报列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 捐助记录列表
	 *
	 * @param serviceId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("donate/list")
	public AjaxResult donateList(Long serviceId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("捐助记录列表, userId={}, serviceId={}, pageNum={}, pageSize={}", userId, serviceId, pageNum, pageSize);
			QueryResult queryResult = csqServiceService.donateList(serviceId, pageNum, pageSize);
			result.setData(queryResult);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "捐助记录列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("捐助记录列表", e);
			result.setSuccess(false);
		}
		return result;
	}
}
