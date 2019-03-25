package com.e_commerce.miscroservice.user.vo;


import com.e_commerce.miscroservice.commons.entity.application.TGroup;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年1月16日 下午2:43:09
 */
public class BaseGroupView extends TGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4226493331327630638L;
	/**
	 * 该组织下的人数
	 */
	private Integer personNum = 0;

	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
	
	public String getIdString() {
		return getId() + "";
	}
	
	
	
}
