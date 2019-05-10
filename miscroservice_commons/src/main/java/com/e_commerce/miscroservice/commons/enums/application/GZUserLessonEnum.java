package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-08 21:06
 */
public enum GZUserLessonEnum {

    STATUS_UNAVAILABLE(0,"不可用"),
    STATUS_AVAILABLE(1,"可用"),
    LESSON_COMPLETION_STATUS_NO(0, "未完成"),
    LESSON_COMPLETION_STATUS_YES(1, "完成");

    int code;
    String desc;

    GZUserLessonEnum(int code, String desc) {
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
