package com.e_commerce.miscroservice.commons.util.colligate;

import java.math.BigDecimal;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-23 20:40
 */
public class NumberUtil {

	public static Double keep2Places(Double dailyIncome) {
		BigDecimal bigDecimal = new BigDecimal(dailyIncome);
		dailyIncome = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return dailyIncome;
	}
}
