package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author nijin
 * @since 2017-12-18
 */
//@TableName("supplier_user")
@Data
public class UserNew  {

	private static final Logger logger = LoggerFactory.getLogger(UserNew.class);
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	//@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 头像
     */
	private String avatar;
    /**
     * 账号
     */
	private String account;
    /**
     * 密码
     */
	private String password;
    /**
     * md5密码盐
     */
	private String salt;
	/**
	 * 商品权限
	 */
	//@TableField("product_permission")
	private String productPermission;
    /**
     * 名字
     */
	private String name;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
	private Integer sex;
    /**
     * 电子邮件
     */
	private String email;
    /**
     * 电话
     */
	private String phone;
    /**
     * 角色id
     */
	private String roleid;
    /**
     * 部门id
     */
	private Integer deptid;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
	private Integer status;
    /**
     * 创建时间
     */
	private Date createtime;
    /**
     * 保留字段
     */
	private Integer version;
    /**
     * 支付宝关联人姓名
     */
	//@TableField("alipay_name")
	private String alipayName;
    /**
     * 商家名称
     */
	//@TableField("business_name")
	private String businessName;
    /**
     * 公司名称
     */
	//@TableField("company_name")
	private String companyName;
    /**
     * 商家地址
     */
	//@TableField("business_address")
	private String businessAddress;
    /**
     * 营业执照号或者统一社会信用代码
     */
	//@TableField("license_number")
	private String licenseNumber;
    /**
     * 税务登记号
     */
	private String taxid;
    /**
     * 法定代表人
     */
	//@TableField("legal_person")
	private String legalPerson;
    /**
     * 身份证号码
     */
	//@TableField("id_card_number")
	private String idCardNumber;
    /**
     * 所在省份
     */
	private String province;
    /**
     * 所在城市
     */
	private String city;
    /**
     * 银行卡选择标记 0 未选中， 1选中
     */
	//@TableField("bank_card_flag")
	private Integer bankCardFlag;
    /**
     * 银行开户名称
     */
	//@TableField("bank_account_name")
	private String bankAccountName;
    /**
     * 开户银行名称
     */
	//@TableField("bank_name")
	private String bankName;
    /**
     * 银行开户号
     */
	//@TableField("bank_account_no")
	private String bankAccountNo;
    /**
     * 支付宝选择标记 0 未选中， 1选中
     */
	//@TableField("alipay_flag")
	private Integer alipayFlag;
    /**
     * 支付宝账号
     */
	//@TableField("alipay_account")
	private String alipayAccount;
    /**
     * 品牌仓库Id
     */
	//@TableField("lowarehouse_id")
	private Long lowarehouseId;
    /**
     * 关联品牌Id
     */
	//@TableField("brand_id")
	private Long brandId;
    /**
     * 更新时间
     */
	private Long updatetime;
    /**
     * 分销门店数
     */
	//@TableField("store_number")
	private Integer storeNumber;
    /**
     * 账户资金
     */
	//@TableField("cash_income")
	private Double cashIncome;
    /**
     * 可提现余额
     */
	//@TableField("available_balance")
	private Double availableBalance;
    /**
     * 组织机构号
     */
	//@TableField("organization_no")
	private String organizationNo;
    /**
     * 品牌描述
     */
	//@TableField("brand_description")
	private String brandDescription;
    /**
     * 品牌LOGO
     */
	//@TableField("brand_logo")
	private String brandLogo;
    /**
     * 品牌展示图片
     */
	//@TableField("brand_show_imgs")
	private String brandShowImgs;
    /**
     * 提成百分比
     */
	//@TableField("commission_percentage")
	private Double commissionPercentage;
    /**
     * 结算日期
     */
	//@TableField("settlement_date")
	private Integer settlementDate;
    /**
     * 微信选择标记 0 未选中， 1选中
     */
	//@TableField("weixin_flag")
	private Integer weixinFlag;
    /**
     * 微信账号
     */
	//@TableField("weixin_account")
	private String weixinAccount;
    /**
     * 微信关联姓名
     */
	//@TableField("weixin_name")
	private String weixinName;
    /**
     * 提款设置 0 所有费用合并提款， 1不同款项分开取款 
     */
	//@TableField("withdraw_setting")
	private Integer withdrawSetting;
    /**
     * 物流余额
     */
	//@TableField("express_cash_income")
	private Double expressCashIncome;
    /**
     * 可提现物流余额
     */
	//@TableField("available_express_cash")
	private Double availableExpressCash;
    /**
     * 最低体现额
     */
	//@TableField("min_withdrawal")
	private Double minWithdrawal;
    /**
     * 保证金
     */
	private Double bond;
    /**
     * 商家号
     */
	//@TableField("business_number")
	private Long businessNumber;
    /**
     * 物流银行开户名称
     */
	//@TableField("express_bank_account_name")
	private String expressBankAccountName;
    /**
     * 物流开户银行名称
     */
	//@TableField("express_bank_name")
	private String expressBankName;
    /**
     * 物流银行账户号
     */
	//@TableField("express_bank_account_no")
	private String expressBankAccountNo;
    /**
     * 创建时间,长整型
     */
	//@TableField("add_createtime")
	private Long addCreatetime;
    /**
     * 是否使用初始化密码,0：不是，1：是
     */
	//@TableField("is_originalpassword")
	private Integer isOriginalpassword;
    /**
     * 供应商收货人
     */
//	private String receiver;
    /**
     * 供应商收货地址
     */
//	@TableField("supplier_receive_address")
//	private String supplierReceiveAddress;
    /**
     * 收货人电话
     */
//	@TableField("receiver_phone")
//	private String receiverPhone;
	/**
	 * 批发起定量
	 */
	//@TableField("wholesale_count")
	private Integer wholesaleCount;
	/**
	 * 批发起定额
	 */
	//@TableField("wholesale_cost")
	private Double wholesaleCost;
	/**
     * 公司名称
     */
	//@TableField("campaign_image")
	private String campaignImage;
	


}
