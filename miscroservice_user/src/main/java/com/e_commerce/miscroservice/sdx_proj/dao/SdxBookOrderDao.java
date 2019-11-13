package com.e_commerce.miscroservice.sdx_proj.dao;

import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;

import java.util.List;

public interface SdxBookOrderDao {
    int saveTSdxBookOrderIfNotExist(TSdxBookOrderPo tSdxBookOrderPo);
    int modTSdxBookOrder(TSdxBookOrderPo tSdxBookOrderPo);
    int delTSdxBookOrderByIds(Long... ids);
    TSdxBookOrderPo findTSdxBookOrderById(Long id);
    List<TSdxBookOrderPo> findTSdxBookOrderByAll(TSdxBookOrderPo tSdxBookOrderPo, Integer page, Integer size);

	List<TSdxBookOrderPo> selectByTypeAndStatus(int code, int code1);

	List<TSdxBookOrderPo> selectByBookInfoIdAndTypeAndStatus(Long id, int code, int code1);

	List<TSdxBookOrderPo> selectByBookInfoIdAndTypeAndStatus(Long bookInfoId, int type, int status, Page page);

	List<TSdxBookOrderPo> selectByBookInfoIdAndTypeAndStatus(Long bookInfoId, int type, int status, Page page, MybatisPlusBuild.OrderBuild... orderBuild);

	TSdxBookOrderPo selectByShippingAddressIdAndBookInfoIdsAndBookFeeAndUserIdAndShipFee(Long shippingAddressId, String bookInfoIds, Double bookFee, Long userId, Double shipFee, int code, int type);

	TSdxBookOrderPo selectByOrderNo(String out_trade_no);

	TSdxBookOrderPo selectByPrimaryKey(Long orderId);

	List<TSdxBookOrderPo> purchaseList(Long userIds, Integer option, Integer pageNum, Integer pageSize);

	List<TSdxBookOrderPo> donateList(Long userIds, Integer option, Integer pageNum, Integer pageSize);
}
