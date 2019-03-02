package com.e_commerce.miscroservice.commons.enums.application;

/**
 * 数据库存活的枚举
 */
public enum DbLiveSignEnum {

    LIVE(0), DIE(1);
    private Integer value;

    DbLiveSignEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
