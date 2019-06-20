package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.enums.colligate.AppErrorEnums;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.TokenUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import com.e_commerce.miscroservice.user.service.UserService;
import org.apache.commons.lang3.StringUtils;

import static com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService.DEFAULT_PASS;

/**
 * @Description 获取用户
 * @ClassName UserUtil
 * @Auhor huangyangfeng
 * @Date 2019-03-27 19:03
 * @Version 1.0
 */
public class UserUtil {

    public static UserUtil userUtil;

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

    public static TUser getUser() {
        Integer currentId = IdUtil.getId();
        TUser user = new TUser();
        if(currentId==null) {
            return null;
        }
        user.setId(Long.valueOf(currentId));
        return user;
    }

    public static TUser getTestUser(Long userId) {
		TUser user = getUser();
		if(user == null) {
			user = new TUser();
			userId = userId == null? 1292:userId;
			user.setId(userId);
			user.setName("测试用户-三胖");
		}
		return user;
	}

	public static TUser getTestUser() {
    	return getTestUser(null);
	}

	public static Long getTestId() {
//		Long id = getId();
		Long id = null;
		return id == null? 1292: id;
	}

	public static Long getId() {
    	return Long.valueOf(IdUtil.getId());
	}

	public static Integer getApplication(Integer option) {
		Integer application = ApplicationEnum.XIAOSHI_APPLICATION.toCode();	//默认值
		if(option!=null) {
			boolean flag = false;
			for(ApplicationEnum tEnum:ApplicationEnum.values()) {
				if(tEnum.toCode() == option) {
					flag = true;
					application = option;
					break;
				}
			}
			if(!flag) {
				throw new MessageException("application参数不合法!");
			}
		}
		return application;
	}

	public static ApplicationEnum getApplicationEnum(Integer option) {
		ApplicationEnum applicationEnum = ApplicationEnum.XIAOSHI_APPLICATION;	//默认值
		if(option!=null) {
			boolean flag = false;
			for(ApplicationEnum tEnum:ApplicationEnum.values()) {
				if(tEnum.toCode() == option) {
					flag = true;
					applicationEnum = tEnum;
					break;
				}
			}
			if(!flag) {
				throw new MessageException("application参数不合法!");
			}
		}
		return applicationEnum;
	}

	public static String getApplicationNamePrefix(Integer option) {
		String namePrefix = ApplicationEnum.XIAOSHI_APPLICATION.getNamePrefix();	//默认值
		if(option!=null) {
			boolean flag = false;
			for(ApplicationEnum tEnum:ApplicationEnum.values()) {
				if(tEnum.toCode() == option) {
					flag = true;
					namePrefix = tEnum.getNamePrefix();
					break;
				}
			}
			if(!flag) {
				throw new MessageException("application参数不合法!");
			}
		}
		return namePrefix;
	}

	public static TCsqUser login(TCsqUser tCsqUser, Integer application, AuthorizeRpcService authorizeRpcService) {
		String namePrefix = UserUtil.getApplicationNamePrefix(application);
		Token load = authorizeRpcService.load(namePrefix + tCsqUser.getId(),
			DEFAULT_PASS, tCsqUser.getUuid());
		if (load != null && load.getToken() != null) {
			tCsqUser.setToken(load.getToken());
		}
		return tCsqUser;
	}
}
