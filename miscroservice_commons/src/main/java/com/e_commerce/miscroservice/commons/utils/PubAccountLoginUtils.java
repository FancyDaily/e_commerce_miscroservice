package com.e_commerce.miscroservice.commons.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/28 19:03
 * @Copyright 玖远网络
 */
public class PubAccountLoginUtils{

    private static Log logger = Log.getInstance (PubAccountLoginUtils.class);


    public static final String SEPARATOR = "_";
    /**
     * 返还给客户端的openId key
     */
    public static final String COOKIE_OPEN_ID = "openId";
    /**
     * 返还给客户端的token key
     */
    public static final String COOKIE_TOKEN = "openToken";

    public static final int CODE = 505;
    public static final String TIP = "请登录";

    /**
     * 生成token
     *
     * @param openId openId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/28 17:54
     */
    public static String createUserToken(String openId) {
        return Md5Util.md5 (openId + "yjj_2018");
    }

    /**
     * 校验登录,未登录则抛异常
     *
     * @param cache  cache
     * @param openId openId
     * @param token  token
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/28 19:16
     */
    public static PublicAccountUser checkUserToken(ValueOperations<String, String> cache, String openId, String token) {
        logger.info ("校验token和openId---openId={},token={}", openId, token);
        //非空
        ErrorHelper.declare (! BeanKit.hasNull (openId, token), CODE, TIP);
        //token合法
        openId = openId.trim ();
        token = token.trim ();
        String userToken = createUserToken (openId);
        boolean isLegal = userToken.equals (token);
        logger.info ("校验openId合法 isLegal={},userToken={},token={}", isLegal, userToken, token);
        ErrorHelper.declare (isLegal, CODE, TIP);
        //redis有
        String key = userCacheKey (openId);
        logger.info ("key={}", key);
        String json = cache.get (key);
        logger.info ("缓存中获取user={}", json);
        return StringUtils.isNotBlank (json) ? JSONObject.parseObject (json, new TypeReference<PublicAccountUser> (){}) : null;
    }


    /**
     * 校验登录,未登录则抛异常
     *
     * @param cache   cache
     * @param request request
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/28 19:22
     */
    public static PublicAccountUser checkUserToken(ValueOperations<String, String> cache, HttpServletRequest request) {
        return checkUserToken (cache, request.getParameter (COOKIE_OPEN_ID), request.getParameter (COOKIE_TOKEN));
    }


    /**
     * 将openId和token返回客户端
     *
     * @param openId   openId
     * @param response response
     * @author Charlie
     * @date 2018/9/28 19:27
     */
    public static void putTokenInResponse(String openId, HttpServletResponse response) {
        HttpUtils.CookieMap.me (2)
                .putCookie (COOKIE_OPEN_ID, openId)
                .putCookie (COOKIE_TOKEN, createUserToken (openId))
                .putInResponse (response);
    }


    /**
     * 用户缓存中存的key
     *
     * @param openId openId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/28 19:23
     */
    public static String userCacheKey(String openId) {
        return RedisKeyEnum.PUBLIC_ACCOUNT_USER_WX.toKey () + openId.trim ();
    }


    /**
     * 根据openId获取user
     *
     * @param cache cache
     * @param openId openId
     * @return com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser
     * @author Charlie
     * @date 2018/10/24 21:33
     */
    public static PublicAccountUser getByOpenId(ValueOperations<String, String> cache, String openId) {
        if (StringUtils.isNotBlank (openId)) {
            String json = cache.get (userCacheKey (openId));
            if (StringUtils.isNotBlank (json)) {
                return JSONObject.parseObject (json, new TypeReference<PublicAccountUser> (){});
            }
        }
        return null;
    }
}
