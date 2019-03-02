package com.e_commerce.miscroservice.commons.enums.user;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/9 15:12
 * @Copyright 玖远网络
 */
public enum ShopMembAcctCashOutInTypeEnum{

    /**
     * 0.自有订单分销返现
     */
    SELF_COMMISSION(0),
    /**
     * 1.一级粉丝返现入账
     */
    FANS_1_COMMISSION(1),
    /**
     * 2.二级粉丝返现入账
     */
    FANS_2_COMMISSION(2),
    /**
     * 10.分销商的团队收益入账
     */
    DISTRIBUTOR_TEAM_IN (10),
    /**
     * 11.合伙人的自有返佣团队收益入账
     */
    PARTNER_TEAM_IN (11),
    /**
     * 50.提现
     */
    CARRY_CASH(50)
            ;

    private int code;

    ShopMembAcctCashOutInTypeEnum(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
