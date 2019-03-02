package com.e_commerce.miscroservice.commons.enums.distributionSystem;

/**
 * Create by hyf on 2018/10/10
 */
public enum DistributionGradeEnum {
    COMMON(0,"common"),
    STORE(1,"store"),
    DISTRIBUTOR(2,"distributor"),
    PARTNER(3,"partner");
    private Integer code;
    private String value;

    DistributionGradeEnum(Integer code, String value) {
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
