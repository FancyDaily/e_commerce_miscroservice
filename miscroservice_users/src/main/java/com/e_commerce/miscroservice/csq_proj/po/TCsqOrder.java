package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 涉及应用外支付的订单表
 * @Author: FangyiXu
 * @Date: 2019-06-11 17:57
 */
@Table(commit = "应用外支付的订单表")
@Data
@Builder
public class TCsqOrder extends BaseEntity {

	@Id
	private Long id;

	private Long userId;

	private Long fundId;

	private Long serviceId;

	@Column(commit = "业务类型(爱心账户充值、申请基金支付、基金充值、项目捐赠 .etc)")
	private Integer type;

	@Column(commit = "订单编号")
	private String orderNo;

	@Column(commit = "订单金额", precision = 2, isNUll = false)
	private Double price;

	@Column(commit = "订单状态", length = 11, defaultVal = "1", isNUll = false)
	private Integer status;

	@Column(commit = "订单创建时间戳")
	private Long orderTime;

}
