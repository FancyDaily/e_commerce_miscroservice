package com.e_commerce.miscroservice.user.vo;


import com.e_commerce.miscroservice.commons.entity.application.TService;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月19日 下午6:52:38
 */
public class BaseServiceView extends TService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 需求类型
	 */
	private String serviceTypeName;
	/**
	 * 状态的String类型值，前端的导出功能使用，根据status显示具体的字符串
	 */
	private String statusString;
	
	public String getStatusString() {
		if (this.getStatus().equals(1)) {
			return "审核中";
		}
		if (this.getStatus().equals(2) || this.getStatus().equals(3) || this.getStatus().equals(4)) {
			return "已通过";
		}
		if (this.getStatus().equals(5) || this.getStatus().equals(6) || this.getStatus().equals(7)) {
			return "已结束";
		}
		if (this.getStatus().equals(9)) {
			return "审核未通过";
		}
		return "";
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}



	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}



	public String getIdString() {
		return super.getId() + "";
	}

}
