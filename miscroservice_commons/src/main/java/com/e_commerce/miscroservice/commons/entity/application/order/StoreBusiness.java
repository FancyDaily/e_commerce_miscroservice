package com.e_commerce.miscroservice.commons.entity.application.order;


import lombok.Data;

/**
 * <p>
 * 门店商家
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
@Data
public class StoreBusiness  {


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
	private Double cashIncome;
    /**
     * 提成百分比
     */
	private Double commissionPercentage;
    /**
     * 会员购物提成百分比
     */
	private Double memberCommissionPercentage;
    /**
     * 默认下级商家提成百分比
     */
	private Double defaultCommissionPercentage;
    /**
     * 上级商家号   例如(,1,3,5,6,)    首尾都有逗号
     */
	private String superBusinessIds;
    /**
     * 可提现余额
     */
	private Double availableBalance;
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
     * 组织机构号
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
	private Double storeArea;
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
     * 法人身份证号码
     */
	private String legalIdNumber;
	private Long protocolTime;
    /**
     * APP版本号
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
     * 商家公告
     */
	private String storeNotice;
    /**
     * VIP门店：0不是Vip门店，1是Vip门店
     */
	private Integer vip;
    /**
     * 商家优惠券核销人数
     */
	//@TableField("used_coupon_total_member_count")
	private Integer usedCouponTotalMemberCount;
    /**
     * 商家优惠券核销张数
     */
	//@TableField("used_coupon_total_count")
	private Integer usedCouponTotalCount;
    /**
     * 商家优惠券核销面值总金额（单位元）
     */
	//@TableField("used_coupon_total_money")
	private Double usedCouponTotalMoney;
    /**
     * 是否开通小程序：0未开通，1已开通
     */
	//@TableField("is_open_wxa")
	private Integer isOpenWxa;
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
	private Double rate;
    /**
     * 同步上新按钮状态 0：关闭  1：开启
     */
	private Integer synchronousButtonStatus;
	
//	private boolean firstStore;//是否是一级门店
//	
//	private String receiverName;
//	
//	private String telephone;
//	
//	private String superStoreNameAndNumber;//上级门店名称及号码
//	
//	private long superBusinessNumber;//上级门店商家号
//	
//	private String addrFull;
	
	/**
	 * 供应商ID
	 */
    private Long supplierId;
    /**
     * 首次登录状态 0:老用户,1:首次审核通过登录,2:非首次登录
     */
    //@TableField("first_login_status")
    private Integer firstLoginStatus;
    
    /**
     * 门店头图,英文逗号分隔
     */
    //@TableField("store_display_images")
	private String storeDisplayImages;
    
    /**
     * 第一阶段结束时间
     */
	//@TableField("one_stage_time")
	private Integer oneStageTime;
    /**
     * 第二阶段结束时间
     */
	//@TableField("two_stage_time")
	private Integer twoStageTime;
    /**
     * 第三阶段结束时间
     */
	//@TableField("three_stage_time")
	private Integer threeStageTime;
    /**
     * 地推用户id
     */
	//@TableField("ground_user_id")
	private Long groundUserId;
    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
	//@TableField("super_ids")
	private String superIds;
  
    /**
     * 0：提交审核 1：审核通过   -1:审核不通过 -2:禁用
     */
	//@TableField("audit_status")
	private Integer auditStatus;
    /**
     * 0:未第一次审核通过；>0审核通过时间
     */
	//@TableField("audit_time")
	private Long auditTime;
    /**
     * 0：提交审核 1：审核通过  2 : 未提交  -1:审核不通过 -2:禁用
     */
	//@TableField("data_audit_status")
	private Integer dataAuditStatus;
    /**
     * 0:未第一次审核通过；>0审核通过时间
     */
	//@TableField("data_audit_time")
	private Long dataAuditTime;
    /**
     * 0:未激活；>0激活时间
     */
	//@TableField("activation_time")
	private Long activationTime;
	/**
     * 推荐人手机号码
     */
	//@TableField("ground_user_phone")
	private String groundUserPhone;
	/**
     * 推荐人姓名
     */
	//@TableField("ground_user_name")
	private String groundUserName;
	
	
	/**
	 * 女装风格
	 */
	//@TableField("store_style")
	private String storeStyle;
	/**
	 * 店铺面积
	 */
	//@TableField("store_area_scope")
	private Integer storeAreaScope;
	/**
	 * 年龄范围
	 */
	//@TableField("store_age")
	private String storeAge;
	
	/**
	 * 小程序首页是否小时文章模块：0不显示、1显示
	 */
	//@TableField("wxa_article_show")
	private Integer wxaArticleShow;
	
	/**
	 * 问候图片
	 */
	//@TableField("greeting_image")
	private String greetingImage;
	
	/**
	 * 问候语
	 */
	//@TableField("greeting_words")
	private String greetingWords;
	

	
	
	
	/**
	 * 问候消息发送类型:0:没有设置;1:问候语;2:问候图片
	 */
	//@TableField("greeting_send_type")
	private Integer greetingSendType;
	
	/**
	 * 小程序预约试穿开关：0：关闭；1：开启
	 */
	//@TableField("shop_reservations_order_switch")
	private Integer shopReservationsOrderSwitch;
	 
	/**
	 * 线上小程序版本
	 */
	//@TableField("online_wxa_version")
	private String onlineWxaVersion;
	/**
	 * 线上小程序版本
	 */
	//@TableField("has_hotonline")
	private Integer hasHotonline;
	/**
	 * 线上小程序版本
	 */
	//@TableField("hot_online")
	private String hotOnline;

	
	/**
     * 小程序服务开通时间
     */
	//@TableField("wxa_open_time")
	private Long wxaOpenTime;
	/**
     * 小程序服务使用截止时间
     */
	//@TableField("wxa_close_time")
	private Long wxaCloseTime;
	/**
     * 小程序服务续约保护截止时间
     */

	//@TableField("wxa_renew_protect_close_time")
	private Long wxaRenewProtectCloseTime;

	/**
	 * @see: 进货渠道
	 */
	//@TableField("purchase_channel")
	private String purchaseChannel;

	/**
	 * @see: 价格档次
	 */
	//@TableField("price_level")
	private String priceLevel;

}
