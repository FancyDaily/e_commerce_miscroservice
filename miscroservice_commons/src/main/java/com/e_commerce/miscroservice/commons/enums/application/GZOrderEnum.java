package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Description 观照订单枚举
 * @ClassName GZOrderEnum
 * @Auhor huangyangfeng
 * @Date 2019-05-12 19:22
 * @Version 1.0
 */
public enum  GZOrderEnum {

    IS_SALE_PRICE_YES(1,"使用优惠价格"),
    IS_SALE_PRICE_NO(0, "不使用优惠价格"),

    UN_PAY(1,"待支付"),
    PAYED(2,"支付完成"),
    TIMEOUT_PAY(3,"交易超时关闭"),
    CANCEL_PAY(4,"交易取消"),;

    public static final long INTERVAL = 30L * 60 * 1000;
    private Integer code;
    private String value;

    GZOrderEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }}


