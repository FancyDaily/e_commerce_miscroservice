package com.e_commerce.miscroservice.xiaoshi_proj.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TEvaluate;
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
	 * 订单状态  0、无人报名 1、待支付 2、待开始 3、   4、待评价 5、待对方评价 6、已取消 7、被取消  8 已完成 9投诉中
	 */
	private Integer status;
	/**
	 * 举报行为 1、投诉 2、举报
	 */
	private Integer reportAction;
	/**
	 * 详情
	 */
	private List<TServiceDescribe> listDesc;
	/**
	 * 评价信息
	 */
	private List<TEvaluate> listEvalute;

}
