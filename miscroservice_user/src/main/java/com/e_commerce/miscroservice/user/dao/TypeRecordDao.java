package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TTypeRecord;

import java.util.List;

public interface TypeRecordDao {

    /**
     * 根据用户id、起止时间查询成长值记录
     * @param id
     * @param begin
     * @param end
     * @return
     */
    List<TTypeRecord> selectByUserIdBetween(Long id, Long begin, Long end);

    /**
     * 根据用户id、起止时间查找收入类型成长值记录
     * @param id
     * @param beginStamp
     * @param endStamp
     * @return
     */
    List<TTypeRecord> selectIncomeByUserIdBetween(Long id, Long beginStamp, Long endStamp);

    int insert(TTypeRecord record);

    /**
     * 根据类型查找成长值记录
     * @param code
     * @return
     */
    List<TTypeRecord> selectByType(int code);

    /**
     * 根据用户id、类型查找成长值记录
     * @param code
     * @param userId
     * @return
     */
    List<TTypeRecord> selectByTypeAndUserId(int code, Long userId);

    /**
     * 根据用户id、类型s查找成长值记录
     * @param code
     * @param code1
     * @param id
     * @return
     */
    List<TTypeRecord> selectByTypeAndUserId(int code, int code1, Long id);

    /**
     * 查找今日成长值记录
     * @param id
     * @return
     */
    List<TTypeRecord> selectDailyGrowthRecords(Long id);

    /**
     * 查找所有成长记录
     * @param id
     * @return
     */
    List<TTypeRecord> selectGrowthRecords(Long id);
}
