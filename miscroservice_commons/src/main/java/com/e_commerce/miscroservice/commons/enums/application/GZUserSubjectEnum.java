package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 17:20
 */
public enum GZUserSubjectEnum {

    STATUS_NOT_AVAILABLE_YET(0, "未开课"),
    STATUS_LEARNING(1, "正在学习"),
    STATUS_EXPIRED(2, "过期"),
    STATUS_DONE(3, "完成");

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

    GZUserSubjectEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
