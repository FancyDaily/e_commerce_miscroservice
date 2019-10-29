package com.e_commerce.miscroservice.commons.entity.colligate;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-09 17:13
 */
public class Page {

	private Integer pageNum;
	private Integer pageSize;

	public Integer getPageNum() {
		return pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Page() {
	}

	public Page(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}
}
