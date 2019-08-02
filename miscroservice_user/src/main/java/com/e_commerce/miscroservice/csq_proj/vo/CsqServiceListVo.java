package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-27 17:24
 */
@Data(matchSuffix = true)
@Builder
public class CsqServiceListVo {
	@Id
	protected Long id;

	protected Long userId;

	protected Long fundId;

	protected String donePercent;	//捐献完成百分比

	@Transient
	protected Double sumTotalPayMine;	//此项目我的总计捐款(或项目已筹集多少金额

	@Transient
	protected Integer donaterCnt;	//捐助人数

	@Transient
	protected List<String> trendPubValues;

	@Column(commit = "基金状态")
	protected Integer fundStatus;

	@Column(commit = "类型(项目/基金【占位】)", length = 11, defaultVal = "0")
	protected Integer type;

	@Column(commit = "项目方向pubkeyId")
	protected String typePubKeys;

	@Column(commit = "项目名称")
	protected String name;	//MARK

	@Column(commit = "备案编号")
	protected String recordNo;

	@Column(commit = "状态", length = 11, defaultVal = "-1")
	protected Integer status;

	@Column(commit = "目的描述")
	protected String purpose;

	@Column(commit = "累积收到金额(已筹金额)", precision = 2, defaultVal = "0.00")
	protected Double sumTotalIn;

	@Column(commit = "累积捐助次数", precision = 11, defaultVal = "0")
	protected Integer totalInCnt;

	@Column(commit = "剩余金额", precision = 2, defaultVal = "0.00")
	protected Double surplusAmount;

	@Column(commit = "期望金额", precision = 2, defaultVal = "0.00")
	protected Double expectedAmount;

	@Column(commit = "距离期望还差多少金额", precision = 2, defaultVal = "0")
	protected Double expectedRemainAmount;

	@Column(commit = "开始日期")
	protected Long startDate;

	@Column(commit = "结束日期")
	protected Long endDate;

	@Column(commit = "封面图")
	protected String coverPic;

	@Column(commit = "描述")
	protected String description;	//MARK

	public TCsqService copyTCsqService() {
		return null;
	}
}
