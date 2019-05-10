package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserTimeRecordDao {
    /**
     * 查找月度流水记录
     * @param userId
     * @param begin
     * @param end
     * @return
     */
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

    List<TUserTimeRecord> selectDailyByUserIdInOrderIds(Long userId, List<Long> orderIds);

    List<TUserTimeRecord> selectByUserId(Long id);

    /**
     * 查找
     * @param userId 用户id
     * @param paymentType 支付枚举类型
     * @param betLeft 区间-起
     * @param betRight 区间-止
     * @return
     */
    List<TUserTimeRecord> selectByUserIdOrFromUserIdAndTypeBetween(Long userId, int paymentType, long betLeft, long betRight);
}
