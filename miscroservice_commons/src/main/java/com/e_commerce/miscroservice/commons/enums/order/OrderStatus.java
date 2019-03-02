package com.e_commerce.miscroservice.commons.enums.order;


//订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)
public enum OrderStatus {
    UNPAID(0, "未付款"),
    
    ALL(5, "所有"),

    PAID(10, "已付款"),

    UNCHECK(20, "待审核"),

    CHECKED(30, "已审核"),

    CHECK_FAIL(40, "审核不通过"),

    DELIVER(50, "已发货"),

    SIGNED(60, "已签收"),

    SUCCESS(70, "交易成功"),

    REFUNDING(80, "退款中"),

    REFUNDED(90, "退款成功"),

    CLOSED(100, "交易关闭");

    private OrderStatus(int intValue, String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    private int intValue;

    private String displayName;

    public int getIntValue() {
        return intValue;
    }

    public String getDisplayName() {
        return displayName;
    }
}
