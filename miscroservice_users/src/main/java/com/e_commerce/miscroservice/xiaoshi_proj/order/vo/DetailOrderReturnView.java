package com.e_commerce.miscroservice.xiaoshi_proj.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import lombok.Data;

import java.util.List;


/**
 * 功能描述:订单详情view
 * @author 马晓晨
 * @date 2019/3/8 15:49
 */
@Data
public class DetailOrderReturnView {

	/**
	 * 服务求助
	 */
	private TOrder order;
	/**
	 * 服务求助发布者
	 */
	private BaseUserView user;
	/**
	 * 订单关系
	 */
	private TOrderRelationship orderRelationship;

	/**
	 * 服务信息
	 */
	private TService service;

	/**
	 * 服务总开始时间
	 */
	private String serviceStartTime;

	/**
	 * 服务结束时间
	 */
	private String serviceEndTime;

	/**
	 * 服务求助详细描述
	 */
	private List<TServiceDescribe> listServiceDescribe;
	/**
	 * 服务求助类型（key-value 中的value）
	 */
	private String serviceType;
	/**
	 * 封面图地址
	 */
	private String coverImgUrl;
	/**
	 * 可报名日期
	 */
	private String[] enrollDate;

	/**
	 * 再次报名标记
	 */
	private boolean alreadyEnroll;

	public String getOrderIdString() {
		if (order != null) {
			return order.getId() + "";
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
		if (order != null) {
			return order.getCreateUser() + "";
		}
		return "";
	}

}
