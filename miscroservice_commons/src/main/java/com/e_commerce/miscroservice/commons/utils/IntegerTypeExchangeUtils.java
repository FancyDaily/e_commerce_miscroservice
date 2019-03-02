package com.e_commerce.miscroservice.commons.utils;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/20 14:44
 * @Copyright 玖远网络
 */
public class IntegerTypeExchangeUtils {

    public static Integer shopMemberAccountTypeChange(Integer typeStatus) {

//        订单佣金类型
        if (typeStatus == 0 || typeStatus == 1 || typeStatus == 2) {
            typeStatus = 0;
        }
//管理奖类型
        if (typeStatus == 10 || typeStatus == 11) {
            typeStatus = 1;
        }
//提现
        if (typeStatus == 50 || typeStatus == 51 || typeStatus == 52) {
            typeStatus = 2;
        }
//6签到
        if (typeStatus == 20 || typeStatus == 21) {
            typeStatus = 3;
        }

        return typeStatus;
    }
}
