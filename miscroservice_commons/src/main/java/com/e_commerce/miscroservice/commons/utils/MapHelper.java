package com.e_commerce.miscroservice.commons.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/15 14:26
 * @Copyright 玖远网络
 */
public class MapHelper extends HashMap<String, Object> {


    public static MapHelper me(int initialCapacity) {
        return new MapHelper(initialCapacity);
    }

    public MapHelper(int initialCapacity) {
        super(initialCapacity);
    }

    public MapHelper(Map<? extends String, ?> m) {
        super(m);
    }

    @Override
    public MapHelper put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
