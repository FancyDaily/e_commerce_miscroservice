package com.xiaoshitimebank.app.view;

import java.io.Serializable;

import com.xiaoshitimebank.app.entity.TService;
import com.xiaoshitimebank.app.entity.TUser;

/**
 * 功能描述:求助服务分页返回值
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年10月31日 下午6:28:06
 */
public class PageServiceReturnView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户信息
	 */
	private PageServiceUserView user;
	/**
	 * 服务信息
	 */
	private TService service;
	/**
	 * 服务类型标签
	 */
	private String serviceType;
	/**
	 * 报名人数
	 */
	private Long enrollPeopleNum;
	/**
	 * 封面图地址
	 */
	private String imgUrl;
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getEnrollPeopleNum() {
		return enrollPeopleNum;
	}

	public void setEnrollPeopleNum(Long enrollPeopleNum) {
		this.enrollPeopleNum = enrollPeopleNum;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceIdString() {
		if (service != null) {
			return service.getId() + "";
		}
		return "";
	}
	
	public PageServiceUserView getUser() {
		return user;
	}
	public void setUser(PageServiceUserView user) {
		this.user = user;
	}
	public TService getService() {
		return service;
	}
	public void setService(TService service) {
		this.service = service;
	}
	
	

}
