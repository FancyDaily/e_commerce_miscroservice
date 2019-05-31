package com.e_commerce.miscroservice.commons.enums.colligate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public enum ApplicationEnum {

    XIAOSHI_APPLICATION(1,"晓时互助"),
    GUANZHAO_APPLICATION(2,"观照律师训练营");

    public static LoadingCache loadingCache = CacheBuilder.newBuilder()
            .maximumSize(10000) //设置缓存上线
			.expireAfterWrite(10, TimeUnit.SECONDS)  //写入时刷新缓存
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) {
                    return "";  //当取到null值时的默认值
                }
   });

    int code;
    String desc;

    ApplicationEnum(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int toCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
