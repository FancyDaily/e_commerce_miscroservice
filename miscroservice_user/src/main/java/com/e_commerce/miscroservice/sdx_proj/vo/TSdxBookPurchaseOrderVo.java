package com.e_commerce.miscroservice.sdx_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import lombok.Builder;
import lombok.Data;

/**
 * 购书订单编号
 * @Author: FangyiXu
 * @Date: 2019-10-24 17:12
 */
@Data
@Builder
public class TSdxBookPurchaseOrderVo {

	@Column(commit = "快递单号")
	private String expressNo;

	@Column(commit = "多个书本编号")
	private String bookIds;

	@Column(commit = "书本信息编号")
	private Long bookInfoIds;

	@Column(commit = "状态")
	private Integer status;

	@Column(commit = "邮寄地址编号")
	private Long shippingAddressId;

	@Column(commit = "书本总价", precision = 2)
	private Double bookPrice;

	@Column(commit = "抵扣积分总额", length = 11)
	private Integer scoreUsed;

	@Column(commit = "积分抵扣总金额", precision = 2)
	private Double scoreDiscountPrice;

	@Column(commit = "运费")
	private Double shipPirce;

	@Column(commit = "订单总价")
	private Double totalPrice;

	@Column(commit = "总价", precision = 2)
	private Double price;

	public TSdxBookOrderPo copyTSdxBookOrder() {return null;}
}
