package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import com.e_commerce.miscroservice.user.po.TUser;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:13
 */
public interface CsqOrderDao {
	
	TCsqOrder selectByUserIdAndFundId(Long userId, Long fundId);

	TCsqOrder selectByOrderNo(String orderNo);

	TCsqOrder selectByUserIdAndTypeAndAmountValid(Long userId, int code, Double amount);

	int update(TCsqOrder tCsqOrder);

	List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatus(Long userId, int toCode, int code);

	List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusDesc(Long userId, int toCode, int code);

	List<TCsqOrder> selectInOrderNos(String... orderNo);

	int update(List<TCsqOrder> toUpdateList);
}
