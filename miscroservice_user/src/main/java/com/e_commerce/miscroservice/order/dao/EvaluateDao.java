package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.commons.entity.application.TEvaluate;

import java.util.List;

public interface EvaluateDao {
    /**
     * 根据订单id集合查找评价列表
     * @param orderIds
     * @return
     */
    List<TEvaluate> selectEvaluateInOrderIds(List orderIds);

    /**
     * 根据订单id集合、用户id查找评价列表
     * @param orderIds
     * @param userId
     * @return
     */
    List<TEvaluate> selectEvaluateInOrderIdsAndByUserId(List orderIds, Long userId);
}
