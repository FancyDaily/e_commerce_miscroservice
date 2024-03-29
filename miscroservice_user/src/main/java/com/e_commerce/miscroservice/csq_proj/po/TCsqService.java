package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.vo.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目
 * @Author: FangyiXu
 * @Date: 2019-06-10 10:01
 */
@Table(commit = "从善桥项目表", charset = "utf8mb4")
@Data
@Builder
@NoArgsConstructor
public class TCsqService extends BaseEntity {

	@Id
	private Long id;

	private Long userId;

	private Long fundId;

	@Transient
	private Long lastIncomeStamp;	//最后收入时间戳

	@Transient
	private Double sumTotalOut;

	@Transient
	private String donePercent;	//捐献完成进度

	@Transient
	private Double sumTotalPayMine;	//此项目我的总计捐款

	@Transient
	private Integer donaterCnt;	//捐助人数

	@Transient
	private List<CsqBasicUserVo> donaters;	//捐助人列表

	@Transient
	private List<String> trendPubValues;

	@Transient
	private List<TCsqUserPaymentRecord> csqUserPaymentRecords;

	@Transient
	private List<TCsqServiceReport> reports;

	@Transient
	private Integer relatedBookInfoNum;

	@Column(commit = "白名单")
	private String whiteList;

	@Column(commit = "基金状态")
	private Integer fundStatus;

	@Column(commit = "类型(项目/基金【占位】)", length = 11, defaultVal = "0")
	private Integer type;

	@Column(commit = "项目方向pubkeyId")
	private String typePubKeys;

	@Column(commit = "项目名称", charset = "utf8mb4")
	private String name;

	@Column(commit = "备案编号")
	private String recordNo;

	@Column(commit = "状态", length = 11, defaultVal = "-1")
	private Integer status;

	@Column(commit = "目的描述", charset = "utf8mb4")
	private String purpose;

	@Column(commit = "累积收到金额(已筹金额)", precision = 2, defaultVal = "0.00")
	private Double sumTotalIn;

	@Column(commit = "累积被捐助次数",  length = 11, defaultVal = "0")
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

	@Column(commit = "分享图")
	private String sharePic;

	@Column(commit = "描述", charset = "utf8mb4", length = 8192)
	private String description;

	@Column(commit = "描述图", length = 2048)
	private String detailPic;

	@Column(commit = "受益人/机构")
	private String beneficiary;

	@Column(commit = "银行卡号")
	private String creditCard;

	@Column(commit = "负责人")
	private String personInCharge;

	@Column(commit = "负责人职位")
	private String occupation;

	@Column(commit = "负责人头像")
	private String personInChargePic;

	@Column(commit = "身份证/机构代码")
	private String certificatedNo;

	@Column(commit = "可展示状态(首页),管理员关闭项目/基金后，将不可展示", defaultVal = "1")
	private Integer isShown;

	@Column(commit = "优先级", defaultVal = "0")
	private Integer priority;

	@Column(commit = "书袋熊相关", defaultVal = "0")
	private Integer isSdx;

	public CsqDailyDonateVo copyCsqDailyDonateVo() {
		return null;
	}

	public CsqServiceListVo copyCsqServiceListVo() {
		return null;
	}

	public CsqServiceDetailVo copyCsqServiceDetailVo() {
		return null;
	}

	public CsqSimpleServiceVo copyCsqSimpleServiceVo() {return null;}
}
