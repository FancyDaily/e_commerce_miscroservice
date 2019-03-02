package com.e_commerce.miscroservice.commons.enums.product;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 14:21
 * @Copyright 玖远网络
 */
public enum ShopProductOwnEnum{

    //商品所属：0平台供应商商品, 1是用户自定义款，2用户自营平台同款
    SUPPLIER_PRODUCT (0),
    SELF_CUSTOM (1),
    AS_SAME_AS_SUPPLIER (2)
    ;

    private Integer code;

    ShopProductOwnEnum(Integer code) {
        this.code = code;
    }

    public boolean isThis(Integer code) {
        if (code == null) {
            return Boolean.FALSE;
        }
        return this.code.equals (code);
    }


    public static ShopProductOwnEnum create(Integer code) {
        if (code == null) {
            return null;
        }

        for (ShopProductOwnEnum own : values ()) {
            if (own.isThis (code)) {
                return own;
            }
        }
        return null;
    }


    /**
     * 自营
     *
     * @param code code
     * @return boolean
     * @author Charlie
     * @date 2018/11/26 14:43
     */
    public static boolean isSelfSupport(Integer code) {
        return SELF_CUSTOM.isThis (code) || AS_SAME_AS_SUPPLIER.isThis (code);
    }




    /**
     * 非自营
     *
     * @param code code
     * @return boolean
     * @author Charlie
     * @date 2018/11/26 14:42
     */
    public static boolean isNoSelfSupport(Integer code) {
        return SUPPLIER_PRODUCT.isThis (code);
    }


    public Integer getCode() {
        return code;
    }
}
