package com.e_commerce.miscroservice.commons.enums.activity;

/**
 * 图片类型枚举
 * Create by hyf on 2018/12/20
 * @author hyf
 */
public enum PictureTypeEnums {

    LOTTERY_DRAW_ACTIVITY_PIC(1001,"活动内容图片"),
    LOTTERY_DRAW_ACTIVITY_BANNER(1002,"活动Banner图片"),
    LOTTERY_DRAW_ACTIVITY_BUTTON(1003,"活动Button图片"),

    ;

    private Integer code;
    private String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    PictureTypeEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
