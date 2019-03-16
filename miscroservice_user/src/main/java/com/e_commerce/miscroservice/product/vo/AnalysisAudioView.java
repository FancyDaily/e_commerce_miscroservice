package com.e_commerce.miscroservice.product.vo;

import lombok.Data;

/**
 * 解析语音的vo
 * @author 马晓晨
 * @date 2019/3/15
 */
@Data
public class AnalysisAudioView {
	//标题
	private String title;
	//开始日期
	private String startDateS;
	// 结束日期
	private String endDateS;
	// 开始时间
	private String startTimeS;
	// 结束时间
	private String endTimeS;
	// 经度
	private Double longitude;
	// 纬度
	private Double latitude;
	// 收取时间
	private Integer collectTime;
	// 地址名称
	private String addressName;
	// 发布类型 1、求助 2、服务
	private Integer type;
	//要求人数
	private Integer personCount;
	//  0、一次性  1、重复性周期
	private Integer timeType;
	//星期  1,2,3 此类
	private String dateWeekNumber;
	//开始时间 timeType=0 单次使用此时间
	private Long startTime;
	//结束时间 timeType=0 单次使用此时间
	private Long endTime;

}
