package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;

/**
 * 功能描述:服务记录view类
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月17日 下午2:42:56
 */
public class ServiceRecordView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 服务记录内容
	 */
	private String value;
	/**
	 * 服务记录时间
	 */
	private Long createTime;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	

}
