package com.e_commerce.miscroservice.commons.enums.live;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/16 15:00
 * @Copyright 玖远网络
 */
public enum LiveUserTypeEnum {

    //主播类型: 0 店家主播，1 平台主播  ，2 供应商主播 ，3 普通主播

    /**
     * 店家主播
     */
    SHOP(0),
    /**
     * 平台主播
     */
    PLATFORM(1),
    /**
     * 供应商主播
     */
    SUPPLIER(2),
    /**
     * 普通主播
     */
    COMMON(3),
    /**
     * 未知
     */
    UNKNOWN(-1)
    ;

    private int code;

    LiveUserTypeEnum(int code) {
        this.code = code;
    }


    public static LiveUserTypeEnum create(Integer type) {
        if (type == null) {
            return UNKNOWN;
        }
        for (LiveUserTypeEnum typeEnum : values()) {
            if (type.intValue() == typeEnum.code) {
                return typeEnum;
            }
        }
        return UNKNOWN;
    }



}
