package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TTypeDictionaries;

import java.util.List;

public interface TypeDictionariesDao {
    /**
     * 查询邀请记录
     * @param mineId
     * @param inviterId
     * @return
     */
    List<TTypeDictionaries> selectInviteRecords(Long mineId, Long inviterId);

    /**
     * 插入记录
     * @param dictionaries
     * @return
     */
    int insert(TTypeDictionaries dictionaries);

    /**
     * 根据id、有效值查找订单记录
     * @param scene
     * @return
     */
    List<TTypeDictionaries> selectByIdAndIsValid(Long scene, String isValid);
}
