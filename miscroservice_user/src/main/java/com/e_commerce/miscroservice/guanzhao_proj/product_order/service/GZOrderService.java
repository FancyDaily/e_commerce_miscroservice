package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.OrderDetailVO;

public interface GZOrderService {
    /**
     * 查询我的订单
     * @param id
     * @param pageNumber
     * @param pageSize
     * @return
     */
    QueryResult<TGzOrder> findMyOrderList(Long id, Integer pageNumber, Integer pageSize);

    /**
     * 查询我的订单
     * @param orderId
     * @param userId
     * @return
     */
    OrderDetailVO findOrderDetailed(String orderId, Long userId);
}
