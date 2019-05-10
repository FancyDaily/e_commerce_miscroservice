package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-08 21:06
 */
public enum GZSubjectEnum {

    AVAILABLE_STATUS_TRUE(1, "可用"),
    AVAILABLE_STATUS_FALSE(2, "不可用");

    int code;
    String desc;

    GZSubjectEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
