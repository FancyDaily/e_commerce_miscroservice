package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 分发中心
 *
 * @author hyf
 * @version V1.0
 * @date 2018/12/7 11:55
 * @Copyright 玖远网络
 */
@Component
public class DistributionCenterHelper {
    @Autowired
    @Qualifier("userHashRedisTemplate")
    private HashOperations<String, String, String> userHashRedisTemplate;

    private static Log logger = Log.getInstance(DistributionCenterHelper.class);

    public final static String SUCCESS = "success";
    public final static String FAIL = "fail";
    public final static String MSG = "msg";
    public final static String CLOSE = "close";

    /**
     * @param modelCheck         店铺选择/ 直播间选择
     * @param preCode 前置标识
     * @param map
     * @return
     */
    public Map<String, String> intoIt(String modelCheck, String preCode, Map map) {
        //        第一步 先判定 是否分发
        //        第二步 判定 分发给谁
        //        第三步 判定 需要分发的 直播间/店中店等 是否存在

        Map<String, String> response = new HashMap<>(4);
        response.put(SUCCESS, FAIL);
        userHashRedisTemplate.put(modelCheck,preCode, preCode);
        String modelLive = userHashRedisTemplate.get(modelCheck,preCode);
        if (StringUtils.isEmpty(modelLive)) {
            //需要分发的模块已关闭 或者 不存在
            response.put(MSG, "NOT FIND ERROR");
            return response;
        }
        //一、思路1：根据if else 去判定 是否符合 若符合 则调用
        //一、思路2：直接携带 目标方法名称 根据反射 直接调用方法
//        Map<String, String> stringStringMap = (Map<String, String>) chooseMethod(preCode, modelMethodName, map);
//        DistributionCenterHelper distributionCenterHelper =  ContextKit.getDistributionCenter();
//        return (Map<String, String>)distributionCenterHelper.chooseMethod(preCode,modelMethodName,map);
        //二、思路1：工厂模式 通过工厂模式建立 需要被分发的目的地
        return modelHandel(modelCheck,preCode, map);
    }

    /**
     * 根据方法名获取 调用 相应方法
     *
     * @param modelMethodName
     * @return
     */
    public Object chooseMethod(String preCode, String modelMethodName, Map<String, Object> map) {
        Class<?> cls =  ApplicationContextUtil.getBean(DistributionCenterHelper.class).getClass();
//获取执行方法

//        DistributionCenterHelper distributionCenterHelper = new DistributionCenterHelper();
//        Class distributionClass = DistributionCenterHelper.class;
        Object returns = null;
        try {
            Object distributionCenterHelper = cls.newInstance();

            Method method = cls.getMethod(modelMethodName, String.class, Map.class);

//            Method method = distributionClass.getMethod(modelMethodName,String.class,Map.class);
            returns = method.invoke(distributionCenterHelper, preCode, map);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        System.out.println(returns);
        return returns;
    }

    /**
     * 直播间
     *
     * @param preCode
     * @param map
     * @return
     */
    public Map<String, String> modelHandel(String modelCheck, String preCode, Map<String, Object> map) {
        logger.info("模块处理 preCode={},map={}", preCode, map);
        Map<String, String> response = new HashMap<>(4);

//        TVActivity tvActivity = (TVActivity) MapTrunPojo.map2Object(map, TVActivity.class);
//        System.out.println(tvActivity);
        response.put(SUCCESS, FAIL);
        //跳转到 直播
        String roomId = userHashRedisTemplate.get(modelCheck,preCode);
        if (StringUtils.isEmpty(roomId)) {
            //该直播间不存在
            response.put(MSG, "NOT FIND ERROR");
            return response;
        }
        if (roomId.equals(CLOSE)) {
//                直播间已关闭
            response.put(MSG, "直播间已关闭");
            return response;
        }
        //跳转至直播间
        return response;
    }

    public String employShop() {
        System.out.println("EMPLOY_SHOP");
        return "EMPLOY_SHOP";
    }


}
