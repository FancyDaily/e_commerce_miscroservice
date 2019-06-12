package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 充值捐助流水(普通账户、基金、项目)
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:55
 */
@Table
@Data
@Builder
public class CsqUserPaymentRecord  extends BaseEntity{

	@Id
	private Long id;

	private Long userId;

	private Long fundId;

	private Long serviceId;

	@Column(commit = "描述")
	private String desc;

	@Column(commit = "类型(爱心账户、基金、项目)")
	private Integer type;

	@Column(commit = "收入0/支出1")
	private Integer inOut;

	@Column(commit = "金额")
	private Long money;



}
