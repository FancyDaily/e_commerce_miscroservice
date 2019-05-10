package com.e_commerce.miscroservice.commons.enums.application;

/**
 * 代金券
 */
public enum GZVoucherEnum {

    STATUS_INACTIVITED(1, "待激活"),
    STATUS_AVAILABLE(2, "可用"),
    STATUS_ALREADY_USED(3, "已使用"),
    STATUS_EXPIRED(4, "已过期"),
    TYPE_ALLPOWERFUL(1, "通用类型"),
    TYPE_SPECIFIC(2, "特定类型");

    private Integer code;    //状态
    private String desc;    //类型

    public Integer toCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    GZVoucherEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}

