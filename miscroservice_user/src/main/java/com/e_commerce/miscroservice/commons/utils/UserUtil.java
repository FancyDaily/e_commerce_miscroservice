package com.e_commerce.miscroservice.commons.utils;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserEnum;
import com.e_commerce.miscroservice.commons.enums.application.UserEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.AppErrorEnums;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.TokenUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import com.e_commerce.miscroservice.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

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

	private static String MANAGER_USER_DESCRIBE = "manager:user:%s";

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
        Long currentId = IdUtil.getId();
        TUser user = new TUser();
        if(currentId==null) {
            return null;
        }
        user.setId(currentId);
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
		Long id = getId();
//		Long id = null;
//		return id == null? 2000: id;
//		return id == null? 2051: id;
		return id;
	}

	public static Long getTestId(Long userId) {
		if(userId == null) {
			return getTestId();
		}
		return userId;
	}

	public static Long getId() {
		return IdUtil.getId();
	}

	public static Long getManagerId(CsqUserDao csqUserDao, RedisTemplate<String, Object> userRedisTemplate) {
		Long id = IdUtil.getId();
		String key = String.format(MANAGER_USER_DESCRIBE, id);
		TCsqUser csqUser = (TCsqUser) userRedisTemplate.opsForValue().get(key);
		logger.info("获取缓存={}", csqUser);
		Long expire = userRedisTemplate.getExpire(key);
		logger.info("剩余有效时间={}", expire);
		if (csqUser == null){
			csqUser = csqUserDao.selectByPrimaryKey(id);
			if (csqUser!=null){
				userRedisTemplate.opsForValue().set(key, csqUser, 30, TimeUnit.SECONDS);
			} else {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户不存在!");
			}
		}

		Integer maanagerType = csqUser.getMaanagerType();
		if(CsqUserEnum.MANAGER_TYPE_NOT_A_MANAGER.toCode().equals(maanagerType)) {	//非管理员
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前用户没有权限！");
		}
		return id;
	}

	public static Long getManagerId(CsqUserDao csqUserDao, HashOperations<String, String, Object> userRedisTemplate) {
		Long id = IdUtil.getId();
		String key = String.format(MANAGER_USER_DESCRIBE, id);
		String hashKey = String.valueOf(id);
		TCsqUser csqUser = (TCsqUser) userRedisTemplate.get(key, hashKey);
		logger.info("获取缓存={}", csqUser);
		Long expire = userRedisTemplate.getOperations().getExpire(key);
		logger.info("剩余有效时间={}", expire);
		if (csqUser == null){
			csqUser = csqUserDao.selectByPrimaryKey(id);
			if (csqUser!=null){
				userRedisTemplate.put(key, hashKey, csqUser);
				Boolean aBoolean = userRedisTemplate.getOperations().expire(key, 30, TimeUnit.SECONDS);
			} else {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户不存在!");
			}
		}

		Integer maanagerType = csqUser.getMaanagerType();
		if(CsqUserEnum.MANAGER_TYPE_NOT_A_MANAGER.toCode().equals(maanagerType)) {	//非管理员
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前用户没有权限！");
		}
		return id;
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
