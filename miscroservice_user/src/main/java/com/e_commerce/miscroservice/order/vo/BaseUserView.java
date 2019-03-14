package com.e_commerce.miscroservice.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单查询用户的基本信息
 */
@Data
public class BaseUserView implements Serializable {

	/**
	 * 用户id
	 */
	private Long id;
	/**
	 * 用户名称
	 */
	private String name;
	/**
	 * 用户头像
	 */
	private String userHeadPortraitPath;
	/**
	 * 用户服务次数
	 */
	private Integer serveNum;
	/**
	 * 总评价
	 */
	private Integer totalEvaluate;
	/**
	 * 用户技能
	 */
	private String skill;
	/**
	 * 是否是达人
	 */
	private Integer masterStatus;
	/**
	 * 用户职业
	 */
	private String occupation;
	/**
	 * 关注状态 1、显示关注 2、显示已关注
	 */
	private Integer careStatus;
	/**
	 * 订单详情中用户小点的显示状态  1、未到  、2、异常  3、已到
	 */
	private Integer pointStatus;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 用户手机号
	 */
	private String userTel;
	/**
	 * 性别  0 未知  1、男 2、女
	 */
	private Integer sex;
	/**
	 * 信息完整度
	 */
	private Integer integrity;
	/**
	 *  实名状态 1、未实名 2、已实名
	 */
	private Integer authenticationStatus;
	/**
	 * 用户类型：用户性质(1个人，2公益组织、3一般组织)
	 */
	private String userType;

	/**
	 * 获取字符串类型ID
	 *
	 * @return
	 */
	public String getIdString() {
		return id + "";
	}
}
