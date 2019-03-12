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
     * @param ids
     * @return
     */
    List<TUserCompany> queryByUserIdsDESC(Long... ids);

    /**
     * 根据用户id、组织内角色查询UserCompany记录
     * @param id
     * @param role
     * @return
     */
    List<TUserCompany> selectByUserIdAndCompanyjob(Long id, Integer role);

    /**
     * 根据用户id、组织id获取UserCompany记录
     * @param id
     * @param companyId
     * @return
     */
    List<TUserCompany> selectByUserIdAndCompanyId(Long id, Long companyId);

    /**
     * 更新
     * @param userCompany
     */
    int updateByPrimaryKey(TUserCompany userCompany);

    /**
     * 插入
     * @param userCompany
     */
    int insert(TUserCompany userCompany);
}
