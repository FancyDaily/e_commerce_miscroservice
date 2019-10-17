package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审批表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:58
 */
@Table(commit = "商品房表")
@Data
@Builder
@NoArgsConstructor
public class TLpglCert extends BaseEntity {

	@Column(commit = "商品房编号")
	private Long houseId;

	@Column(commit = "申请用户编号")
	private Long applyUserId;

	@Column(commit = "审批用户编号")
	private Long certUserId;

	@Column(commit = "客户报备编号")
	private Long customerInfoId;

	@Column(commit = "类型", defaultVal = "1")
	private Integer type;

	@Column(commit = "优惠价")
	private Double discountPrice;

	@Column(commit = "描述")
	private String description;

	@Column(commit = "状态", defaultVal = "0")
	private Integer status;

}
