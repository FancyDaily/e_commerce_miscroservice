package com.e_commerce.miscroservice.user.vo;
/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月8日 下午10:29:07
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */

import com.e_commerce.miscroservice.commons.entity.application.TEvaluate;
import com.e_commerce.miscroservice.commons.entity.application.TService;

import java.io.Serializable;


public class PageRemarkView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户view
	 */
	private BaseUserView user;
	/**
	 * 评价view
	 */
	private TEvaluate evaluate;
	/**
	 * 关联service
	 */
	private TService service;
	
	public TService getService() {
		return service;
	}

	public void setService(TService service) {
		this.service = service;
	}

	public String getEvaluateIdString() {
		if (evaluate != null) {
			return evaluate.getId() + "";
		}
		return "";
 	}
	
	public String getServiceIdString() {
		if (service != null) {
			return service.getId() + "";
		}
		return "";
	}
	
	public BaseUserView getUser() {
		return user;
	}
	public void setUser(BaseUserView user) {
		this.user = user;
	}
	public TEvaluate getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(TEvaluate evaluate) {
		this.evaluate = evaluate;
	}
	
	
}
