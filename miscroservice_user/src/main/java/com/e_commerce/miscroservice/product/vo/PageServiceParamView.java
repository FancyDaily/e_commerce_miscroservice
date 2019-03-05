package com.e_commerce.miscroservice.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述:分页求助服务接收参数的view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年10月31日 下午5:46:37
 */
@Data
public class PageServiceParamView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 服务场所的类型筛选
	 * 1、线上    2、线下   0、不限
	 */
	private Integer servicePlaceType;
	/**
	 * 根据时间筛选的状态
	 * 1、上午   2、下午  3、晚上   4、工作日  5、非工作日  0、不限
	 */
	private Integer serviceTimeType;
	/**
	 * 根据时间币筛选
	 * 1、小于一小时  2、1~3小时  3、3~5小时  4、5小时以上 0、不限
	 */
	private Integer collectTimeType;
	/**
	 * 服务求助类型  根据接口名controller添加  不需要前端传递
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

}
