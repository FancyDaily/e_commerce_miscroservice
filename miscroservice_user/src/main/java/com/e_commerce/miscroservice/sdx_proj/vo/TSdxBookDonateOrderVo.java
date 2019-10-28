package com.e_commerce.miscroservice.sdx_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import lombok.Builder;
import lombok.Data;

/**
 * 捐书订单vo
 * @Author: FangyiXu
 * @Date: 2019-10-24 17:12
 */
@Data
@Builder
public class TSdxBookDonateOrderVo {

	@Column(commit = "多个书本编号")
	private String bookIds;

	@Column(commit = "书本信息编号")
	private Long bookInfoIds;

	@Column(commit = "状态")
	private Integer status;

	@Column(commit = "邮寄地址编号")
	private Long shippingAddressId;

	@Column(commit = "预计获得总积分")
	private Integer expectedTotalScores;

	@Column(commit = "实际获得积分")
	private Integer exactTotalScores;

	@Column(commit = "配送类型(邮寄、自送）", defaultVal = "1")
	private Integer shipType;

	@Column(commit = "书籍驿站编号")
	private Long bookStationId;

	public TSdxBookOrderPo copyTSdxBookOrder() {return null;}
}
