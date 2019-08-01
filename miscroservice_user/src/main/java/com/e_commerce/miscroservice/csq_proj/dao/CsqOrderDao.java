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

	int update(List<TCsqOrder> toUpdateList, List<Long> toUpdateIds);

	List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIds(Long fundId, int fromType, int toType, List<Long> orderIds);

	List<TCsqOrder> selectByFromIdAndFromTypeAndToTypeInOrderIdsAndStatus(Long fundId, int toCode, int toCode1, List<Long> tOrderIds, int code);

	List<TCsqOrder> selectByUserIdAndToTypeDesc(Long userId, int toCode);

	List<TCsqOrder> selectByToIdAndToTypeAndOrderTimeBetweenDesc(Long toId, int toCode, long startStamp, long endStamp);

	TCsqOrder selectByUserIdAndFromIdAndFromTypeAndToIdAndToTypeAndAmountAndStatusDesc(Long userId, Long userId1, int toCode, Long fundId, int toCode1, Double amount, Integer status);

	List<TCsqOrder> selectByUserIdAndFromTypeAndToTypeInvoiceStatusAndStatusDesc(Long userId, int toCode, int toCode1, int code);

	List<TCsqOrder> selectByUserIdAndFromTypeAndToTypeInvoiceStatusAndStatusDesc(Long userId, int fromType, int toType, int invoiceStatus, int status);

	List<TCsqOrder> selectByToIdAndToTypeAndStatusDesc(Long entityId, int toCode, int status);

	int insert(TCsqOrder csqOrder);

	List<TCsqOrder> selectByUserIdAndToTypeInToIdAndStatus(Long userId, int toCode, int toCode1, int code);

	List<TCsqOrder> selectByUserIdInToIdAndStatus(Long userId, List<Long> toIds, int code);

	List<TCsqOrder> selectInIds(List<Long> orderIds);

	List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusAndStatusDesc(Long userId, int toCode, int code, int code1);

	List<TCsqOrder> selectByToIdAndToTypeAndStatusAndOrderTimeBetweenDesc(Long toId, int toType, int status, long startStamp, long endStamp);

	List<TCsqOrder> selectByUserIdAndToTypeAndToIdDesc(Long userId, int toCode, Long serviceId);

	List<TCsqOrder> selectByUserIdDesc(Long userId);

	List<TCsqOrder> selectByUserIdInToTypeDesc(Long userId, int toCode, int toCode1);

	List<TCsqOrder> selectByFromIdAndFromTypeInOrderIdsAndStatus(Long fundId, int toCode, List<Long> tOrderIds, Integer code);

	TCsqOrder selectByPrimaryKey(Long orderId);

	List<TCsqOrder> selectByUserIdAndFromTypeAndInvoiceStatusAndStatusDescPage(Integer pageNum, Integer pageSize, Long userId, int toCode, Integer code, Integer code1);

	List<TCsqOrder> selectInOrderNosPage(String[] split, Integer pageNum, Integer pageSize);

	List<TCsqOrder> selectByUserIdInToTypeDescPage(Integer pageNum, Integer pageSize, Long userId, int toCode, int toCode1);
}
