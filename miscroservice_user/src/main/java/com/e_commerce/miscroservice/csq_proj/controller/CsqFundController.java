package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
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
public class CsqFundController {

	@Autowired
	private CsqFundService fundService;

	/**
	 * 申请前检查
	 *
	 * @return
	 */
	@RequestMapping("apply/check")
	@UrlAuth
	public AjaxResult beforeApplyForFund() {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
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
	@UrlAuth
	public AjaxResult applyForAFund(Long amount,
									@RequestParam(required = false) Long fundId,
									@RequestParam(required = false) String orderNo,
									@RequestParam(required = false) Long publishId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("申请基金, amount={},fundId={},orderNo={},publishId={}");
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
	 * @param id              基金编号
	 * @param trendPubKeys    趋势pubKeys
	 * @param name            名
	 * @param description     描述
	 * @param coverPic        封面图
	 * @param orgName         组织名
	 * @param orgAddr         组织地址
	 * @param contact         联系方式
	 * @param personInCharge  负责人
	 * @param creditCardName  银行名
	 * @param creditCardId    银行卡号
	 * @param status          基金状态(用于发起审核)
	 * @param agentModeStatus 代理状态
	 * @param detailPic	详情图
	 * @param purpose	目的描述(概述
	 * @param sharePic 分享图
	 * @return
	 */
	@RequestMapping("modify")
	@Consume(CsqFundVo.class)
	@UrlAuth
	public AjaxResult modifyMyFund(@RequestParam Long id, String trendPubKeys, String name,
								   String description, String coverPic, String orgName, String orgAddr, String detailPic,
								   String contact, String personInCharge, String creditCardName, String creditCardId,String purpose,
								   String sharePic,
								   @RequestParam(required = false) Integer status,
								   @RequestParam(required = false) Integer agentModeStatus) {
		AjaxResult result = new AjaxResult();
		CsqFundVo vo = (CsqFundVo) ConsumeHelper.getObj();
		TCsqFund csqFund = vo.copyTCsqFund();
		try {
			log.info("修改基金, fundId={}, trendPubKeys={}, name={}, description={}, coverPic={}, orgName={}, orgAddr={}, contact={}, personIncharge={}, creditCardName={}, creditCardId={}, detailPic={}, purpose={}",
				id, trendPubKeys, name, description, coverPic, orgName, orgAddr, contact, personInCharge, creditCardName, creditCardId, detailPic, purpose);
			fundService.modifyFund(vo.getUserId(), csqFund);
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
	@UrlAuth(withoutPermission = true)
	public AjaxResult certFund(Long fundId, Integer option) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
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
	 *               "contributeInCnt": 2,	捐款人数
	 *               "trendPubNames": [	 方向名
	 *               "北京方向",
	 *               "东京方向",
	 *               "南京方向",
	 *               "皇后大道"
	 *               ],
	 *               "goToList": [	捐献去向列表
	 *               {
	 *               "id": 18,
	 *               "createTime": 1561433484000,
	 *               "updateTime": 1563175155000,
	 *               "deletedFlag": false,
	 *               "extend": "",
	 *               "createUser": 132,
	 *               "updateUser": 123231,
	 *               "isValid": "1",
	 *               "userId": 2000,
	 *               "fromId": 6,
	 *               "toId": 20,
	 *               "date": "2019/07/15",
	 *               "serviceName": "",
	 *               "fromType": 3,
	 *               "toType": 4,
	 *               "orderNo": "15987",
	 *               "price": 128,
	 *               "status": 2,
	 *               "inVoiceStatus": 1,
	 *               "orderTime": "",
	 *               "cached": false,
	 *               "countColumn": "*",
	 *               "buildClass": "com.e_commerce.miscroservice.csq_proj.po.TCsqOrder"
	 *               }
	 *               ],
	 *               "mine": false 是否为我的基金
	 *               "raiseStatus": "",
	 *               "stationorgName": "",
	 *               "stationorgAddr": "",
	 *               "stationcontact": "",
	 *               "stationPersonIncharge": "",
	 *               "stationcreditCardName": "",
	 *               "stationcreditCardId": "",
	 *               "id": 6,
	 *               "userId": 2000,
	 *               "serviceId": 116,
	 *               "helpCnt": 31,
	 *               "totalInCnt": 0,
	 *               "trendPubKeys": "1,2,3,6",	方向编号
	 *               "name": "小宝绿色基金会所",  基金名
	 *               "description": "11111111111111",  描述
	 *               "coverPic": "https://timebank-test-img.oss-cn-hangzhou.aliyuncs.com/oneHour(v3.0)/otherImg/156335924596846.png",  封面图
	 *               "detailPic": "123"  详细图
	 *               "orgName": "",
	 *               "orgAddr": "",
	 *               "contact": "",
	 *               "personInCharge": "",	负责人
	 *               "creditCardName": "",
	 *               "creditCardId": "",
	 *               "balance": 76766,	余额
	 *               "sumTotalIn": 4444,  总收入
	 *               "agentModeStatus": 1,
	 *               "status": 0 基金状态0未公开1审核中2已公开3审核失败
	 *               }
	 * @return
	 */
	@RequestMapping("detail")
	@UrlAuth(withoutPermission = true)
	public AjaxResult FundDetail(Long fundId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("基金详情, fundId={}", fundId);
			CsqFundVo csqFundVo = fundService.fundDetail(userId, fundId);
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
	@UrlAuth
	public AjaxResult shareFund(Long fundId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
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
	 * @param option   操作(多选)0筹备中2进行中
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "contributeInCnt":"",	贡献人次
	 *                 "trendPubNames":"",	捐助方向
	 *                 "goToList":"",	资助列表
	 *                 "id":6,
	 *                 "userId":2000,
	 *                 "helpCnt":0,	累积资助项目次数
	 *                 "totalInCnt":0,	资金累积流入次数
	 *                 "trendPubKeys":"1,6",	关注方向
	 *                 "name":"发哥爱心事业基金会",	名
	 *                 "description":"",	描述
	 *                 "coverPic":"https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",	图片
	 *                 "orgName":"",	组织名
	 *                 "orgAddr":"",	组织地址
	 *                 "contact":"",	联系方式
	 *                 "personInCharge":"",	负责人
	 *                 "creditCardName":"",	银行名字
	 *                 "creditCardId":"",	银行卡号
	 *                 "balance":0,	余额
	 *                 "totalIn":10001,	总进账
	 *                 "agentModeStatus":0,	代理状态
	 *                 "status":0	状态
	 *                 }
	 *                 ],
	 *                 "totalCount":1
	 *                 }
	 * @return
	 */
	@RequestMapping("list")
	@UrlAuth
	public AjaxResult fundList(Integer pageNum, Integer pageSize, Integer... option) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
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

	/**
	 * 基金捐款项目列表
	 *
	 * @param fundId   项目编号
	 * @param pageNum  页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("donate/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult donateServiceList(Long fundId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("基金捐款项目列表, userId={}, funId={}, pageNum={}, pageSize={}", userId, fundId, pageNum, pageSize);
			QueryResult queryResult = fundService.donateServiceList(fundId, pageNum, pageSize);
			result.setData(queryResult);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "基金捐款项目列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("基金捐款项目列表", e);
			result.setSuccess(false);
		}
		return result;
	}

}
