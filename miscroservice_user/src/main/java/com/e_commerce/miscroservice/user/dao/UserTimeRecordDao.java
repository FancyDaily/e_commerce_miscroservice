package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
import com.e_commerce.miscroservice.commons.enums.application.PaymentEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserTimeRecordDao {
    List<TUserTimeRecord> selectMonthlyTimeRecord(Long userId, Long begin, Long end);

    Long insert(TUserTimeRecord record);

    TUserTimeRecord selectById(Long id);
    /**
     * 查找一个订单的所有流水
     * @param orderId
     * @return
     */
    List<TUserTimeRecord> selectGetTimeByOrder(Long orderId);

    /**
     * 根据用户id查找流水记录
     * @param userId
     * @return
     */
    List<TUserTimeRecord> selectTimeRecordByUserIdBetweenASC(Long userId,Long begin,Long end);

    /**
     * 根据用户id、订单ids查找所有流水记录
     * @param userId
     * @param orderIds
     * @return
     */
    List<TUserTimeRecord> selectByUserIdInOrderIds(Long userId, List<Long> orderIds);

    List<TUserTimeRecord> selectByUserId(Long id);

    List<TUserTimeRecord> selectByUserIdOrFromUserIdAndTypeBetween(Long userId, PaymentEnum paymentTypeAceptServ, long betLeft, long betRight);
}
