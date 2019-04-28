package com.e_commerce.miscroservice.product.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 求助服务发布参数view
 */
@Data
public class ProductSubmitParamView implements Serializable {
	/** 发布类型 */
	private Integer type;
	/** 来源 */
	private Integer source;
	/** 类型ID */
	private Long serviceTypeId;
	/** 求助服务名称 */
	private String serviceName;
	/** 线上线下  1、线上 2、线下*/
	private Integer servicePlace;
	/** 标签 */
	private String labels;
	/** 所需人数 */
	private Integer servicePersonnel;
	/** 开始时间 */
	private Long startTime;
	/**
	 * 结束时间
	 */
	private Long endTime;
	/**
	 * 时间类型 0、指定时间  1、可重复
	 */
	private Integer timeType;
	/**
	 * 重复周几
	 */
	private String dateWeek;
	/**
	 * 地址名称
	 */
	private String addressName;
	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 	半径
	 */
	private Double radius;
	/**
	 * 收取类型 1、互助时 2、公益时
	 */
	private Integer collectType;
	/**
	 * 收取时长
	 */
	private Long collectTime;
	/**
	 * 标题音频的url
	 */
	private String nameAudioUrl;
	/**
	 * 组织id
	 */
	private Long companyId;
	/**
	 * 报名权限 1、公开对外  2、仅内部成员可见
	 */
	private Integer openAuth;
	/**
	 * 服务详情的json
	 */
	private String serviceDescJson;

}
