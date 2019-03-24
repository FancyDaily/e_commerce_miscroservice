package com.e_commerce.miscroservice.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页显示参数列表
 * @author 马晓晨
 */
@Data
public class PageOrderParamView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 服务求助类型
	 * 1、求助  2、服务
	 */
	private Integer type;
	
	/**
	 * 类型id  key-value数据库中的值 直接id值进行查询
	 */
	private Integer serviceTypeId;
	
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 * 每页数量
	 */
	private Integer pageNum;
	/**
	 * 每页的数量
	 */
	private Integer pageSize;
	/**
	 * 用户所加入的组织
	 */
	private List<Long> userCompanyIds;
	/**
	 * 查询条件
	 */
	private String condition;
	/**
	 * 当前用户ID，用于查询时的排序
	 */
	private Long currentUserId;
	/**
	 * 组织用户ID --> 用来查询我发布的订单列表  “选人”
	 */
	private Long companyUserId;

}
