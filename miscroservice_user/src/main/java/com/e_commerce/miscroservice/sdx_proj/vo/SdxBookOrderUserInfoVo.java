package com.e_commerce.miscroservice.sdx_proj.vo;

import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-28 10:18
 */
@Data
@Builder
@NoArgsConstructor
public class SdxBookOrderUserInfoVo {

	private String name;

	private String headPic;

	private String doneTimeDesc;

	private Long timeStamp;

	public void buildDoneTimeDesc(long timeStamp) {
		long currentInterval = System.currentTimeMillis() - timeStamp;
		boolean isToday = currentInterval < DateUtil.interval;
		boolean isYesterDay = !isToday && currentInterval < 2 * DateUtil.interval;
		//组装售出时间信息 分钟前、日期(如果为昨天，显示昨天）
		String desc;
		desc = isYesterDay ? "昨天" : isToday ? DateUtil.getMinsStrWithinOneDay(currentInterval) : DateUtil.timeStamp2Date(timeStamp);
		this.doneTimeDesc = desc;
	}
}
