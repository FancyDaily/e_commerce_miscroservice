package com.e_commerce.miscroservice.commons.enums.distributionSystem;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/13 14:41
 * @Copyright 玖远网络
 */
public enum CashOutInTypeEnum{
    /**
     * 自有订单分销返现
     */
    SELF_COMMISSION (0),

    /**
     * 一级粉丝返现入账
     */
    FANS_1_COMMISSION (1),
    /**
     * 二级粉丝返现入账
     */
    FANS_2_COMMISSION (2),
    /**
     * 分销商的团队收益入账
     */
    DISTRIBUTOR_TEAM_IN (10),
    /**
     * 合伙人的团队收益入账
     */
    PARTNER_TEAM_IN (11),
    /**
     * 签到
     */
    SING_IN_AWARD (20),
    /**
     * 签到阶段奖励
     */
    SING_STAGE_AWARD (21),
    /**
     * 订单取消
     */
    ORDER_CANCEL (30),
    /**
     * 订单抵扣
     */
    ORDER_DEDUCTION (31),
    /**
     * 分享
     */
    SHARE (40),
    /**
     * 邀新(新用户)
     */
    NEW_USER_INVITEE(41),
    /**
     * 提现-总金额
     */
    CASH_OUT_TOTAL (50),
    /**
     * 提现-佣金
     */
    CASH_OUT_COMMISSION (51),
    /**
     * 提现-管理金
     */
    CASH_OUT_MANAGER (52)

    ;


    public static CashOutInTypeEnum find(Integer type) {
        if (type == null) {
            return null;
        }
        for (CashOutInTypeEnum typeEnum : values ()) {
            if (typeEnum.isThis (type)) {
                return typeEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    private int code;

    CashOutInTypeEnum(int code) {
        this.code = code;
    }

    /**
     * 是否是分佣金入账
     *
     * @param type {@link ShopMemberAccountCashOutIn#type}
     * @return true 是
     * @author Charlie
     * @date 2018/10/10 19:09
     */
    public static boolean isCommissionCashIn(Integer type) {
        if (type == null) {
            return Boolean.FALSE;
        }
        return type == SELF_COMMISSION.code || FANS_1_COMMISSION.code == type || FANS_2_COMMISSION.code == type;
    }



    /**
     * 是否是管理金入账
     *
     * @param type {@link ShopMemberAccountCashOutIn#type}
     * @return true 是
     * @author Charlie
     * @date 2018/10/10 19:09
     */
    public static boolean isManagerCashIn(Integer type) {
        if (type == null) {
            return Boolean.FALSE;
        }
        return type == DISTRIBUTOR_TEAM_IN.code || PARTNER_TEAM_IN.code == type;
    }

    public boolean isThis(Integer type) {
        if (type == null) {
            return Boolean.FALSE;
        }
        return type == code;
    }
}
