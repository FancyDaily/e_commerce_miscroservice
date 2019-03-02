package com.e_commerce.miscroservice.commons.utils;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/9 15:02
 * @Copyright 玖远网络
 */
public class DecimalUtils{


    public static BigDecimal add(BigDecimal... source) {
        BigDecimal offset = BigDecimal.ZERO;
        for (BigDecimal decimal : source) {
            offset = offset.add (decimal);
        }
        return offset;
    }


}
