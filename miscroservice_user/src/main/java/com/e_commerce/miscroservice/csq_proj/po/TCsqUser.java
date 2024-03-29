package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:14
 */
@Table(commit = "从善桥用户表", charset = "utf8mb4")
@Data
@Builder
@NoArgsConstructor
public class TCsqUser extends BaseEntity {
	@Id
	private Long id;

	/**
	 * 我的捐书订单数量
	 */
	@Transient
	private Integer bookDonateOrderNum;

	/**
	 * 购物车数量
	 */
	@Transient
	private Integer trolleyNum;

	@Transient
	private String fundName;

	@Transient
	private Double totalDonate;    //累积捐助(项目详情)

	@Transient
	private Integer minutesAgo;

	@Transient
	private String uuid;

	@Transient
	private String token;

	@Column(commit = "密码")
	private String password;

	@Column(commit = "账号")
	private String userAccount;

	@Column(commit = "昵称", charset = "utf8mb4")
	private String name;

	@Column(commit = "手机号")
	private String userTel;

	@Column(commit = "权限", length = 11, defaultVal = "0")
	private Integer jurisdiction;

	@Column(commit = "头像")
	private String userHeadPortraitPath;

	@Column(commit = "主页背景图")
	private String userPicturePath;

	@Column(commit = "微博账号")
	private String weiboAccount;    //微博账号

	@Column(commit = "微信公众号")
	private String wechatPubAccount;    //微信账号

	@Column(commit = "微信openid")
	private String vxOpenId;

	@Column(commit = "微信号")
	private String vxId;

	//微信名、年龄、地区等基本信息
	@Column(commit = "职业")
	private String occupation;

	@Column(commit = "公司")
	private String workPlace;

	@Column(commit = "大学")
	private String college;

	@Column(commit = "年龄", length = 11)
	private Integer age;

	@Column(commit = "生日")
	private Long birthday;

	@Column(commit = "性别", length = 11, defaultVal = "0")
	private Integer sex;

	@Column(commit = "关注数", length = 11, defaultVal = "0")
	private Integer followNum;

	@Column(commit = "个人描述", charset = "utf8mb4")
	private String remarks;

	@Column(commit = "等级", length = 11, defaultVal = "1")
	private Integer level;

	@Column(commit = "成长值")
	private Long growthValue;

	@Column(commit = "捐款次数", length = 11, defaultVal = "0")
	private Integer payNum;

	@Column(commit = "累积捐款总额", length = 11, defaultVal = "0", precision = 2)
	private Double sumTotalPay;

	@Column(commit = "爱心账户余额", length = 11, defaultVal = "0", precision = 2)
	private Double surplusAmount;

	@Column(commit = "爱心账户状态", length = 11, defaultVal = "0")
	private Integer balanceStatus;

	@Column(commit = "实名认证状态", length = 11, defaultVal = "0")
	private Integer authenticationStatus;

	@Column(commit = "实名认证类型", length = 11, defaultVal = "1")
	private Integer authenticationType;

	@Column(commit = "技能")
	private String skill;    //unused

	@Column(commit = "完整度", length = 11, defaultVal = "0")
	private Integer integrity;    //unused

	@Column(commit = "大V标记状态", length = 11, defaultVal = "0")
	private Integer masterStatus;    //unused

	@Column(commit = "微信基本信息授权状态", length = 11, defaultVal = "0")
	private Integer authStatus;

	@Column(commit = "邀请码")
	private String inviteCode;    //unused

	@Column(commit = "可用状态", defaultVal = "1")
	private Integer availableStatus;

	@Column(commit = "账号类型(个人，组织 etc.)", length = 11, defaultVal = "1")
	private Integer accountType;

	@Column(commit = "管理员权限(非管理员、一般管理员 etc.)", length = 11, defaultVal = "0")
	private Integer maanagerType;

	@Column(commit = "是否为预注册账号", length = 11, defaultVal = "0")
	private Integer isFake;

	@Column(commit = "联系人")
	private String contactPerson;

	@Column(commit = "联系方式")
	private String contactNo;

	@Column(commit = "意向编号")
	private String trendPubKeys;

	@Column(commit = "旧用户编号")
	private Long oldId;

	@Column(commit = "邮箱")
	private String mail;

	@Column(commit = "书袋熊标记", defaultVal = "0")
	private Integer isSdx;

	@Column(commit = "兴趣")
	private String interests;

	@Column(commit = "书袋熊积分", length = 11, defaultVal = "0")
	private Integer sdxScores;

	@Transient
	private Double sdxScoreMoney;

	@Column(commit = "正元用户编号")
	private Long yunmaId;

	public CsqBasicUserVo copyCsqBasicUserVo() {
		return null;
	}
}
