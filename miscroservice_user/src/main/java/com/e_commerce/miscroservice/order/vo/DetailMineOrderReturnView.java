package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TOrderRecord;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import lombok.Data;

import java.util.List;

/**
 * 我的订单的详情
 * @author 马晓晨
 * @date 2019/3/9
 */
@Data
public class DetailMineOrderReturnView {
	/**
	 * 订单信息
	 */
	private TOrder order;
	/**
	 * '订单的消息记录
	 */
	private List<TOrderRecord> record;
	/**
	 * 服务者或者报名者
	 */
	private List<BaseUserView> listUserView;
	/**
	 * 订单状态
	 */
	private String status;
	/**
	 * 详情
	 */
	private List<TServiceDescribe> listDesc;

}
