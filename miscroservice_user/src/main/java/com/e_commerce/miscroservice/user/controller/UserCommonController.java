package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;
import com.e_commerce.miscroservice.commons.entity.colligate.MsgResult;
import com.e_commerce.miscroservice.commons.enums.application.GrowthValueEnum;
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
    public MsgResult freezeTimeCoin(Long userId, long freeTime, Long serviceId, String serviceName) {
        MsgResult result = new MsgResult();
        try {
            userService.freezeTimeCoin(userId, freeTime, serviceId, serviceName);
            result.setCode("200");
            result.setMessage("冻结金额成功");
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode("500");
            result.setMessage("冻结金额失败");
            return result;
        }
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

    /**
     * 批量任务完成(包含增加成长值、等级提升、根据用户等级修改用户授信额度、任务记录)
     * @param user
     * @param growthValueEnum
     */
    public void taskComplete(TUser user, GrowthValueEnum growthValueEnum, Integer counts) {
        userService.taskComplete(user,growthValueEnum,counts);
    }
}
