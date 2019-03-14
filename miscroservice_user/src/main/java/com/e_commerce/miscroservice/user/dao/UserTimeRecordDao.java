package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserTimeRecordDao {
    List<TUserTimeRecord> selectMonthlyTimeRecord(Long userId, Long begin, Long end);

    long insert(TUserTimeRecord record);
}
