package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 功能描述:以人分组显示服务列表
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月6日 上午11:18:02
 */
public class PageServiceGroupReturnView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PageServiceUserView user;
	
	private List<BaseServiceView> listService;
	
	public PageServiceUserView getUser() {
		return user;
	}

	public void setUser(PageServiceUserView user) {
		this.user = user;
	}

	public List<BaseServiceView> getListService() {
		return listService;
	}

	public void setListService(List<BaseServiceView> listService) {
		this.listService = listService;
	}

	
	
	
}
