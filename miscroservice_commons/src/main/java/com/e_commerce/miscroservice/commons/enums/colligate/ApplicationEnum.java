package com.e_commerce.miscroservice.commons.enums.colligate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public enum ApplicationEnum {

    XIAOSHI_APPLICATION(1,"晓时互助", "XIOASHI_AUTHORIZE_"),
    GUANZHAO_APPLICATION(2,"观照律师训练营", "GUANZHAO_AUTHORIZE_"),
	CONGSHANQIAO_APPLICATION(3, "丛善桥", "CONGSHANQIAO_AUTHORIZE_"),
	LPGL_APPLICATION(4, "楼盘管理", "LPGL_AUTHORIZE_");

    public static LoadingCache loadingCache = CacheBuilder.newBuilder()
            .maximumSize(10000) //设置缓存上线
			.expireAfterWrite(5, TimeUnit.MINUTES)  //写入时刷新缓存
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) {
                    return "";  //当取到null值时的默认值
                }
   });

    int code;
    String desc;
    String namePrefix;

    public int toCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

	public String getNamePrefix() {
		return namePrefix;
	}

	ApplicationEnum(int code, String desc, String namePrefix) {
		this.code = code;
		this.desc = desc;
		this.namePrefix = namePrefix;
	}

}
