package com.e_commerce.miscroservice.commons.enums.system;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/9 10:07
 * @Copyright 玖远网络
 */
public enum  DataDictionaryEnums {

    SIGN_DATA_COIN("signDateCoin","sign"),//连续签到奖励
    SIGN_PERIODICAL_PRIZE("signPeriodicalPrize","sign"),//签到阶段奖励
    UPGRADE_CONDITION_PARTNER("partner","upgradeCondition"),//合伙人升级条件
    UPGRADE_CONDITION_DISTRIBUTOR("distributor","upgradeCondition"),//分销商升级条件
    UPGRADE_CONDITION_STORE("store","upgradeCondition"),//店长升级条件
    /**
     * 金币人民币兑换率
     */
    GOLD_COIN_EXCHANGE_RATE("exchangeRate","goldCoin"),
    /**
     * 提现方式限额判断
     */
    CASH_OUT_LIMIT_MONEY("limitMoney","cashOut"),
    /**
     * 在限额内的提现指定日期
     */
    CASH_OUT_LIMIT_TIME("limitTime","cashOut"),
    /**
     * 分享商品
     */
    SHARE_PRODUCT_CONFIG("productConfig","share")
    ;

    private String code;
    private String groupCode;

    DataDictionaryEnums(String code, String groupCode) {
        this.code = code;
        this.groupCode = groupCode;
    }

    public String getCode() {
        return code;
    }

    public String getGroupCode() {
        return groupCode;
    }


    public boolean isThis(String groupCode, String code) {
        for (DataDictionaryEnums dictionaryEnums : values ()) {
            if (dictionaryEnums.getGroupCode ().equals (groupCode) && dictionaryEnums.getCode ().equals (code)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
