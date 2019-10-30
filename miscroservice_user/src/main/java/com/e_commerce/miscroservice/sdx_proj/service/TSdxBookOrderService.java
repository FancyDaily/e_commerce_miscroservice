package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookOrderUserInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookDonateOrderVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookOrderVo;
import java.util.List;
public interface TSdxBookOrderService {
    long modTSdxBookOrder(TSdxBookOrderPo tSdxBookOrderPo);
    int delTSdxBookOrderByIds(Long... ids);
    TSdxBookOrderVo findTSdxBookOrderById(Long id);
    List<TSdxBookOrderVo> findTSdxBookOrderByAll(TSdxBookOrderPo tSdxBookOrderPo, Integer page, Integer size);

	List<TSdxBookDonateOrderVo> donateOrdersList();

	List<SdxBookOrderUserInfoVo> getEverDonateList(Long bookInfoId);

	String buildDoneTimeDesc(long timeStamp);
}
