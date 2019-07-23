package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import com.e_commerce.miscroservice.csq_proj.service.CsqInvoiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqInvoiceRecord;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqWaitToInvoiceOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发票
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:35
 */
@RestController
@Log
@RequestMapping("csq/invoice")
public class CsqInvoiceController {

	@Autowired
	private CsqInvoiceService invoiceService;

	/**
	 * 申请开票
	 *
	 * @param type      类型 0个人 1企业
	 * @param name      名字
	 * @param taxNo     税号
	 * @param addr      地址
	 * @param person    人
	 * @param telephone 手机号
	 * @param orderNo   订单号
	 *                  <p>
	 *                  {"success":false,"errorCode":"","msg":"所选订单总额未达到100.0元,无法开票!","data":""}
	 * @return
	 */
	@Consume(CsqUserInvoiceVo.class)
	@RequestMapping("submit")
	@UrlAuth
	public AjaxResult invoiceSubmit(Integer type, String name, String taxNo, String addr, String person, String telephone, String... orderNo) {
		AjaxResult result = new AjaxResult();
		CsqUserInvoiceVo vo = (CsqUserInvoiceVo) ConsumeHelper.getObj();
		TCsqUserInvoice tCsqUserInvoice = vo.copyTCsqUserInvoice();
		try {
			log.info("申请开票, orderNo={}, type={}, name={}, taxNo={}, addr={}, person={}, telephone={}", orderNo, type, name, taxNo, addr, person, telephone);
			invoiceService.submit(vo.getUserId(), tCsqUserInvoice, orderNo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "申请开票", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("申请开票", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 待开票列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 大小
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "id":7,
	 *                 "toId":15,	//项目编号
	 *                 "date":"2019/06/25",	//日期
	 *                 "serviceName":"张三爱心事业基金会",	//项目名称
	 *                 "toType":4,	//类型（项目/基金）
	 *                 "orderNo":"9801",	//订单好
	 *                 "price":548,	//项目金额
	 *                 "inVoiceStatus":0,	//申请状态
	 *                 }
	 *                 ],
	 *                 "totalCount":3
	 *                 }
	 * @return
	 */
	@RequestMapping("list/waitTo")
	@UrlAuth
	public AjaxResult invoiceWaitToList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("待开票列表, userId={}", userId);
			QueryResult<CsqWaitToInvoiceOrderVo> queryResult = invoiceService.waitToList(userId, pageNum, pageSize);
			result.setData(queryResult);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "待开票列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("待开票列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 已开票列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 大小
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "id":24,
	 *                 "userId":2000,
	 *                 "dateString":"2019/07/03",
	 *                 "recordCnt":"",	//发票对应项目个数
	 *                 "amount":555,	//发票面额
	 *                 "isOut":0,	//出票状态
	 *                 "orderNos":"4801,1324",	//对应项目订单号
	 *                 "type":1,	//类型0个人1企业
	 *                 "name":"张三",	//抬头
	 *                 "taxNo":"gazd123131",	//税号
	 *                 "addr":"地址这样",		//地址
	 *                 "person":"人",	//法人或个人
	 *                 "telephone":"131231313"	//手机号
	 *                 }
	 *                 ],
	 *                 "totalCount":1
	 *                 }
	 * @return
	 */
	@RequestMapping("list/done")
	@UrlAuth
	public AjaxResult invoiceDoneList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("已开票列表, userId={}, pageNum={}, pageSize={}", userId, pageNum, pageSize);
			QueryResult<CsqUserInvoiceVo> queryResult = invoiceService.doneList(userId, pageNum, pageSize);
			result.setData(queryResult);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "已开票列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("已开票列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 发票详情
	 *
	 * @param invoiceId 发票编号
	 *                  <p>
	 *                  <p>
	 *                  {
	 *                  "id":24,	//以下参数参考已开具发票列表
	 *                  "userId":2000,
	 *                  "dateString":"",
	 *                  "recordCnt":9,
	 *                  "amount":555,
	 *                  "isOut":0,
	 *                  "orderNos":"4801,1324",
	 *                  "type":1,
	 *                  "name":"张三",
	 *                  "taxNo":"gazd123131",
	 *                  "addr":"地址这样",
	 *                  "person":"人",
	 *                  "telephone":"131231313"
	 *                  }
	 * @return
	 */
	@RequestMapping("detail")
	@UrlAuth(withoutPermission = true)
	public AjaxResult invoiceDetail(Long invoiceId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("发票详情, userId={}, invoiceId={}", userId, invoiceId);
			CsqUserInvoiceVo csqUserInvoiceVo = invoiceService.invoiceDetail(userId, invoiceId);
			result.setData(csqUserInvoiceVo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发票详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发票详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 发票对应项目列表
	 *
	 * @param invoiceId 发票编号
	 *                  <p>
	 *                  {
	 *                  "resultList":[
	 *                  {
	 *                  "dateString":"2019/06/25",	//日期
	 *                  "myAmount":222,	//金额
	 *                  "orderNo":"4801",	//订单号
	 *                  "name":"测试标题"		//项目名
	 *                  }
	 *                  ],
	 *                  "totalCount":2
	 *                  }
	 * @return
	 */
	@RequestMapping("record/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult invoiceRecordList(Long invoiceId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("发票对应项目列表, userId={}, invoiceId={}", userId, invoiceId);
			QueryResult<CsqInvoiceRecord> queryResult = invoiceService.recordList(userId, invoiceId, pageNum, pageSize);
			result.setData(queryResult);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发票对应项目列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发票对应项目列表", e);
			result.setSuccess(false);
		}
		return result;
	}

}
