package com.e_commerce.miscroservice.user.vo;
/**
 * 功能描述:分页显示订单列表view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月10日 下午5:03:55
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */

import com.e_commerce.miscroservice.commons.entity.application.TServiceReceipt;
import com.e_commerce.miscroservice.order.po.TService;

import java.io.Serializable;


public class PageOrderReturnView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  账单列表
	 */
	private TServiceReceipt serviceReceipt;
	/**
	 * 求助服务
	 */
	private TService service;
	
	/**
	 * 显示的人数
	 */
	private Integer peopleNum;
	/**
	 * 订单类型 1、发布生成的订单  2、接单生成的订单
	 */
	private Integer orderType;
	/**
	 * 服务来源 1个人  2组织  组织发布的都是活动
	 */
	private Integer serviceSource;
	/**
	 * 服务场所 线上或线下 1、线上 2、线下
	 */
	private Integer servicePlace;
	/**
	 * 我的类型  1、我是求助者  2、我是发布者
	 */
	private Integer myType;
	/**
	 * 状态  2、待开始  7、进行中 8待支付 9、待评价  10、已完成 11、待对方评价 1、显示投诉中
	 */
	private Integer showStatus;
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
	
	public TService getService() {
		return service;
	}
	public void setService(TService service) {
		this.service = service;
	}
	public Integer getShowStatus() {
		return showStatus;
	}
	public void setShowStatus(Integer showStatus) {
		this.showStatus = showStatus;
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
	public Integer getMyType() {
		return myType;
	}
	public void setMyType(Integer myType) {
		this.myType = myType;
	}
	public Integer getServiceSource() {
		return serviceSource;
	}
	public void setServiceSource(Integer serviceSource) {
		this.serviceSource = serviceSource;
	}
	public Integer getServicePlace() {
		return servicePlace;
	}
	public void setServicePlace(Integer servicePlace) {
		this.servicePlace = servicePlace;
	}
	/**
	 * 
	 * 功能描述:订单id的String类型
	 * 作者:马晓晨
	 * 创建时间:2018年11月21日 上午11:03:17
	 * @return
	 */
	public String getServiceReceiptIdString() {
		if (serviceReceipt != null) {
			return serviceReceipt.getId() + "";
		}
		return "";
	}
	//组 id的string 类型
	public String getServiceParentString() {
		if (serviceReceipt != null) {
			return serviceReceipt.getParent() + "";
		}
		return "";
	}
	
	public String getServiceIdString() {
		if (serviceReceipt != null) {
			return serviceReceipt.getServiceId() + "";
		}
		return "";
	}
	
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(Integer peopleNum) {
		this.peopleNum = peopleNum;
	}

	public TServiceReceipt getServiceReceipt() {
		return serviceReceipt;
	}

	public void setServiceReceipt(TServiceReceipt serviceReceipt) {
		this.serviceReceipt = serviceReceipt;
	}
	
	
	

}
