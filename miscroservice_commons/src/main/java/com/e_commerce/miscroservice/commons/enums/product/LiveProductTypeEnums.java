package com.e_commerce.miscroservice.commons.enums.product;


/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/15 10:00
 * @Copyright 玖远网络
 */
public enum LiveProductTypeEnums {

    /**
     * 0平台供应商商品
     */
    SUPPLIER_PRODUCT (ShopProductOwnEnum.SUPPLIER_PRODUCT.getCode()),
    /**
     * 1是用户自定义款
     */
    SELF_CUSTOM (ShopProductOwnEnum.SELF_CUSTOM.getCode()),
    /**
     * 2用户自营平台同款
     */
    AS_SAME_AS_SUPPLIER (ShopProductOwnEnum.AS_SAME_AS_SUPPLIER.getCode()),
    /**
     * 10平台商品
     */
    PLATFORM(10)
    ;

    LiveProductTypeEnums(Integer code) {
        this.code = code;
    }

    public static final int[] WXA_USER_PRODUCT_TYPES = new int[] {};

    private Integer code;

    public static LiveProductTypeEnums create(Integer type) {
        if (type == null) {
            return null;
        }
        for (LiveProductTypeEnums typeEnums : values()) {
            if (typeEnums.code.equals(type)) {
                return typeEnums;
            }
        }
        return null;
    }

    /**
     * 自己的直播商品(非平台直播商品)
     *
     * @param type type
     * @return boolean
     * @author Charlie
     * @date 2019/1/16 11:20
     */
    public static boolean isSelfLiveProduct(Integer type) {
        return SUPPLIER_PRODUCT.isThis(type) || SELF_CUSTOM.isThis(type) || AS_SAME_AS_SUPPLIER.isThis(type);
    }

    public Integer getCode() {
        return code;
    }

    public boolean isThis(Integer type) {
        if (type == null) {
            return Boolean.FALSE;
        }
        return this.code.equals(type);
    }
}
