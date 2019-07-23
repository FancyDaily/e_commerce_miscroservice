package com.e_commerce.miscroservice.xiaoshi_proj.product.util;

import lombok.Data;

/**
 * @author 马晓晨
 * @date 2019/3/15
 */
@Data
public class DateResult {
	private Long startTimeMill;
	private Long endTimeMill;
	//距离上一次开始结束时间所增加的天数
	private Integer days = 0;
}
