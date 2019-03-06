package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;

import java.io.Serializable;
import java.util.List;


/**
 * 功能描述:订单求助详情view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月16日 下午5:34:49
 */
public class OrderSeekHelpDetailView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 求助
	 */
	private TService service;
	/**
	 * 求助详情
	 */
	private List<TServiceDescribe> listDesc;
	/**
	 * 报名者或发布者
	 */
	private List<BaseUserView> listUser;
	/**
	 * 服务记录
	 */
	private List<ServiceRecordView> listRecord;
	/**
	 * 服务求助类型
	 */
	private String serviceType;
	/**
	 * 我的类型  1、我是求助者  2、我是服务者
	 */
	private Integer myType;
	/**
	 * 状态  2、待开始  7、进行中 8待支付 9、待评价  10、已完成 11、待对方评价 1、显示投诉中
	 */
	private Integer showStatus;
	/**
	 * 订单组id
	 */
	private String parent;
	/**
	 * 订单类型 1、发布生成的订单  2、接单生成的订单
	 */
	private Integer orderType;
	/**
	 * 是否可以显示待开始 
	 */
	private boolean showConfirmBegin;
	/**
	 * 是否可以显示确认结束
	 */
	private boolean showConfirmEnd;
	/**
	 * 是否可以显示确认支付
	 * 1、显示确认支付  2、不显示  3、显示未全部支付
	 */
	private Integer showConfirmPay;
	/**
	 * 显示存在人数 
	 */
	private Integer showPeopleNum;
	
	public Integer getShowPeopleNum() {
		return showPeopleNum;
	}


	public void setShowPeopleNum(Integer showPeopleNum) {
		this.showPeopleNum = showPeopleNum;
	}


	public boolean isShowConfirmBegin() {
		return showConfirmBegin;
	}


	public void setShowConfirmBegin(boolean showConfirmBegin) {
		this.showConfirmBegin = showConfirmBegin;
	}


	public boolean isShowConfirmEnd() {
		return showConfirmEnd;
	}


	public void setShowConfirmEnd(boolean showConfirmEnd) {
		this.showConfirmEnd = showConfirmEnd;
	}


	public Integer getShowConfirmPay() {
		return showConfirmPay;
	}


	public void setShowConfirmPay(Integer showConfirmPay) {
		this.showConfirmPay = showConfirmPay;
	}


	public Integer getOrderType() {
		return orderType;
	}


	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}


	public String getParent() {
		return parent;
	}


	public void setParent(String parent) {
		this.parent = parent;
	}


	public Integer getShowStatus() {
		return showStatus;
	}


	public void setShowStatus(Integer showStatus) {
		this.showStatus = showStatus;
	}


	public Integer getMyType() {
		return myType;
	}


	public void setMyType(Integer myType) {
		this.myType = myType;
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
	
	
	public List<ServiceRecordView> getListRecord() {
		return listRecord;
	}
	public void setListRecord(List<ServiceRecordView> listRecord) {
		this.listRecord = listRecord;
	}
	public List<BaseUserView> getListUser() {
		return listUser;
	}
	public void setListUser(List<BaseUserView> listUser) {
		this.listUser = listUser;
	}
	public TService getService() {
		return service;
	}
	public void setService(TService service) {
		this.service = service;
	}
	
	public List<TServiceDescribe> getListDesc() {
		return listDesc;
	}
	public void setListDesc(List<TServiceDescribe> listDesc) {
		this.listDesc = listDesc;
	}

}
