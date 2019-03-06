package com.e_commerce.miscroservice.user.vo;
/**
 * 功能描述:我报名的参数列表
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年2月20日 下午2:36:01
 */
public class SearchMyEnrollParamView {
	/**
	 * 分页参数页码
	 */
	Integer pageNum;
	/**
	 * 分页参数每页大小
	 */
	Integer pageSize;
	
	/**
	 * 我发布的类型 1、求助 2、服务
	 */
	Integer type;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
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
	
	
}
