package com.e_commerce.miscroservice.commons.enums.activity;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/18 16:07
 * @Copyright 玖远网络
 */
public enum  ActivityEnums {
    LOTTERY_DRAW(1001,"抽奖活动"),

    ;
    private Integer code;
    private String value;

    ActivityEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
