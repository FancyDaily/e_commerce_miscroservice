package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.order.po.TOrder;
import com.e_commerce.miscroservice.order.po.TOrderRelationship;
import com.e_commerce.miscroservice.order.po.TServiceDescribe;
import com.e_commerce.miscroservice.product.vo.BaseUserView;
import lombok.Data;

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

	public String getServiceIdString() {
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
