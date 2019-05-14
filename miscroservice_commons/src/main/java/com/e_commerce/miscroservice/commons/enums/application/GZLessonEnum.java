package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 20:47
 */
public enum GZLessonEnum {

    AVAILABLE_STATUS_NO(0, "不可用"),
    AVAILABLE_STATUS_YES(1, "可用"),
    VIDEO_STATUS_AVAILABLE_NO(0, "视频未装载"),
    VIDEO_STATUS_AVAILABLE_YES(1, "视频已装载" );

    int code;
    String desc;

    GZLessonEnum(int code, String desc) {
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
