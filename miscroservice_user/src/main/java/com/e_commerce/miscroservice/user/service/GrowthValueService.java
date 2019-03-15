package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TTypeRecord;
import com.e_commerce.miscroservice.commons.entity.application.TUser;

import java.util.List;
import java.util.Map;

public interface GrowthValueService {

    /**
     * 成长值记录明细
     * @param user
     * @param ymString
     * @param option
     * @return
     */
    Map<String, Object> scoreList(TUser user, String ymString, String option);

    /**
     * 增加成长值
     * @param user
     * @param type
     * @return
     */
    TUser addGrowthValue(TUser user, int type);

    /**
     * 查询今日成长流水
     * @param id
     */
    List<TTypeRecord> findOnesDailyGrowthRecords(Long id);

    /**
     * 查询所有成长流水
     * @param id
     * @return
     */
    List<TTypeRecord> findOnesGrowthRecords(Long id);
}
