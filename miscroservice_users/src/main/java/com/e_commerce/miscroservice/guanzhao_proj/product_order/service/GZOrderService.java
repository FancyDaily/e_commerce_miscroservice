package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;

public interface GZOrderService {
    /**
     * 查询我的订单
     * @param id
     * @param pageNumber
     * @param pageSize
     * @return
     */
    QueryResult<TGzOrder> findMyOrderList(Integer id, Integer pageNumber, Integer pageSize);
}
