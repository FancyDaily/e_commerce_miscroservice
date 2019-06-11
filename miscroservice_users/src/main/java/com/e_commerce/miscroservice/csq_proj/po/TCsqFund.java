package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 基金账户
 *
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:47
 */
@Table(commit = "从善桥专项基金表")
@Data
@Builder
public class TCsqFund {

	@Id
	private Long id;

	private Long userId;

	@Column(commit = "关注方向(publish表对于id")
	private Integer trendPubKey;

	@Column(commit = "名称")
	private String orgName;

	@Column(commit = "地址")
	private String orgAddr;

	@Column(commit = "联系方式")
	private String contact;

	@Column(commit = "负责人")
	private String personInCharge;

	@Column(commit = "银行名字")
	private String creditCardName;

	@Column(commit = "银行卡号")
	private String creditCardId;

	@Column(commit = "余额", precision = 2, defaultVal = "0")
	private Double balance;

	@Column(commit = "托管状态(0未托管，1托管)", defaultVal = "0")
	private Integer agentModeStatus;

	@Column(commit = "状态(未公开、审核中、已公开、审核未通过)", defaultVal = "0")
	private Integer status;

	@Column(commit = "累积资助项目次数", defaultVal = "0")
	private Integer helpCnt;

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
