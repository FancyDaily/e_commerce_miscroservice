package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;

import java.util.List;

public interface GZOrderDao {
    /**
     * 查询我的订单
     * @param id
     * @return
     */
    List<TGzOrder> findMyOrderList(Integer id);

    List<TGzOrder> selectByUserIdAndSubjectIdAndStatusCreateTimeDesc(Long userId, Long subjectId, int i);

    /**
     * 保存订单
     * @param payPo
     */
    void saveOrder(TGzOrder payPo);
}
