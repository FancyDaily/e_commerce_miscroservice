package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import com.e_commerce.miscroservice.csq_proj.vo.CsqInvoiceVo;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 14:42
 */
public interface CsqInvoiceService {

	void submit(Long userId, TCsqUserInvoice userInvoice, String...orderNo);

	QueryResult<CsqInvoiceVo> waitToList(Long userId, Integer pageNum, Integer pageSize);

	QueryResult<CsqUserInvoiceVo> doneList(Long userId, Integer pageNum, Integer pageSize);

	CsqUserInvoiceVo invoiceDetail(Long userId, Long invoiceId);

	QueryResult<CsqInvoiceVo> recordList(Long userId, Long invoiceId, Integer pageNum, Integer pageSize);
}
