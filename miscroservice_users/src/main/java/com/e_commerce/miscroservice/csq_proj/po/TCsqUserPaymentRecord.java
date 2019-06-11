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
@Table(commit = "从善桥流水表")
@Data
@Builder
public class TCsqUserPaymentRecord {

	@Id
	private Long id;

	private Long userId;

	private Long fundId;

	private Long serviceId;

	@Column(commit = "描述")
	private String desc;

	@Column(commit = "类型(爱心账户、基金、项目)", length = 11, isNUll = false)
	private Integer type;

	@Column(commit = "收入0/支出1", length = 11, isNUll = false)
	private Integer inOut;

	@Column(commit = "金额", precision = 2, isNUll = false)
	private Double money;

	@Column(commit = "扩展字段")
	private String extend;

	@Column(commit = "创建者编号", isNUll = false)
	private Long createUser;

	@Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Timestamp createTime;

	@Column(commit = "更新者编号", isNUll = false)
	private Long updateUser;

	@Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
	private Timestamp updateTime;

	@Column(commit = "有效性", defaultVal = "1")
	private String isValid;

}
