package com.e_commerce.miscroservice.commons.enums.activity;

import org.springframework.util.ObjectUtils;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/21 15:49
 * @Copyright 玖远网络
 */
public enum ShareTypeEnum {

    /**
     * 空
     */
    NULL(-999),
    /**
     * 分享商品
     */
    PRODUCT(2),
    /**
     * 分销活动
     */
    ACTIVITY(1),
    /**
     * 分享优惠券
     */
    COUPON(3),
    /**
     * 小程序
     */
    WXA(4),
    /**
     * 粉丝分享
     */
    FANS_SHARE(10),
    /**
     * 小程序分享活动-分享至首页
     */
    WWA_SHARE_TO_HOME(1001)
        ;

    private Integer code;

    ShareTypeEnum(Integer code) {
        this.code = code;
    }

    public static ShareTypeEnum create(Integer code) {
        for (ShareTypeEnum type : values ()) {

            if (ObjectUtils.nullSafeEquals (code, type.getCode ())) {
                return type;
            }
        }
        return ShareTypeEnum.NULL;
    }


    public int getCode() {
        return code;
    }

    public boolean isThis(Integer shareType) {
        if (shareType == null) {
            return false;
        }
        for (ShareTypeEnum typeEnum : values ()) {
            if (typeEnum.code.equals (shareType)) {
                return true;
            }
        }
        return false;
    }
}
