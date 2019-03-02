package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.user.ClientPlatform;
import com.e_commerce.miscroservice.commons.entity.user.UserLoginLog;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.user.UserType;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.entity.LiveUser;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.mapper.StoreBusinessMapper;
import com.e_commerce.miscroservice.user.mapper.UserMapper;
import com.e_commerce.miscroservice.user.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * user Dao层实现类
 */
@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;

    @Override
    public UserPO getUserByUserPhone(String userPhone) {
        BasePage.setPage(1);
        return userMapper.findUserOneByUserPhone(userPhone);
    }
    /**
     * 根据用户手机号查询 门店账户
     * @param phone
     * @return
     */
    @Override
    public StoreBusiness getStoreBusinessByPhone(String phone) {
//        StoreBusiness storeBusiness = MybatisOperaterUtil.getInstance().findOne(new StoreBusiness(),new MybatisSqlWhereBuild(StoreBusiness.class).eq(StoreBusiness::getPhoneNumber,phone));
        StoreBusinessVo storeBusiness = new StoreBusinessVo ();
        storeBusiness.setPhoneNumber(phone);
        StoreBusiness returnBusiness = storeBusinessMapper.selectOne(storeBusiness);
        return returnBusiness;
    }

    /**
     * 添加 门店账户
     *
     * @param user
     */
    @Override
    public void addStoreBusiness(StoreBusiness user) {
        storeBusinessMapper.insertSelective(user);
    }

    /**
     * 更新 门店账户
     *
     * @param user
     */
    @Override
    public void updateStoreBusiness(StoreBusiness user) {
        storeBusinessMapper.updateByPrimaryKeySelective(user);
    }
    /**
     * 用户登陆日志
     * @param userLoginLog
     */
    @Override
    public void addUserLoginLog(UserLoginLog userLoginLog) {
        userMapper.addUserLoginLog(userLoginLog);
    }

    /**
     * 根据用户微信认证号查询
     *
     * @param unionid
     * @return
     */
    @Override
    public StoreBusiness getStoreBusinessByWeixinId(String unionid) {
        StoreBusinessVo storeBusiness = new StoreBusinessVo ();
        storeBusiness.setBindWeixinId(unionid);
        return storeBusinessMapper.selectOne(storeBusiness);
    }

    /**
     * 更新老的微信信息
     * @param id
     * @param unionid
     * @param nickname
     * @param headimgurl
     * @return
     */
    @Override
    public int oldUserBindWeixin(Long id, String unionid, String nickname, String headimgurl) {
        long time = System.currentTimeMillis();
        StoreBusinessVo storeBusiness = new StoreBusinessVo ();
        storeBusiness.setUpdateTime(time);
        storeBusiness.setBindWeixinIcon(headimgurl);
        storeBusiness.setBindWeixinName(nickname);
        storeBusiness.setBindWeixinId(unionid);
        storeBusiness.setId(id);

        return storeBusinessMapper.oldUserBindWeixin(storeBusiness);
    }
    /**
     * 添加微信信息绑定手机号
     * @param phone
     * @param userType
     * @param client
     * @param unionid
     * @param weiXinNickName
     * @param headimgurl
     */
    @Override
    public long addUserWeixinAndPhone(String phone, UserType userType, ClientPlatform client, String unionid, String weiXinNickName, String headimgurl) {

        long time = System.currentTimeMillis();
        StoreBusiness storeBusiness = new StoreBusiness ();
        storeBusiness.setCreateTime(time);
        storeBusiness.setUpdateTime(time);
        storeBusiness.setBindWeixinIcon(headimgurl);
        storeBusiness.setBindWeixinName(weiXinNickName);
        storeBusiness.setBindWeixinId(unionid);
        storeBusiness.setUserPassword("");
        storeBusiness.setUserName(unionid);
        storeBusiness.setPhoneNumber(phone);
        storeBusiness.setActiveTime(0L);
        storeBusiness.setStatus(0);
        storeBusiness.setDistributionStatus(0);
        storeBusiness.setBankCardFlag(0);
        storeBusiness.setAlipayFlag(0);
        storeBusiness.setWeixinFlag(0);
        storeBusiness.setBusinessType(0);
//		userMapper.addStoreBusiness(storeBusiness);
        storeBusinessMapper.insertSelective(storeBusiness);
        storeBusiness.setBusinessNumber(storeBusiness.getId() + 800000000);
        storeBusiness.setUpdateTime(System.currentTimeMillis());
        storeBusinessMapper.updateByPrimaryKeySelective(storeBusiness);
//		userMapper.updateUserBusinessNumber(storeBusiness.getId(), storeBusiness.getId() + 800000000);
        long storeId = storeBusiness.getId();
        // 添加绑定日志
        // addUserBindPhoneLog(phone, userId);

        return storeId;
    }



}
