package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-27 17:24
 */
@Data(matchSuffix = true)
public class CsqServiceVo {
	@Id
	private Long id;

	private Long userId;

	private Long fundId;

	@Transient
	private Integer donaterCnt;	//捐助人数

	@Transient
	private List<CsqBasicUserVo> donaters;	//捐助人列表

	@Transient
	private Double sumTotalOut;

	@Transient
	private List<String> trendPubValues;

	@Transient
	private List<TCsqUserPaymentRecord> csqUserPaymentRecords;

	@Transient
	private List<TCsqServiceReport> reports;

	@Column(commit = "基金状态")
	private Integer fundStatus;

	@Column(commit = "类型(项目/基金【占位】)", length = 11, defaultVal = "0")
	private Integer type;

	@Column(commit = "项目方向pubkeyId")
	private String typePubKeys;

	@Column(commit = "项目名称")
	private String name;

	@Column(commit = "备案编号")
	private Long recordId;

	@Column(commit = "状态", length = 11, defaultVal = "-1")
	private Integer status;

	@Column(commit = "目的描述")
	private String purpose;

	@Column(commit = "累积收到金额(已筹金额)", precision = 2, defaultVal = "0.00")
	private Double sumTotalIn;

	@Column(commit = "累积捐助次数", precision = 11, defaultVal = "0")
	private Integer totalInCnt;

	@Column(commit = "剩余金额", precision = 2, defaultVal = "0.00")
	private Double surplusAmount;

	@Column(commit = "期望金额", precision = 2, defaultVal = "0.00")
	private Double expectedAmount;

	@Column(commit = "距离期望还差多少金额", precision = 2, defaultVal = "0")
	private Double expectedRemainAmount;

	@Column(commit = "开始日期")
	private Long startDate;

	@Column(commit = "结束日期")
	private Long endDate;

	@Column(commit = "封面图")
	private String coverPic;

	@Column(commit = "描述")
	private String description;

	@Column(commit = "描述图")
	private String detailPic;

	@Column(commit = "受益人/机构")
	private String beneficiary;

	@Column(commit = "银行卡号")
	private String creditCard;

	public TCsqService copyTCsqService() {
		return null;
	}
}
