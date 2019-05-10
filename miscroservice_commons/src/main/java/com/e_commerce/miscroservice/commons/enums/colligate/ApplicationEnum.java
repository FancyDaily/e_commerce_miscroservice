package com.e_commerce.miscroservice.commons.enums.colligate;

public enum ApplicationEnum {

    XIAOSHI_APPLICATION(1,"晓时互助小程序"),
    GUANZHAO_APPLICATION(2,"观照律师训练营");

    int code;
    String desc;

    ApplicationEnum(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int toCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
