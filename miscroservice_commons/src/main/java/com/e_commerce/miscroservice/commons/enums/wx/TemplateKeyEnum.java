package com.e_commerce.miscroservice.commons.enums.wx;

/**
 * 微信通知模版的关键词
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/12 14:12
 * @Copyright 玖远网络
 */
public enum TemplateKeyEnum{

    /**
     * 商品名称
     */
    PRODUCT_NAME (8, "商品名称"),
    /**
     * 订单号
     */
    ORDER_NO (7, "订单号"),
    /**
     * 金额
     */
    MONEY (9, "金额"),
    /**
     * 收益来源
     */
    EARNINGS_SOURCE (2, "收益来源"),
    /**
     * 收益金额
     */
    EARNINGS_MONEY (2, "收益金额"),
    /**
     * 到账时间
     */
    IN_ACCOUNT_TIME (3, "到账时间");


    /**
     * 关键词id
     */
    private int id;
    /**
     * 关键词name
     */
    private String name;

    TemplateKeyEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
