package com.e_commerce.miscroservice.commons.enums.distributionSystem;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 14:26
 * @Copyright 玖远网络
 */
public enum StrategyEarningsTypeEnum{
    /**
     * 自分佣
     */
    SELF_COMMISSION_EARNINGS(0),
    /**
     * 一级粉丝分佣
     */
    FANS1_COMMISSION_EARNINGS(1),
    /**
     * 二级粉丝分佣
     */
    FANS2_COMMISSION_EARNINGS(2),
    /**
     * 团队管理提成
     */
    MANAGER(10);

    private int code;

    StrategyEarningsTypeEnum(int code) {
        this.code = code;
    }

    public boolean isThis(Integer code) {
        if (code == null) {
            return Boolean.FALSE;
        }
        return this.code == code;
    }


    public int getCode() {
        return code;
    }
}
