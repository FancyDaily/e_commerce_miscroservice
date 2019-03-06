package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;

/**
 * 功能描述:查询我发布的参数view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年1月15日 下午6:01:56
 */
public class SearchMyPublishParamView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -904100475873738615L;
	/**
	 * 分页参数页码
	 */
	Integer pageNum;
	/**
	 * 分页参数每页大小
	 */
	Integer pageSize;
	/**
	 * 我发布的状态  0显示全部  默认为0是为了兼容小程序接口传递参数为0  否则为null会报错
	 */
	Integer status = 0;
	/**
	 * 我发布的类型 1、求助 2、服务
	 */
	Integer type;
	/**
	 * 开始时间
	 */
	Long startTime;
	/**
	 * 结束时间
	 */
	Long endTime;
	/**
	 * 搜索名称
	 */
	String keyName;
	
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	
}
