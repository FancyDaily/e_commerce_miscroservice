package com.e_commerce.miscroservice.user.vo;
/**
 * 功能描述:我报名的列表的返回list的view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年2月20日 下午4:16:07
 */

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.entity.application.TTypeDictionaries;

import java.io.Serializable;


public class SearchMyEnrollReturnView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 求助服务
	 */
	private TService service;
	/**
	 * 详情
	 */
	private TServiceDescribe serviceDesc;
	/**
	 * 报名信息
	 */
	private TTypeDictionaries enrollInfo;
	public TService getService() {
		return service;
	}
	public void setService(TService service) {
		this.service = service;
	}
	public TServiceDescribe getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(TServiceDescribe serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public TTypeDictionaries getEnrollInfo() {
		return enrollInfo;
	}
	public void setEnrollInfo(TTypeDictionaries enrollInfo) {
		this.enrollInfo = enrollInfo;
	}
	
}
