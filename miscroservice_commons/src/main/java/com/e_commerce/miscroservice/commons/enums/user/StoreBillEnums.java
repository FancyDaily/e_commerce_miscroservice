package com.e_commerce.miscroservice.commons.enums.user;

/**
 * 账目流水 枚举
 *
 * @author hyf
 * @version V1.0
 * @date 2018/12/12 20:02
 * @Copyright 玖远网络
 */
public enum StoreBillEnums {
//  类型 0 平台充值提现，1 用户充值提现，2 商品消费,3 退款,4 补偿, 5 扣款,6充值 ,7货品订单,8资金结算,9转出",isNUll = false)
    /**************************************************************************************************/
    /***********************************************收入支出*******************************************/
    /**************************************************************************************************/
    INCOME(0,"收入"),
    PAY(1,"支出"),
    /**************************************************************************************************/
    /********************************************明细类目-前缀*****************************************/
    /**************************************************************************************************/
    WITHDRAW_CASH(10,"提现"),
    RECHARGE(11,"充值"),
    ACCOUNT_IN(12,"转入"),
    ACCOUNT_OUT(13,"转出"),
    REFUND_MONEY(14,"退款"),
    COMPENSATE(15,"补偿"),
    CUT_PAYMENT(16,"扣款"),
    CONSUME(17,"商品消费"),
    GOODS_ORDER(18,"货品订单"),
    SETTLE_ACCOUNTS(19,"资金结算"),
    PLATFORM_INSTEAD_OF_SEND_GOODS(20,"平台代发货"),
    APP_REFUND(21,"平台代发商品退款"),//暂时没用
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /***********************************************充值提现*******************************************/
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    PLATFORM_RECHARGE_WITHDRAW_CASH(0, "平台充值提现"),
    USER_RECHARGE_WITHDRAW_CASH(1, "用户充值提现"),
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /*********************************后台统一状态-与明细类目前缀相-对应*******************************/
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    WITHDRAW_CASH_SUCCESS(1001,"提现成功"),
    RECHARGE_SUCCESS(1101,"充值成功"),
    ACCOUNT_IN_SUCCESS(1201,"转入成功"),
    APP_REFUND_MONEY_SUCCESS(1301,"平台代发商品退款给商家成功"),
    REFUND_MONEY_SUCCESS(1401,"退款成功"),
    COMPENSATE_SUCCESS(1501,"发放成功"),
    CUT_PAYMENT_SUCCESS(1601,"扣款成功"),
    CONSUME_SUCCESS(1701,"扣款成功"),
    APP_REFUNDMONEY_SUCCESS(1403,"平台商品退款给商家成功"),
    APP_PAY_MONEY(1602,"商家购买app商品扣款成功"),

    /**
     * C端购买商品,待结算+钱
     */
    GOODS_ORDER_SUCCESS(1801,"下单成功"),
//    资金结算 减去冻结金额
    SETTLE_ACCOUNTS_SUCCESS(1901,"结算成功"),
//    资金结算到可用账户 待结到已结
    SETTLE_ACCOUNTS_USE_SUCCESS(1902,"结算成功"),
    PLATFORM_INSTEAD_OF_SEND_GOODS_SUCCESS(2001,"平台代替发货成功"),

    ;
    private Integer code;
    private String value;

    StoreBillEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean isThis(Integer type) {
        if (type == null) {
            return false;
        }
        return code.equals(type);
    }
}
