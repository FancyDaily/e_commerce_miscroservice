package com.e_commerce.miscroservice.commons.enums.product;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 17:26
 * @Copyright 玖远网络
 */
public enum  ShopGoodsCarStatusEnum{
    /**
     * -1 删除
     */
    DELETE(-1),
    /**
     * 0 禁用
     */
    FORBIDDEN(0),
    /**
     * 1 正常
     */
    NORMAL(1),
    /**
     * 2 失效
     */
    DISABLED(2)
    ;


    private int code;

    ShopGoodsCarStatusEnum(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }
}
