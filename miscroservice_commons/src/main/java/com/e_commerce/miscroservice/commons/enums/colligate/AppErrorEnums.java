package com.e_commerce.miscroservice.commons.enums.colligate;

/**
 * 错误信息 枚举类
 */

public enum AppErrorEnums {

    /**
     * 登陆异常重新登轮
     */
    LOGIN_ERROR(33333,"登陆异常，重新登陆");
    private Integer code;


    private String msg;

    AppErrorEnums() {
    }

    AppErrorEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
