package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.order.po.TService;
import com.e_commerce.miscroservice.order.po.TServiceDescribe;

import java.util.List;


/**
 * 功能描述:服务求助详情返回view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月3日 下午6:18:50
 */
public class DetailServiceReturnView {
	/**
	 * 不显示关注
	 */
	public final int CARE_STATUS_NOT_SHOW = 0;
	/**
	 * 显示关注
	 */
	public final int CARE_STATUS_SHOW_CARE = 1;
	/**
	 * 显示已关注
	 */
	public final int CARE_STATUS_SHOW_ALREADY_CARE = 2;
	
	/**
	 * 收藏状态为0未收藏
	 */
	public final int COLLECT_STATUS_NOT_COLLECT = 0;
	/**
	 * 收藏状态为1已收藏
	 */
	public final int COLLECT_STATUS_ALREADY_COLLECT = 1;
	
	/**
	 * 不显示帮助他
	 */
	public final int SHOW_HELP_STATUS_NOT_SHOW = 1;
	/**
	 * 显示帮助他
	 */
	public final int SHOW_HELP_STATUS_SHOW_HELP = 2;
	/**
	 * 显示已帮助
	 */
	public final int SHOW_HELP_STATUS_SHOW_HELPED = 3;
	/**
	 * 显示进行中
	 */
	public final int SHOW_HELP_STATUS_RUNNING = 4;
	/**
	 * 显示已完成
	 */
	public final int SHOW_HELP_STATUS_COMPLETED = 5;
	/**
	 * 显示已下架
	 */
	public final int SHOW_HELP_STATUS_SHOW_LOWERFRAME = 8;
	
	/**
	 * 服务求助
	 */
	private TService service;
	/**
	 * 服务求助发布者
	 */
	private DetailSeekHelpUserView user;
	/**
	 * 服务求助详细描述
	 */
	private List<TServiceDescribe> listServiceDescribe;
	/**
	 * 服务求助类型（key-value 中的value）
	 */
	private String serviceType;
	/**
	 * 关注状态 ： 0、用户自己看自己求助详情，不显示关注按钮  1、用户未登录或未关注  显示关注按钮  2、已经关注过该用户，显示已关注
	 */
	private Integer careStatus;
	/**
	 * 收藏状态：0、未收藏  1、已收藏
	 */
	private Integer collectStatus;
	/**
	 * 展示帮助他状态 1、不显示帮助他  2、显示帮助他   3、显示已帮助
	 */
	private Integer showHelpStatus = 2;
	
	public String getServiceIdString() {
		if (service != null) {
			return service.getId() + "";
		}
		return "";
	}
	/**
	 * 
	 * 功能描述:发布者id
	 * 作者:马晓晨
	 * 创建时间:2018年11月25日 下午11:53:01
	 * @return
	 */
	public String getServiceUserIdString() {
		if (service != null) {
			return service.getUserId() + "";
		}
		return "";
	}
	
	public Integer getShowHelpStatus() {
		return showHelpStatus;
	}

	public void setShowHelpStatus(Integer showHelpStatus) {
		this.showHelpStatus = showHelpStatus;
	}

	public String getServiceType() {
		return serviceType;
	}
	
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public TService getService() {
		return service;
	}

	public void setService(TService service) {
		this.service = service;
	}

	public DetailSeekHelpUserView getUser() {
		return user;
	}

	public void setUser(DetailSeekHelpUserView user) {
		this.user = user;
	}

	public List<TServiceDescribe> getListServiceDescribe() {
		return listServiceDescribe;
	}

	public void setListServiceDescribe(List<TServiceDescribe> listServiceDescribe) {
		this.listServiceDescribe = listServiceDescribe;
	}

	public Integer getCareStatus() {
		return careStatus;
	}

	public void setCareStatus(Integer careStatus) {
		this.careStatus = careStatus;
	}

	public Integer getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(Integer collectStatus) {
		this.collectStatus = collectStatus;
	}
	
	
	
}
