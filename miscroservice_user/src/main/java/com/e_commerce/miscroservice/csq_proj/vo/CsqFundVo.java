package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:02
 */
@Data(matchSuffix = true)
public class CsqFundVo {

	private boolean isDonated;	//该项目是否已捐助

	private Long serviceId;	//对应项目

	Integer contributeInCnt;	//贡献人次

	List<String> trendPubNames;	//关注方向(名)

	List<TCsqOrder> goToList;	//去向(捐助项目记录)

	private boolean mine;

	private Integer raiseStatus;	//筹备状态

	private String stationorgName;	//机构名称

	private String stationorgAddr;	//地址

	private String stationcontact;	//联系方式

	private String stationPersonIncharge;	//负责人

	private String stationcreditCardName;	//银行名字

	private String stationcreditCardId;	//银行卡号

	@Id
	private Long id;

	private Long userId;

	@Column(commit = "累积资助项目次数", defaultVal = "0")
	private Integer helpCnt;

	@Column(commit = "资金累积流入次数", precision = 11, defaultVal = "0")
	private Integer totalInCnt;

	@Column(commit = "关注方向(publish表对于id")
	private String trendPubKeys;	//MARK

	@Column(commit = "基金名")
	private String name;	//MARK

	@Column(commit = "描述")
	private String description;	//MARK

	@Column(commit = "详细图")
	private String detailPic;

	@Column(commit = "封面图")
	private String coverPic;

	@Column(commit = "机构名称")
	private String orgName;	//MARK

	@Column(commit = "地址")
	private String orgAddr;	//MARK

	@Column(commit = "联系方式")
	private String contact;	//MARK

	@Column(commit = "负责人")
	private String personInCharge;	//MARK

	@Column(commit = "银行名字")
	private String creditCardName;

	@Column(commit = "银行卡号")
	private String creditCardId;

	@Column(commit = "余额", precision = 2, defaultVal = "0")
	private Double balance;

	@Column(commit = "资金累积总收入", precision = 2, defaultVal = "0")
	private Double sumTotalIn;

	@Column(commit = "托管状态(0未托管，1托管)", defaultVal = "0")
	private Integer agentModeStatus;

	@Column(commit = "状态(未激活、未公开、审核中、已公开、审核未通过)", defaultVal = "-1")
	private Integer status;	//MARK

	public TCsqFund copyTCsqFund() {
		return null;
	}

}
