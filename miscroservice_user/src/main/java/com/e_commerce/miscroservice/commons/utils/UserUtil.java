package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.colligate.AppErrorEnums;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.util.colligate.TokenUtil;
import com.e_commerce.miscroservice.user.service.UserService;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 获取用户
 * @ClassName UserUtil
 * @Auhor huangyangfeng
 * @Date 2019-03-27 19:03
 * @Version 1.0
 */
public class UserUtil {
    public static Log logger = Log.getInstance(UserUtil.class);

    /**
     * 根据用户token获取User
     * @param token
     * @return
     */
    public static TUser getUser(String token){
        logger.info("获取用户信息token={}",token);
        RedisUtil redisUtil = ApplicationContextUtil.getBean(RedisUtil.class);
        TUser user = (TUser) redisUtil.get(token);
        if (user==null&& StringUtils.isNoneEmpty(token)){
            UserService userService = ApplicationContextUtil.getBean(UserService.class);
            if (TokenUtil.genUserId(token)==null){
                logger.info("异常登陆信息，清除token={}重新登陆",token);
                redisUtil.del(token);
                throw new MessageException(String.valueOf(AppErrorEnums.LOGIN_ERROR.getCode()),"请重新登陆");
            }
            String userId = null;
            try {
                userId = TokenUtil.genUserId(token);

            }catch (Exception e){
                logger.error("解析用户id出错token={}",token);
            }
            if (StringUtils.isNotEmpty(userId)){
                user = userService.getUserbyId(Long.valueOf(userId));
            }else {
                redisUtil.del(token);
                throw new MessageException(String.valueOf(AppErrorEnums.LOGIN_ERROR.getCode()),"请重新登陆");

            }

        }
        return user;
    }

}
