package com.e_commerce.miscroservice.commons.enums.user;

import org.apache.commons.lang3.StringUtils;

public enum Platform {
    IPHONE("iphone"),

    IPOD("ipod"),

    IPAD("ipad"),

    ANDROID("android"),

    ANDROID_PAD("android_pad"),

    WINDOWS_PHONE("wp"),
    
    WENXIN("wx"),

    DESKTOP("desktop");

    private Platform(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public boolean is(String value) {
        return StringUtils.equals(getValue(), value);
    }
    /**
     * 是否微信
     * @return
     */
    public boolean isWeiXin() {
        return this == WENXIN;
    }
   
    public boolean isIOS() {
        return this == IPHONE || this == IPOD || this == IPAD;
    }
   
    public boolean isAndroid() {
        return this == ANDROID || this == ANDROID_PAD;
    }

    public static Platform parse(String value, Platform defaultValue) {
        for(Platform platform : values()){
            if (platform.is(value)) {
                return platform;
            }
        }
        return defaultValue;
    }

}
