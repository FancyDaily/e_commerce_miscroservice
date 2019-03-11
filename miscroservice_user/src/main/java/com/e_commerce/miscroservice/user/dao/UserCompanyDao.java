package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserCompany;

import java.util.List;

public interface UserCompanyDao {
    /**
     * 根据用户id时间倒序查询UserCompany记录
     * @param userId
     * @return
     */
    List<TUserCompany> queryByUserIdDESC(Long userId);

    /**
     * 根据组织id获取UserCompany记录
     * @param companyId
     * @return
     */
    List<TUserCompany> selectByCompanyId(Long companyId);

    /**
     * 查询组织账号的UserCompany记录
     * @param companyId
     * @return
     */
    List<TUserCompany> findRecordOfCompanyAccount(Long companyId);

    /**
     * 根据用户id时间倒序查询UserCompany记录
     * @param userIds
     * @return
     */
    List<TUserCompany> queryByUserIdsDESC(Long... userIds);
}
