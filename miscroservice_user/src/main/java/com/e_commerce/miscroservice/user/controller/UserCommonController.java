package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCommonController {

    Log logger = Log.getInstance(UserCommonController.class);

    @Autowired
    private UserService userService;

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
}
