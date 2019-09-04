package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.vo.CsqWaitToInvoiceOrderVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import com.e_commerce.miscroservice.csq_proj.vo.CsqInvoiceRecord;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 14:42
 */
public interface CsqInvoiceService {

	void submit(Long userId, TCsqUserInvoice userInvoice, String...orderNo);

	QueryResult<CsqWaitToInvoiceOrderVo> waitToList(Long userId, Integer pageNum, Integer pageSize);

	QueryResult<CsqUserInvoiceVo> doneList(Long userId, Integer pageNum, Integer pageSize);

	CsqUserInvoiceVo invoiceDetail(Long userId, Long invoiceId);

	QueryResult<CsqInvoiceRecord> recordList(Long userId, Long invoiceId, Integer pageNum, Integer pageSize);

	int express(Long invoiceId, String expressNo);

	QueryResult<CsqUserInvoiceVo> list(String searchParam, Integer isOut, Integer pageNum, Integer pageSize);

	void modify(TCsqUserInvoice obj);
}
