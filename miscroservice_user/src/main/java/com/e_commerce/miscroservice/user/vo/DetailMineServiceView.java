package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.order.po.TService;
import com.e_commerce.miscroservice.order.po.TServiceDescribe;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述:我发布的服务详情
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月8日 下午6:06:49
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class DetailMineServiceView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 服务
	 */
	private TService service;
	/**
	 * 服务详情
	 */
	private List<TServiceDescribe> serviceDesc;
	/**
	 * 候选人
	 */
	private List<DetailSeekHelpUserView> listCandidate;
	
	/**
	 * 确定的人选
	 */
	private List<BaseUserView> listServant;
	
	/**
	 * 服务类型
	 */
	private String serviceType;
	
	
	
	public List<DetailSeekHelpUserView> getListCandidate() {
		return listCandidate;
	}

	public List<BaseUserView> getListServant() {
		return listServant;
	}

	public void setListServant(List<BaseUserView> listServant) {
		this.listServant = listServant;
	}

	public void setListCandidate(List<DetailSeekHelpUserView> listCandidate) {
		this.listCandidate = listCandidate;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String serviceIdString() {
		if (service != null) {
			return service.getId() + "";
		}
		return "";
	}
	
	public TService getService() {
		return service;
	}
	public void setService(TService service) {
		this.service = service;
	}
	public List<TServiceDescribe> getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(List<TServiceDescribe> serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	
	

}
