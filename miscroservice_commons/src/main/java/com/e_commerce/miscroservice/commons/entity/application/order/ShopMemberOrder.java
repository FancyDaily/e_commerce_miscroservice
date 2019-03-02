package com.e_commerce.miscroservice.commons.entity.application.order;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员订单表
 *
 * @author charlie
 * @version V1.0
 * @date 2018年10月11日 下午 03:29:25
 */
@Data
//@Table ("shop_member_order")
public class ShopMemberOrder implements Serializable{

	@Id
	private Long id;

	/**
	 * 订单编号
	 */
	private String orderNumber;

	/**
	 * 商家ID
	 */
	private Long storeId;

	/**
	 * 会员ID
	 */
	private Long memberId;

	/**
	 * 团购活动id
	 */
	private Long teamId;

	/**
	 * 秒杀活动id
	 */
	private Long secondId;

	/**
	 * 订单总金额，含邮费
	 */
	private BigDecimal totalExpressAndMoney;

	/**
	 * 商品总金额，不含邮费
	 */
	private BigDecimal totalMoney;

	/**
	 * 实付金额
	 */
	private BigDecimal payMoney;

	/**
	 * 优惠金额
	 */
	private BigDecimal saleMoney;

	/**
	 * 邮费金额
	 */
	private BigDecimal expressMoney;

	/**
	 * 商品总件数
	 */
	private Integer count;

	/**
	 * 商品主图集合，英文逗号分隔
	 */
	private String summaryImages;

	/**
	 * 订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
	 */
	private Integer orderType;

	/**
	 * 订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
	 */
	private Integer orderStatus;

	/**
	 * 第三方的支付订单号
	 */
	private String paymentNo;

	/**
	 * 取消类型：0无、1会员取消、2商家取消、3系统自动取消
	 */
	private Integer cancelReasonType;

	/**
	 * 取消原因
	 */
	private String cancelReason;

	/**
	 * 交易关闭时间
	 */
	private Long orderStopTime;

	/**
	 * 付款时间
	 */
	private Long payTime;

	/**
	 * 提货时间
	 */
	private Long takeDeliveryTime;

	/**
	 * 交易完成时间
	 */
	private Long orderFinishTime;

	/**
	 * 更新时间
	 */
	private Long updateTime;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 购买用户的昵称
	 */
	private String userNickname;

	/**
	 * 用户优惠券ID
	 */
	private Long couponId;

	/**
	 * 用户优惠券标题
	 */
	private String couponName;

	/**
	 * 用户优惠券限额 满多少可以使用
	 */
	private BigDecimal couponLimitMoney;

	/**
	 * 是否已经发送订单即将超时通知:0未发送;1已发送
	 */
	private Integer sendMessage;

	/**
	 * 预支付交易会话标识，用于发送服务模板通知
	 */
	private String payFormId;

	/**
	 * 第三方支付类型：0(无)、1微信小程序、2微信公众号
	 */
	private Integer paymentType;

	/**
	 * 邮递公司中文名称
	 */
	private String expreeSupplierCnname;

	/**
	 * 邮递公司名称
	 */
	private String expressSupplier;

	/**
	 * 邮递运单号
	 */
	private String expressNo;

	/**
	 * 收件人姓名
	 */
	private String receiverName;

	/**
	 * 收件人电话
	 */
	private String receiverPhone;

	/**
	 * 收件人地址
	 */
	private String receiverAddress;

	/**
	 * 发货时间
	 */
	private Long deliveryTime;

	/**
	 * 确认收货日期，格式例：20170909
	 */
	private Integer confirmSignedDate;

	/**
	 * 确认收货时间，时间戳
	 */
	private Long confirmSignedTime;

	/**
	 * 给商家的留言
	 */
	private String remark;

	/**
	 * 物流信息内容
	 */
	private String expressInfo;

	/**
	 * 购买方式 0：普通   1：团购  2：秒杀
	 */
	private Integer buyWay;


	/**
	 * 使用金币
	 */
	@Column(value = "gold_coin",commit = "使用金币", defaultVal = "0",length = 7,precision = 2)
	private BigDecimal goldCoin;
 }