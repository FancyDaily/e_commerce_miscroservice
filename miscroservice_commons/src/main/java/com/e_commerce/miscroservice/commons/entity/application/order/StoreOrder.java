package com.e_commerce.miscroservice.commons.entity.application.order;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

//@Table("store_order")
@Data
public class StoreOrder {

    /**
     * 订单编号，前台展示
     */
//    @Id
    private Long orderNo;
    /**
     * 订单随机号,用于微信支付，退款时,生成预生成订单,保证订单能够支付
     */
    private String orderNoAttachmentStr;
    /**
     * 用户id
     */
    private Long storeId;
    /**
     * 0: 普通订单 1:售后订单 2:当面付订单
     */
    private Integer orderType;
    /**
     * 订单付款状态：参照参照OrderStatus
     * 订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)
     */
    private Integer orderStatus;
    /**
     * 订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单
     */
    private Integer orderCloseType;


    /**
     * 订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）
     */
    private Double totalMoney;
    /**
     * 订单金额折后总价,不包含邮费,（不包含平台优惠和店铺优惠）,如果有改价,那么该订单金额为改价后金额
     */
    private Double totalPay;
    /**
     * 平台优惠
     */
    private Double platformTotalPreferential;
    /**
     * 商家店铺实际优惠,因为改价差额可以为负，所以商家店铺实际优惠可以为负（包含商家优惠和商家改价差额）
     */
    private Double supplierTotalPreferential;
    /**
     * 商家优惠（不包含改价部分）
     */
    private Double supplierPreferential;
    /**
     * 商家改价差额,original_price - TotalPay,该改价差额可以为负
     */
    private Double supplierChangePrice;
    /**
     * 订单金额优惠后原始待付款价格，不包含邮费
     */
    private Double originalPrice;
    /**
     * 订单金额折后有效总价，去除退款金额，不包含邮费
     */
    private Double totalValidPay;

    /**
     * 是否发放了优惠券 1 是 0 还未发放
     */
    private Integer sendCoupon;
    /**
     * 订单种类 1:商品订单,2购买会员订单
     */

    private Integer classify;

    /**
     * 邮费总价
     */
    private Double totalExpressMoney;
    /**
     * 邮寄信息
     */
    private String expressInfo;
    /**
     * 使用的玖币
     */
    private Integer coinUsed;
    /**
     * 用户订单备注
     */
    private String remark;



    /**
     * 生成订单平台
     */
    private String platform;
    /**
     * 生成订单平台版本号
     */
    private String platformVersion;
    /**
     * 客户端ip
     */
    private String ip;
    /**
     * 第三方的支付订单号
     */
    private String paymentNo;
    /**
     * 使用的第三方支付方式，参考常量PaymentType
     */
    private Integer paymentType;
    /**
     * 母订单id 0:其他  -1:已拆分订单, OrderNo:没有子订单
     */
    private Long parentId;
    /**
     * 0:其他, 需合并订单：自身改为-1并把相应的被合并的订单此字段设为合好的新订单OrderNo, 不需要合并订单：值与自身OrderNo相等
     */
    private Long mergedId;
    /**
     * 仓库id
     * todo...
     */
    private Long lOWarehouseId;
    /**
     * 状态:-1删除，0正常
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
     * 市场价
     */
    private Double totalMarketPrice;
    /**
     * 取消原因
     */
    private String cancelReason;
    /**
     * 推送时间
     */
    private Long pushTime;
    /**
     * 订单过期时间
     */
    private Long expiredTime;
    /**
     * 付款时间
     */
    private Long payTime;
    /**
     * 发货时间
     */
    private Long sendTime;
//    /**
//     * 收益（已废弃）
//     */
	private Double commission;
//    /**
//     * 可提现金额（已废弃）
//     */
	private Double availableCommission;
    /**
     * 提成比例（已废弃）
     */
    private Double commissionPercent;
//    /**
//     * 品牌批发订单编号（已废弃）
//     */
	private Long brandOrder;
    /**
     * 总购买件数
     */
    private Integer totalBuyCount;

    /**
     * 新功能涉及到旧的订单，所以出现，是否已经提现，0：旧订单，1：未提现，2已提现
     */
    private Integer hasWithdrawed;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 地推用户id
     */
    private Long groundUserId;
    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
    private String superIds;
    /**
     * 确认收获日期，格式例：20170909
     */
    private Integer confirmSignedDate;
    /**
     * 确认收获日期，格式例：20170909
     */
    private Long confirmSignedTime;

    /**
     * 是否是售后进行中：0(否)、1(是)
     */
    private Integer refundUnderway;

    /**
     * 自动确认收货总暂停时长(毫秒)
     */
    private Long autoTakeGeliveryPauseTimeLength;

    /**
     * 售后开始时间时间戳,使用方法,当该订单发起第一笔售后或者平台介入就记录时间戳,当该订单售后完全结束且平台介入结束时，该字段变为0
     */
    private Long refundStartTime;

    /**
     * 总的退款金额
     */
    private Double totalRefundCost;

    /**
     * 收件人姓名
     */
    private String expressName;

    /**
     * 收件人号码
     */
    private String expressPhone;

    /**
     * 收件人地址
     */
    private String expressAddress;

    /**
     * 供应商订单备注
     */
    private String orderSupplierRemark;

    /**
     * 订单关闭时间
     */
    private Long orderCloseTime;

    /**
     * 是否挂起 0：否  1：是
     */
    private Integer hangUp;

    /**
     * 是否锁定在支付订单：0：否,不锁定支付订单 1：是,锁定支付订单
     */
    private Integer lockingOrder;

    /**
     * 限购活动商品id
     */
    private Long restrictionActivityProductId;

    /**
     * 0 普通, 1,进货金专属(平台代发货)
     */
    private Integer type;

    /**
     * 平台代发货关联的小程序订单
     */
    private Long shopMemberOrderId;

    /**
     * 获取订单关闭时间  V3.2 仇约帆
     * @param storeOrder
     * @return
     */
    public long buildOrderCloseTime(StoreOrder storeOrder) {
        // TODO V3.2待实现
        //交易关闭的时间，根据不同情况分为：
        //1、用户取消待付款订单的时间，
        //2、平台取消已付款订单的时间，
        //3、售后退货退款：卖家确认收货或者系统自动，仅退款：卖家同意受理售后时间。
        //4、未付款订单超时后系统自动关闭订单的时间
        long closeTime = 0;
        int orderCloseType = storeOrder.getOrderCloseType();//orderCloseType订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105平台客服关闭订单
        if(orderCloseType == 101){//101买家主动取消订单
            closeTime = this.orderCloseTime;
        }else if(orderCloseType == 102){//102超时未付款系统自动关闭订单
            closeTime = this.orderCloseTime;
        }else if(orderCloseType == 103){//103全部退款关闭订单
            closeTime = this.orderCloseTime;
        }else if(orderCloseType == 104){//104卖家关闭订单
            closeTime = 0;//暂时没有实现这个功能
        }else if(orderCloseType == 105){//105平台客服关闭订单
            closeTime = 0;//老平台取消订单的状态是订单取消，不是订单关闭，所以这个功能暂停，这个时间暂时没有
        }
        return closeTime;
    }

}
