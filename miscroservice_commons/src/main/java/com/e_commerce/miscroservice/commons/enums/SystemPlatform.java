package com.e_commerce.miscroservice.commons.enums;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/14 16:36
 * @Copyright 玖远网络
 */
public enum SystemPlatform{

    /**
     * app系统平台
     */
    STORE (1)
    ;

    /**
     * 平台编号
     */
    private int code;

    SystemPlatform(int code) {
        this.code = code;
    }

    public static boolean isThis(Integer code) {
        if (code == null) {
            return false;
        }
        for (SystemPlatform platform : SystemPlatform.values ()) {
            if (platform.getCode () == code) {
                return true;
            }
        }
        return false;
    }


    public static SystemPlatform build(Integer code) {
        if (code == null) {
            return null;
        }
        for (SystemPlatform platform : SystemPlatform.values ()) {
            if (platform.getCode () == code) {
                return platform;
            }
        }
        return null;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
