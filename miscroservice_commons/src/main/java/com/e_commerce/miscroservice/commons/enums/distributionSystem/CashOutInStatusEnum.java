package com.e_commerce.miscroservice.commons.enums.distributionSystem;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/13 14:59
 * @Copyright 玖远网络
 */
public enum CashOutInStatusEnum{


    /**
     * 待结算
     */
    WAIT_SETTLE_ACCOUNT(1),
    /**
     * 已结算
     */
    ALREADY_SETTLE_ACCOUNT(2),
    /**
     * 冻结
     */
    FROZEN(3),
    /**
     * 预待结算(仅插入一条记录,还未进入待结算状态)
     */
    PRE_WAIT_SETTLE(5)

    ;

    private int code;

    CashOutInStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }


    public boolean isThis(Integer status) {
        if (status == null) {
            return Boolean.FALSE;
        }
        return code == status;
    }
}
