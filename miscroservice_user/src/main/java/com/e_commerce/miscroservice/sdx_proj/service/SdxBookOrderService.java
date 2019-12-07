package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookOrderUserInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxPurchaseOrderVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookDonateOrderVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookOrderVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface SdxBookOrderService {
    long modTSdxBookOrder(TSdxBookOrderPo tSdxBookOrderPo);
    int delTSdxBookOrderByIds(Long... ids);
    TSdxBookOrderVo findTSdxBookOrderById(Long id);
    List<TSdxBookOrderVo> findTSdxBookOrderByAll(TSdxBookOrderPo tSdxBookOrderPo, Integer page, Integer size);

	List<TSdxBookDonateOrderVo> donateOrdersList();

	List<SdxBookOrderUserInfoVo> getEverDonateList(Long bookInfoId);

	String buildDoneTimeDesc(long timeStamp);

	Map<String, String> preOrder(Long shippingAddressId, String bookInfoIds, Double bookFee, Long userId, HttpServletRequest httpservletRequest, Double shipFee, Integer scoreUsed) throws Exception;

	void dealWithBookPay(String out_trade_no, String attach);

	void confirmReceipt(Long orderId);

	SdxPurchaseOrderVo detail(Long orderId);

	QueryResult purchaseList(Long userIds, String options, Integer pageNum, Integer pageSize);

	QueryResult donateList(Long id, Integer option, Integer pageNum, Integer pageSize);

	String createDonateOrder(Long userId, Long[] bookInfoIds, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId, Integer status);

	String createDonateOrder(Long userId, Long[] bookInfoIds, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId);

	void cancel(Long orderId);

	Object preDonateOrder(Long id, Long[] bookInfoIds, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId, HttpServletRequest request) throws Exception;

	Map<String, Object> preOrderInfos(Long userId, Long shippingAddressId, String bookInfoIds);
}
