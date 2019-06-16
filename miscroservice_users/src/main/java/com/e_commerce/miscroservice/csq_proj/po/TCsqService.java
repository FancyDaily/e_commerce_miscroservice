package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 项目
 * @Author: FangyiXu
 * @Date: 2019-06-10 10:01
 */
@Table(commit = "从善桥项目表")
@Data
@Builder
public class TCsqService extends BaseEntity {

	@Id
	private Long id;

	@Column(commit = "项目名称")
	private String name;

	@Column(commit = "备案编号")
	private Long recordId;

	@Column(commit = "状态", length = 11, defaultVal = "0")
	private Integer status;

	@Column(commit = "累积收到金额(已筹金额)", precision = 2, defaultVal = "0.00")
	private Double sumTotalIn;

	@Column(commit = "剩余金额", precision = 2, defaultVal = "0.00")
	private Double surplusAmount;

	@Column(commit = "目的描述")
	private String purpose;

	@Column(commit = "期望金额", precision = 2, defaultVal = "0.00")
	private Double expectedAmount;

	@Column(commit = "开始日期")
	private Long startDate;

	@Column(commit = "结束日期")
	private Long endDate;

	@Column(commit = "封面图")
	private Long coverPic;

	@Column(commit = "描述")
	private String desc;

	@Column(commit = "描述图")
	private String detailPic;

	@Column(commit = "受益人/机构")
	private String beneficiary;

	@Column(commit = "银行卡号")
	private String creditCard;


}
