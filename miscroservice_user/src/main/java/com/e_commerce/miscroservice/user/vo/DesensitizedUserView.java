package com.e_commerce.miscroservice.user.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月10日 下午4:48:47
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Data
@Getter
@Setter
public class DesensitizedUserView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idString;	//字符型ID
	
	private Integer age;

	private Integer isAtten;	//0未关注 1已关注 (2.18后改为互关标记)

	private Integer authStatus; // 编辑基本信息状态

	private String vxId; // 微信id

	private String masterStatus; // 达人标示

	private String maxLevelMin;

	private String levelName;

	private String needGrowthNum;

	private String nextLevelName;

	private boolean JoinCompany; // 是否加入组织

	private String timeStamp; // 分页的时间戳（请求参数）

	private Long id;

	private String userAccount;

	private String name;

	private Integer jurisdiction;

	private String userHeadPortraitPath;

	private String userPicturePath;

	private String vxOpenId;

	private String occupation;

	private String workPlace;	//公司

	private String college;	//学校

	private Long birthday;

	private Integer sex;

	private String maxEducation;

	private Integer followNum;

	private Integer receiptNum;

	private String remarks;

	private Integer level;

	private Long growthValue;

	private Integer seekHelpNum;

	private Integer serveNum;

	private Integer seekHelpDoneNum;	//完成求助次数

	private Integer serveDoneNum;	//完成服务次数

	private Long surplusTime;

	private Long freezeTime;

	private Long creditLimit;	//授信额度

	private Long publicWelfareTime;

	private Integer authenticationStatus;

	private Integer authenticationType;

	private Integer totalEvaluate;

	private Integer creditEvaluate;

	private Integer majorEvaluate;

	private Integer attitudeEvaluate;

	private String skill;

	private Integer integrity;

	private Integer isCompanyAccount;	//是否是组织账号

	private String userType;	//用户类型

	private String extend;

	private Long createUser;

	private String createUserName;

	private Long createTime;

	private Long updateUser;

	private String updateUserName;

	private Long updateTime;

	private String isValid;

	private String inviteCode;

	private String idStr;

}
