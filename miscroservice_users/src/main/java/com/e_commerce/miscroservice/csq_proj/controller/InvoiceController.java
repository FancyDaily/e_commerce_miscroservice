package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import com.e_commerce.miscroservice.csq_proj.service.CsqInvoiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqInvoiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发票
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:35
 */
@RestController
@Log
@RequestMapping("csq/invoice")
public class InvoiceController {

	@Autowired
	private CsqInvoiceService invoiceService;

	/**
	 * 申请开票
	 * @param type 类型
	 * @param name 名字
	 * @param taxNo 税号
	 * @param addr 地址
	 * @param person 人
	 * @param telephone 手机号
	 * @param orderNo 订单号
	 * @return
	 */
	@Consume(TCsqUserInvoice.class)
	@RequestMapping("submit")
	public AjaxResult invoiceSubmit(Integer type, String name, String taxNo, String addr, String person, String telephone, String... orderNo) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqUserInvoice userInvoice = (TCsqUserInvoice) ConsumeHelper.getObj();
		try {
			log.info("申请开票, orderNo={}, type={}, name={}, taxNo={}, addr={}, person={}, telephone={}", orderNo, type, name, taxNo, addr, person, telephone);
			invoiceService.submit(userId, userInvoice, orderNo);
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
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list/waitTo")
	public AjaxResult invoiceWaitToList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqUserInvoice userInvoice = (TCsqUserInvoice) ConsumeHelper.getObj();
		try {
			log.info("待开票列表, userId={}", userId);
			QueryResult<CsqInvoiceVo> queryResult = invoiceService.waitToList(userId, pageNum, pageSize);
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
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list/done")
	public AjaxResult invoiceDoneList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		TCsqUserInvoice userInvoice = (TCsqUserInvoice) ConsumeHelper.getObj();
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
	 * @param invoiceId 发票编号
	 * @return
	 */
	@RequestMapping("detail")
	public AjaxResult invoiceDetail(Long invoiceId) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
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
	 * @param invoiceId 发票编号
	 * @return
	 */
	@RequestMapping("record/list")
	public AjaxResult invoiceRecordList(Long invoiceId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("发票对应项目列表, userId={}, invoiceId={}", userId, invoiceId);
			QueryResult<CsqInvoiceVo> queryResult = invoiceService.recordList(userId, invoiceId, pageNum, pageSize);
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
