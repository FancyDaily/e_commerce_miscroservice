package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import com.e_commerce.miscroservice.user.po.TUser;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 充值捐助流水(普通账户、基金、项目)
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:55
 */
@Table(commit = "从善桥流水表")
@Data
@Builder
public class TCsqUserPaymentRecord extends BaseEntity {

	@Id
	private Long id;

	private Long userId;

	@Transient
	private TCsqUser user;

	private Long fundId;

	private Long serviceId;

	@Transient
	private String serviceName;

	@Transient
	private String date;

	@Column(commit = "描述")
	private String desc;

	@Column(commit = "类型(人、爱心账户、基金、项目 etc.)", length = 11, isNUll = false)
	private Integer fromType;

	@Column(commit = "副类型(人、爱心账户、基金、项目 etc.)", length = 11, isNUll = false)
	private Integer toType;
/*
	@Column(commit = "收入0/支出1", length = 11, isNUll = false)
	private Integer inOut;*/

	@Column(commit = "金额", precision = 2, isNUll = false)
	private Double money;


}
