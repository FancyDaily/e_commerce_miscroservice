package com.e_commerce.miscroservice.commons.utils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/16 13:39
 * @Copyright 玖远网络
 */
public class CollectionsKit{


    /**
     * 设置默认值
     *
     * @param source source
     * @param defaultVal defaultVal
     * @param keys keys
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/16 13:45
     */
    public static <T> Map<String, T> putDefaultVal(Map<String, T> source, T defaultVal, String... keys) {
        for (String key : keys) {
            source.putIfAbsent (key, defaultVal);
        }
        return source;
    }


    /**
     * 设置默认值
     *
     * @param source source
     * @param defaultVal defaultVal
     * @param keys keys
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/16 13:45
     */
    public static <T> List<Map<String, T>> putDefaultVal(List<Map<String, T>> source, T defaultVal, String... keys) {
        for (int i = 0; i < source.size (); i++) {
            source.set (i, putDefaultVal (source.get (i), defaultVal, keys));
        }

        return source;
    }





    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<> ();
        map.put ("name", "charlie");
        List<Map<String, Object>> source = new ArrayList<> (1);
        source.add (map);
        source = putDefaultVal (source, BigDecimal.ZERO, "name", "sex", "count");
        System.out.println ("source = " + source);

    }
}
