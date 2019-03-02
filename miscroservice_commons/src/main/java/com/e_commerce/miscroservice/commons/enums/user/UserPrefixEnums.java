package com.e_commerce.miscroservice.commons.enums.user;

/**
 * Create by hyf on 2018/11/5
 */

/**
 * 用户登陆注册前缀
 */
public enum UserPrefixEnums {
    SYSTEM_PLATFORM_APP("APP",0),
    SYSTEM_PLATFORM_OPERATE("OPERATE",1),
    SYSTEM_PLATFORM_CRM("CRM",2);
    private String  platform;
    private Integer code;

    UserPrefixEnums(String platform, Integer code) {
        this.platform = platform;
        this.code = code;
    }

    public String  getPlatform() {
        return  platform;
    }

    public Integer getCode() {
        return code;
    }
}
