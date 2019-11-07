package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookOrderUserInfoVo;
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
}
