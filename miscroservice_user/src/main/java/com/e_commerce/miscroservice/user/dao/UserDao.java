package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.user.ClientPlatform;
import com.e_commerce.miscroservice.commons.entity.user.UserLoginLog;
import com.e_commerce.miscroservice.commons.enums.user.UserType;
import com.e_commerce.miscroservice.user.entity.LiveUser;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.po.UserPO;

/**
 * 用户Dao层
 */
public interface UserDao {
    /**
     * 根据手机查找用户
     *
     * @param userPhone 用户手机
     * @return
     */
    UserPO getUserByUserPhone(String userPhone);


    /**
     * 根据用户手机号查询 门店账户
     * @param phone
     * @return
     */
    StoreBusiness getStoreBusinessByPhone(String phone);

    /**
     * 添加 门店账户
     * @param user
     */
    void addStoreBusiness(StoreBusiness user);
    /**
     * 更新 门店账户
     * @param user
     */
    void updateStoreBusiness(StoreBusiness user);

    /**
     * 用户登陆日志
     * @param userLoginLog
     */
    void addUserLoginLog(UserLoginLog userLoginLog);

    /**
     * 根据用户微信认证号查询
     * @param unionid
     * @return
     */
    StoreBusiness getStoreBusinessByWeixinId(String unionid);

    /**
     * 更新老的微信信息
     * @param id
     * @param unionid
     * @param nickname
     * @param headimgurl
     * @return
     */
    int oldUserBindWeixin(Long id, String unionid, String nickname, String headimgurl);

    /**
     * 添加微信信息绑定手机号
     * @param phone
     * @param phone1
     * @param client
     * @param unionid
     * @param weiXinNickName
     * @param headimgurl
     */

    long addUserWeixinAndPhone(String phone, UserType phone1, ClientPlatform client, String unionid, String weiXinNickName, String headimgurl);


}
