package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
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
}
