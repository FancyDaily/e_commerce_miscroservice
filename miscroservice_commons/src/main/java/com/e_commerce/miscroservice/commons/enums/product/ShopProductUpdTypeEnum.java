package com.e_commerce.miscroservice.commons.enums.product;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 20:16
 * @Copyright 玖远网络
 */
public enum  ShopProductUpdTypeEnum{

    /**
     * 批量推荐
     */
    BATCH_DO_RECOMMEND(1),
    /**
     * 批量取消推荐
     */
    BATCH_CANCEL_RECOMMEND(2),
    /**
     * 批量上架
     */
    BATCH_PUT_ON_SALE(3),
    /**
     * 批量下架
     */
    BATCH_PUT_OFF_SALE(4),
    /**
     * 批量删除
     */
    BATCH_DELETE(5),
    /**
     * 更新商品sku的库存
     */
    UPD_PRODUCT_SKU_REMAIN(10),
    /**
     * 删除sku
     */
    DELETE_SUK(11),
    /**
     * 没有操作
     */
    NO_UPDATE(-1);

    private int code;

    ShopProductUpdTypeEnum(int code) {
        this.code = code;
    }

    public static ShopProductUpdTypeEnum create(Integer code) {
        if (code == null) {
            return NO_UPDATE;
        }

        for (ShopProductUpdTypeEnum typeEnum : values ()) {
            if (typeEnum.code == code) {
                return typeEnum;
            }
        }
        return NO_UPDATE;
    }


}
