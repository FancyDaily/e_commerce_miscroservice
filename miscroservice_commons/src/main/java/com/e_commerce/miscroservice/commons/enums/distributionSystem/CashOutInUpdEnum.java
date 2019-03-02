package com.e_commerce.miscroservice.commons.enums.distributionSystem;

/**
 * 流水的修改操作类型
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/13 15:39
 * @Copyright 玖远网络
 */
public enum CashOutInUpdEnum{
    /**
     * 结算分销佣金
     */
    SETTLE_COMMISSION,
    /**
     * 管理金预待结算到待结算
     */
    PRE_WAIT_MANAGER_TO_WAIT,
    /**
     * 结算分销管理金
     */
    SETTLE_TEAM_IN,
    /**
     * 提现成功
     */
    CASH_OUT_SUCCESS,
    /**
     * 提现失败
     */
    CASH_OUT_FAILED,
    /**
     * 确认提现
     */
    CONFIRM_CASH_OUT_FAILED,
    /**
     * 开始提现
     */
    START_CASH,
    /**
     * 待结算条件满足,但是不发放
     */
    WAIT_SETTLE_CONDITION_IS_OK_BUT_NO_SETTLE
    ;
}
