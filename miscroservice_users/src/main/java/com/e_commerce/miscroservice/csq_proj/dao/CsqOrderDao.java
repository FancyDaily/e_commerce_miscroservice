package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:13
 */
public interface CsqOrderDao {
	
	TCsqOrder selectByOrderNo(String orderNo);

	int update(TCsqOrder tCsqOrder);

	List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatus(Long userId, int toCode, int code);

	List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusDesc(Long userId, int toCode, int code);

	List<TCsqOrder> selectInOrderNos(String... orderNo);

	int update(List<TCsqOrder> toUpdateList);

	List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIds(Long fundId, int fromType, int toType, List<Long> orderIds);

	List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIdsAndStatus(Long fundId, int toCode, int toCode1, List<Long> tOrderIds, int code);

	List<TCsqOrder> selectByUserIdAndToTypeDesc(Long userId, int toCode);

	List<TCsqOrder> selectByToIdAndToTypeAndUpdateTimeBetweenDesc(Long toId, int toCode, long startStamp, long endStamp);

	TCsqOrder selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(Long userId, Long userId1, int toCode, Long fundId, int toCode1, Double amount, Integer status);

	List<TCsqOrder> selectByUserIdAndFromTypeAndToTypeInvoiceStatusDesc(Long userId, int toCode, int toCode1, int code);
}