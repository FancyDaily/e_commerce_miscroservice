package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.product.vo.BaseUserView;
import lombok.Data;

import java.io.Serializable;


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
@Data
public class PageOrderReturnView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户信息
	 */
	private BaseUserView user;
	/**
	 * 订单信息
	 */
	private TOrder order;
	/**
	 * 服务类型标签
	 */
	private String serviceType;
	/**
	 * 封面图地址
	 */
	private String imgUrl;

	public String getOrderIdString() {
		if (order != null) {
			return order.getId() + "";
		}
		return "";
	}
}
