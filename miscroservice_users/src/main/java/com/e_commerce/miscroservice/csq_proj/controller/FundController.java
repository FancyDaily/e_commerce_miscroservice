package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
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
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:33
 */
@RestController
@RequestMapping("csq/fund")
@Log
public class FundController {

	@Autowired
	private CsqFundService fundService;

	/**
	 * 申请前检查
	 *
	 * @return
	 */
	@RequestMapping("apply/check")
	public AjaxResult beforeApplyForFund() {
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
	 *
	 * @param amount    金额
	 * @param orderNo   订单号
	 * @param publishId 标签对应publishId
	 * @return
	 */
	@RequestMapping("apply/do")
	public AjaxResult applyForAFund(Long amount,
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
	 *
	 * @param id             基金编号
	 * @param trendPubKeys   趋势pubKeys
	 * @param name           名
	 * @param description    描述
	 * @param coverPic       封面图
	 * @param orgName        组织名
	 * @param orgAddr        组织地址
	 * @param contact        联系方式
	 * @param personInCharge 负责人
	 * @param creditCardName 银行名
	 * @param creditCardId   银行卡号
	 * @return
	 */
	@RequestMapping("modify")
	@Consume(CsqFundVo.class)
	public AjaxResult modifyMyFund(@RequestParam Long id, String trendPubKeys, String name,
								   String description, String coverPic, String orgName, String orgAddr,
								   String contact, String personInCharge, String creditCardName, String creditCardId,
								   @RequestParam(required = false) Integer status) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		CsqFundVo vo = (CsqFundVo) ConsumeHelper.getObj();
		TCsqFund csqFund = vo.copyTCsqFund();
		try {
			log.info("修改基金, fundId={}, trendPubKeys={}, name={}, description={}, coverPic={}, orgName={}, orgAddr={}, contact={}, personIncharge={}, creditCardName={}, creditCardId={}",
				id, trendPubKeys, name, description, coverPic, orgName, orgAddr, contact, personInCharge, creditCardName, creditCardId);
			fundService.modifyFund(userId, csqFund);
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
	 *
	 * @param fundId 基金编号
	 * @param option 操作
	 *               <p>
	 *               {"success":false,"errorCode":"","msg":"错误的审核前状态!当前基金无法审核!","data":""}
	 * @return
	 */
	@RequestMapping("cert")
	public AjaxResult certFund(Long fundId, Integer option) {
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
	 *
	 * @param fundId 基金编号
	 *               <p>
	 *               {
	 *               "contributeInCnt":0,
	 *               "trendPubNames":[
	 *               "北京方向",
	 *               "皇后大道东方向"
	 *               ],
	 *               "goToList":"",
	 *               "id":6,
	 *               "userId":2000,
	 *               "totalToItemCnt":"",
	 *               "helpCnt":0,
	 *               "totalInCnt":0,
	 *               "trendPubKeys":"1,6",
	 *               "name":"发哥爱心事业基金会",
	 *               "description":"",
	 *               "pic":"https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *               "orgName":"",
	 *               "orgAddr":"",
	 *               "contact":"",
	 *               "personInCharge":"",
	 *               "cardName":"",
	 *               "cardId":"",
	 *               "balance":0,
	 *               "totalIn":10001,
	 *               "agentModeStatus":0,
	 *               "status":0
	 *               }
	 * @return
	 */
	@RequestMapping("detail")
	public AjaxResult FundDetail(Long fundId) {
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
	 *
	 * @param fundId 基金编号
	 * @return
	 */
	@RequestMapping("share")
	public AjaxResult shareFund(Long fundId) {
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
	 *
	 * @param pageNum  页码
	 * @param pageSize 分页大小
	 * @param option   操作
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "contributeInCnt":"",
	 *                 "trendPubNames":"",
	 *                 "goToList":"",
	 *                 "id":6,
	 *                 "userId":2000,
	 *                 "totalToItemCnt":"",
	 *                 "helpCnt":0,
	 *                 "totalInCnt":0,
	 *                 "trendPubKeys":"1,6",
	 *                 "name":"发哥爱心事业基金会",
	 *                 "description":"",
	 *                 "pic":"https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                 "orgName":"",
	 *                 "orgAddr":"",
	 *                 "contact":"",
	 *                 "personInCharge":"",
	 *                 "cardName":"",
	 *                 "cardId":"",
	 *                 "balance":0,
	 *                 "totalIn":10001,
	 *                 "agentModeStatus":0,
	 *                 "status":0
	 *                 }
	 *                 ],
	 *                 "totalCount":1
	 *                 }
	 * @return
	 */
	@RequestMapping("list")
	public AjaxResult fundList(Integer pageNum, Integer pageSize, Integer... option) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			QueryResult<CsqFundVo> list = fundService.list(userId, pageNum, pageSize, option);
			result.setData(list);
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
	 * 测试用插入
	 *
	 * @return
	 */
	@RequestMapping("testInsert")
	public AjaxResult testInsert() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("测试用插入, userId={}", userId);
			fundService.insertForSomeOne(userId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "测试用插入", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("测试用插入", e);
			result.setSuccess(false);
		}
		return result;
	}

}
