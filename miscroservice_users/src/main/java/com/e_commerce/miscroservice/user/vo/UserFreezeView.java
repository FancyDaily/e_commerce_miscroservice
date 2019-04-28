package com.e_commerce.miscroservice.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月8日 下午3:32:15
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Data
public class UserFreezeView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serviceIdString; // String化 服务Id

	private Long serviceId; // 服务Id

	private String orderIdString;	//String化 订单Id

	private Long orderId;	//订单Id

	private Long userId; // 服务发布者Id

	private String serviceName; // 服务名称

	private Integer type; // 服务类型

	private Integer status; // 订单对应的服务进程

	private Integer servicePersonnel; // 人数

	private Long startTime; // 起

	private Long endTime; // 止

	private String addressName; // 地址

	private Long freezeTime; // 冻结时间

	private Integer source;

	private Long serviceTypeId;

	private Integer servicePlace;

	private String labels;

	private Integer timeType;

	private String dateWeek;

	private Double longitude;

	private Double latitude;

	private Double radius;

	private Integer collectType;

	private Long collectTime;

	private String extend;

	private Long createUser;

	private String createUserName;

	private Long createTime; // 可能的分页依据

	private Long updateUser;

	private String updateUserName;

	private Long updateTime;

	private String isValid;
	
}
