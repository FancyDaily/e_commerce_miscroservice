package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.dao.UserFreezeDao;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User共用Controller
 * userController
 */
@Component
public class UserCommonController {


    Log logger = Log.getInstance(UserCommonController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserFreezeDao userFreezeDao;

    /**
     * 根据id获取用户
     * @param userId
     * @return
     */
    public TUser getUserById(Long userId) {
        return userService.getUserbyId(userId);
    }

    /**
     * 冻结用户金额
     * @param userId 用户ID
     * @param freeTime 将要冻结金额
     * @param serviceId 商品ID
     * @param serviceName 商品名称
     */
    public boolean freezeTimeCoin(Long userId, long freeTime, Long serviceId, String serviceName) {
        try {
            userService.freezeTimeCoin(userId, freeTime, serviceId, serviceName);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据userId集合差找用户id
     * @param userIds
     * @return
     */
    public List selectUserByIds(List userIds) {
        return userDao.queryByIds(userIds);
    }

    /**
     * 根据userId更新用户
     * @param user
     * @return
     */
    public int updateByPrimaryKey(TUser user) {
        return userDao.updateByPrimaryKey(user);
    }

    /**
     * 根据userId、orderId查找冻结记录
     * @param userId
     * @param orderId
     * @return
     */
    public TUserFreeze selectUserFreezeByUserIdAndOrderId(Long userId,Long orderId) {
        return userFreezeDao.selectUserFreezeByUserIdAndOrderId(userId,orderId);
    }

    /**
     * 更新冻结记录
     * @param userFreeze
     * @return
     */
    public int updateUserFreeze(TUserFreeze userFreeze) {
        return userFreezeDao.update(userFreeze);
    }


}
