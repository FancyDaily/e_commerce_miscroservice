package com.e_commerce.miscroservice.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 门店商家
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author charlie
 * @version V1.0  
 * @date 2018年09月29日 下午 07:37:36
 */
@Data
public class StoreBusiness implements Serializable{
 
	private Long id;
 
	/**
	 * 商家号
	 */  
	private Long businessNumber;
 
	/**
	 * 商家名称
	 */  
	private String businessName;
 
	/**
	 * 公司名称
	 */  
	private String companyName;
 
	/**
	 * 账户状态0正常，-1删除，1 禁用
	 */  
	private Integer status;
 
	/**
	 * 创建时间
	 */  
	private Long createTime;
 
	/**
	 * 更新时间
	 */  
	private Long updateTime;
 
	/**
	 * 会员数
	 */  
	private Integer memberNumber;
 
	/**
	 * 0正常，1 禁用
	 */  
	private Integer distributionStatus;
 
	/**
	 * 手机号码
	 */  
	private String phoneNumber;
 
	/**
	 * 账户资金
	 */  
	private BigDecimal cashIncome;
 
	/**
	 * 提成百分比
	 */  
	private BigDecimal commissionPercentage;
 
	/**
	 * 会员购物提成百分比
	 */  
	private BigDecimal memberCommissionPercentage;
 
	/**
	 * 默认下级商家提成百分比
	 */  
	private BigDecimal defaultCommissionPercentage;
 
	/**
	 * 上级商家号   例如(,1,3,5,6,)    首尾都有逗号
	 */  
	private String superBusinessIds;
 
	/**
	 * 可提现余额
	 */  
	private BigDecimal availableBalance;
 
	/**
	 * 提现申请次数
	 */  
	private Integer withdrawApply;
 
	/**
	 * 商家地址
	 */  
	private String businessAddress;
 
	/**
	 * 营业执照号或者统一社会信用代码

	 */  
	private String licenseNumber;

	/**
	 * 组织机构号， 修改为推荐人手机号
	 */  
	private String organizationNo;

	/**
	 * 税务登记号
	 */  
	private String taxId;

	/**
	 * 法定代表人
	 */  
	private String legalPerson;

	/**
	 * 老板手机号码
	 */  
	private String legalPhone;

	/**
	 * 所在省份

	 */  
	private String province;
 
	/**
	 * 所在城市

	 */  
	private String city;
 
	/**
	 * 所在区县
	 */  
	private String county;
 
	/**
	 * 门店建筑面积
	 */  
	private BigDecimal storeArea;
 
	/**
	 * 门店描述
	 */  
	private String storeDescription;
 
	/**
	 * 门店LOGO

	 */  
	private String storeLogo;
 
	/**
	 * 门店展示图片
	 */  
	private String storeShowImgs;
 
	/**
	 * 门店全景展示图片
	 */  
	private String storePanoramaImg;
 
	/**
	 * 银行开户名称
	 */  
	private String bankAccountName;
 
	/**
	 * 开户银行名称
	 */  
	private String bankName;
 
	/**
	 * 银行账户号
	 */  
	private String bankAccountNo;
 
	/**
	 * 用户名
	 */  
	private String userName;

	/**
	 * 密碼
	 */
	private String userPassword;  
 
	/**
	 * 银行卡选择标记 0 未选中， 1选中
	 */  
	private Integer bankCardFlag;
 
	/**
	 * 支付宝选择标记 0 未选中， 1选中
	 */  
	private Integer alipayFlag;
 
	/**
	 * 支付宝账号
	 */  
	private String alipayAccount;
 
	/**
	 * 支付宝关联人姓名
	 */  
	private String alipayName;
 
	/**
	 * 微信选择标记 0 未选中， 1选中
	 */  
	private Integer weixinFlag;
 
	/**
	 * 微信账号
	 */  
	private String weixinAccount;
 
	/**
	 * 微信关联姓名
	 */  
	private String weixinName;
 
	/**
	 * 身份证号
	 */  
	private String idCardNumber;
 
	/**
	 * 商家类型 0企业 1个人
	 */  
	private Integer businessType;
 
	/**
	 * 提现密码
	 */  
	private String withdrawPassword;
 
	/**
	 * 门店等级 参考门店全局设置
	 */  
	private Integer grade;
 
	/**
	 * 营业时间
	 */  
	private String businessHours;
 
	/**
	 * 签约日期
	 */  
	private String signTime;
 
	/**
	 * 账号激活时间
	 */  
	private Long activeTime;
 
	/**
	 * 用户cid
	 */  
	private String userCID;
 
	/**
	 * 门店选择银行卡标记 0未选中， 1选中
	 */  
	private Integer bankCardUseFlag;
 
	/**
	 * 门店选择支付宝标记 0未选中， 1选中
	 */  
	private Integer alipayUseFlag;
 
	/**
	 * 门店选择微信标记 0未选中， 1选中
	 */  
	private Integer weixinUseFlag;
 
	/**
	 * 近期首次账户输入错误时间
	 */  
	private Long lastErrorWithdrawPasswordTime;
 
	/**
	 * 输错次数
	 */  
	private Integer errorCount;
 
	/**
	 * 门店层级
	 */  
	private Long deep;
 
	/**
	 * 微信登录ID
	 */  
	private String bindWeixinId;
 
	/**
	 * 绑定微信账号名称
	 */  
	private String bindWeixinName;
 
	/**
	 * 绑定微信账号头像URL
	 */  
	private String bindWeixinIcon;
 
	/**
	 * 协议时间
	 */  
	private Long protocolTime;
 
	/**
	 * 法人身份证号码
	 */  
	private String legalIdNumber;
 
	/**
	 * 已废弃,可随时删除
	 */  
	private String appId;
 
	/**
	 * 小程序appId
	 */  
	private String wxaAppId;
 
	/**
	 * 开发者微信号
	 */  
	private String weiXinNum;
 
	/**
	 * VIP商品：0不是Vip商品，1是Vip商品
	 */  
	private Integer vip;
 
	/**
	 * 商家公告
	 */  
	private String storeNotice;
 
	/**
	 * 是否开通小程序：0未开通(正常)，1已开通(正常), 2冻结(手工关闭)
	 */  
	private Integer isOpenWxa;
 
	/**
	 * 商家优惠券核销人数
	 */  
	private Integer usedCouponTotalMemberCount;
 
	/**
	 * 商家优惠券核销张数
	 */  
	private Integer usedCouponTotalCount;
 
	/**
	 * 商家优惠券核销面值总金额（单位元）
	 */  
	private BigDecimal usedCouponTotalMoney;
 
	/**
	 * 小程序类型：0个人、1企业小程序
	 */  
	private Integer wxaType;
 
	/**
	 * 资质证明图片，英文逗号分隔的字符串
	 */  
	private String qualificationProofImages;
 
	/**
	 * 倍率 保留一位小数
	 */  
	private BigDecimal rate;
 
	/**
	 * 同步上新按钮状态 0：关闭  1：开启
	 */  
	private Integer synchronousButtonStatus;
 
	/**
	 * 供应商ID
	 */  
	private Long supplierId;
 
	/**
	 * 0:老用户,1:首次审核通过登录,2:非首次登录
	 */  
	private Integer firstLoginStatus;
 
	/**
	 * 门店头图,英文逗号分隔
	 */  
	private String storeDisplayImages;
 
	/**
	 * 第一阶段结束时间
	 */  
	private Integer oneStageTime;
 
	/**
	 * 第二阶段结束时间
	 */  
	private Integer twoStageTime;
 
	/**
	 * 第三阶段结束时间
	 */  
	private Integer threeStageTime;
 
	/**
	 * 地推用户id
	 */  
	private Long groundUserId;
 
	/**
	 * 所有上级ids,例如(,1,3,5,6,)
	 */  
	private String superIds;
 
	/**
	 * 0:未激活；>0激活时间
	 */  
	private Long activationTime;
 
	/**
	 * 0：提交审核 1：审核通过   -1:审核不通过 -2:禁用
	 */  
	private Integer auditStatus;
 
	/**
	 * 0:未第一次审核通过；>0审核通过时间
	 */  
	private Long auditTime;
 
	/**
	 * 推荐人手机号码
	 */  
	private String groundUserPhone;
 
	/**
	 * 推荐人姓名
	 */  
	private String groundUserName;
 
	/**
	 * 女装风格
	 */  
	private String storeStyle;
 
	/**
	 * 年龄定位
	 */  
	private String storeAge;
 
	/**
	 * 店铺面积
	 */  
	private String storeAreaScope;
 
	/**
	 * 小程序首页是否小时文章模块：0不显示、1显示
	 */  
	private Integer wxaArticleShow;
 
	/**
	 * 问候图片
	 */  
	private String greetingImage;
 
	/**
	 * 问候语
	 */  
	private String greetingWords;
 
	/**
	 * 问候消息发送类型:0:没有设置;1:问候语;2:问候图片
	 */  
	private Integer greetingSendType;
 
	/**
	 * 小程序预约试穿开关：0：关闭；1：开启
	 */  
	private Integer shopReservationsOrderSwitch;
 
	/**
	 * 线上小程序版本：例子：1.1.1
	 */  
	private String onlineWxaVersion;
 
	/**
	 * 是否开启客服热线 0：关闭 1：开启
	 */  
	private Integer hasHotonline;
 
	/**
	 * 热线电话
	 */  
	private String hotOnline;
 
	/**
	 * 小程序服务开通时间
	 */  
	private Long wxaOpenTime;
 
	/**
	 * 小程序服务使用截止时间
	 */  
	private Long wxaCloseTime;
 
	/**
	 * 小程序服务续约保护截止时间
	 */  
	private Long wxaRenewProtectCloseTime;
 
	/**
	 * 进货渠道 字典
	 */  
	private String purchaseChannel;
 
	/**
	 * 价格水平
	 */  
	private String priceLevel;
 
	/**
	 * 0：提交审核 1：审核通过  2 : 未提交  -1:审核不通过 -2:禁用
	 */  
	private Integer dataAuditStatus;
 
	/**
	 * 0:未第一次审核通过；>0审核通过时间
	 */  
	private Long dataAuditTime;
 
	/**
	 * 认证系统id
	 */
	private Long authId;

	/**
	 * 店中店openId
	 */
	private String inShopOpenId;

	/**
	 * 店中店的memberId
	 */
	private Long inShopMemberId;

	/**
	 * 当前小程序的店铺状态
	 * 小程序店铺类型 0从未开通,1共享版(过期不设为0),2专项版(过期不设为0)
	 */
	private Integer wxaBusinessType;



 }