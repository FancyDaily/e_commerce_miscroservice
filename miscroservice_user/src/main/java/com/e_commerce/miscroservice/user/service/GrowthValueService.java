package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;

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
     * @param inviter
     * @param code
     * @return
     */
    TUser addGrowthValue(TUser inviter, int code);
}
