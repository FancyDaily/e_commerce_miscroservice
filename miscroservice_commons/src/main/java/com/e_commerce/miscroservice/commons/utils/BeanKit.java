package com.e_commerce.miscroservice.commons.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 17:16
 * @Copyright 玖远网络
 */
public class BeanKit{

    /**
     * 有一个是null就返回true
     *
     * @param objects objects
     * @return boolean
     * @author Charlie
     * @date 2018/9/20 17:18
     */
    public static boolean hasNull(Object... objects) {
        if (objects == null || objects.length == 0) {
            return true;
        }

        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
            if (obj instanceof Optional) {
                if (! ((Optional) obj).isPresent ()) {
                    return true;
                }
            }
            if (obj instanceof CharSequence) {
                if (StringUtils.isBlank (((CharSequence) obj))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean notNull(Object object) {
        return ! hasNull(object);
    }


    public static boolean gt0(Long num) {
        if (num == null) {
            return false;
        }
        return num.compareTo (0L) > 0;
    }

    /**
     * emoji表情替换
     *
     * @param source 原字符串
     * @param slipStr emoji表情替换成的字符串
     * @return 过滤后的字符串
     */
    public static String replaceEmoji(String source,String slipStr) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }
    }


    /**
     * UUID
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/13 23:01
     */
    public static String uuid() {
        return UUID.randomUUID ().toString ().replaceAll ("-", "");
    }

    public static Long safeParseLong(String source) {
        if (StringUtils.isBlank (source)) {
            return null;
        }
        return Long.parseLong (source);
    }


    public static String strOf(String userNickname, String defVal) {
        return userNickname == null ? defVal : userNickname;
    }
}
