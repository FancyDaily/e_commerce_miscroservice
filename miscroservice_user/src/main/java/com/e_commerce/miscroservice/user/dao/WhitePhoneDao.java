package com.e_commerce.miscroservice.user.dao;

/**
 * Create by hyf on 2018/11/6
 */
public interface WhitePhoneDao {

    /**
     * 查询白名单
     * @param phone
     * @return
     */
    int getWhitePhone(String phone);
}
