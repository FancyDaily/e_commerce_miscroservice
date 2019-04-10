package com.e_commerce.miscroservice.commons.entity.colligate;

import java.io.Serializable;

/**
 * 功能描述:EasyUI的ajax请求返回结果
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年12月17日 下午3:45:07
 */
public class EasyUIPageResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 总条数
	 */
	private Long total;
	/**
	 * 记录
	 */
	private Object rows;
	
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}
	
	
}
