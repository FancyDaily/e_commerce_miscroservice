package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 21:12
 */
public enum KeyValueEnum {

    TYPE_SIGN(1,"视频签名");

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

    KeyValueEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
