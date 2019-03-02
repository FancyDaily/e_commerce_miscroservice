package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 售后订单表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-11
 */
//@TableName("refund_order")
@Data
public class RefundOrder implements Serializable{

    private static final long serialVersionUID = 1L;
	
	/* 退款类型：1.仅退款  2.退货退款 */
	public static final int refundType_refund = 1;//1.仅退款
	public static final int refundType_refund_and_product = 2;//2.退货退款
	
	//platformInterveneState平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
	public static final int PLATFORM_NOT_INTERVENE = 0;//未介入
	public static final int CUSTOMER_PLATFORM_INTERVENE = 1;//1买家申请平台介入中
	public static final int SELLER_PLATFORM_INTERVENE = 2;//2卖家申请平台介入中
	public static final int CLOSE_CUSTOMER_PLATFORM_INTERVENE = 3;//3买家申请平台介入结束
	public static final int CLOSE_SELLER_PLATFORM_INTERVENE = 4;//4卖家申请平台介入结束
	
    public static final long NO_PAUSE = 0;//卖家自动确认收货暂停时间，为0是则表示未暂停
    
    /**
     * id
     */
	//@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 售后订单编号
     */
	//@TableField("refund_order_no")
	private Long refundOrderNo;
	/**
     * 订单ID
     */
	//@TableField("order_no")
	private Long orderNo;
	
	
   
	
	 /**
     * 商家手机号
     */
	//@TableField("store_phone")
	private String storePhone;
	 /**
     * 商家名称
     */
	//@TableField("store_name")
	private String storeName;
	   
  
	 /**
     * 商家号ID
     */
	//@TableField("store_id")
	private Long storeId;
	 /**
     * 供应商ID
     */
	//@TableField("supplier_id")
	private Long supplierId;
	
    /**
     * 品牌ID
     */
	//@TableField("brand_id")
	private Long brandId;
    /**
     * 品牌名称
     */
	//@TableField("brand_name")
	private String brandName;
    /**
     * 退款类型：1.仅退款  2.退货退款
     */
	//@TableField("refund_type")
	private Integer refundType;
    /**
     * 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
     */
	//@TableField("refund_status")
	private Integer refundStatus;
    /**
     * 退款申请金额
     */
	//@TableField("refund_cost")
	private Double refundCost;


    /**
     * 退货数量
     */
	//@TableField("return_count")
	private Integer returnCount;
	  /**
     * 退款原因ID
     */
	//@TableField("refund_reason_id")
	private Long refundReasonId;
    /**
     * 退款原因
     */
	//@TableField("refund_reason")
	private String refundReason;
    /**
     * 退款说明
     */
	//@TableField("refund_remark")
	private String refundRemark;
    /**
     * 退款证明图片，英文逗号分隔的字符串
     */
	//@TableField("refund_proof_images")
	private String refundProofImages;
    
    /**
     * 买家发货快递单号
     */
	//@TableField("customer_express_no")
	private String customerExpressNo;
	  /**
     * 买家发货快递公司
     */
	//@TableField("customer_express_company")
	private String customerExpressCompany;
	  /**
     * 买家发货快递公司名称
     */
	//@TableField("customer_express_company_name")
	private String customerExpressCompanyName;
   
	
	/**
     * 退款通道：0(无)、1(支付宝)、2(微信)
     */
	//@TableField("refund_way")
	private Integer refundWay;
    
    /**
     * 申请售后时间
     */
	//@TableField("apply_time")
	private Long applyTime;
   
    /**
     * 买家发货时间
     */
	//@TableField("customer_return_time")
	private Long customerReturnTime;
	 /**
     * 卖家确认收货时间
     */
	//@TableField("confirm_time")
	private Long confirmTime;
	
	/**
     * 退款时间
     */
	//@TableField("refund_time")
	private Long refundTime;
	
	 /**
     * 卖家同意时间
     */
	///@TableField("store_allow_refund_time")
	private Long storeAllowRefundTime;
	
	 /**
     * 卖家拒绝时间
     */
	//@TableField("store_refuse_refund_time")
	private Long storeRefuseRefundTime;
	
	 /**
     * 卖家同意退款备注
     */
	//@TableField("store_agree_remark")
	private String storeAgreeRemark;
	
	 /**
     * 卖家拒绝退款原因、拒绝理由：卖家确认拒绝退款时填写的理由
     */
	//@TableField("store_refuse_reason")
	private String storeRefuseReason;
	
	 /**
     * 平台关闭理由
     */
	//@TableField("platform_close_reason")
	private String platformCloseReason;
	
	 /**
     * 平台介入处理意见
     */
	//@TableField("handling_suggestion")
	private String handlingSuggestion;
	
	/**
     * 供应商收货人姓名
     */
	//@TableField("receiver")
	private String receiver;
	
	/**
     * 供应商收货地址
     */
	//@TableField("supplier_receive_address")
	private String supplierReceiveAddress;
	
	/**
     * 收货人电话
     */
	//@TableField("receiver_phone")
	private String receiverPhone;
	
	
	
	 /**
     * 平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
     */
	//@TableField("platform_intervene_state")
	private Integer platformInterveneState;


	
	 /**
     * 平台介入时间
     */
	//@TableField("platform_intervene_time")
	private Long platformInterveneTime;
	
	 /**
     * 平台客服关闭时间
     */
	//@TableField("platform_intervene_close_time")
	private Long platformInterveneCloseTime;
	
	 /**
     * 买家撤销售后订单时间
     */
	//@TableField("customer_cancel_time")
	private Long customerCancelTime;
	
	 /**
     * 买家超时未发货时间
     */
	//@TableField("customer_overtime_time_no_delivery")
	private Long customerOvertimeTimeNoDelivery;
	
	
	
	 /**
     * 物流信息内容
     */
	//@TableField("express_info")
	private String expressInfo;
	
	 /**
     * 卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
     */
	//@TableField("supplier_auto_take_delivery_pause_time")
	private Long supplierAutoTakeDeliveryPauseTime;
	
	 /**
     * 卖家自动确认收货总暂停时长（毫秒）
     */
	//@TableField("supplier_auto_take_delivery_pause_time_length")
	private Long supplierAutoTakeDeliveryPauseTimeLength;
	/**
	 * 商家 确定退款金额
	 */
	private Double storeBackMoney;
	/**
	 * 实际退款金额
	 */
	private Double realBackMoney;


	private Long skuId;

	/**
	 * 售后类型名称  退款类型：1.仅退款  2.退货退款
	 * @return
	 */
	public String buildRefundTypeName() {
		if(this.refundType == 1){
			return "仅退款";
		}else if(this.refundType == 2){
			return "退货退款";
		}else{
			return "未知";
		}
	}
	/**
	 * 平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
	 * @return
	 */
	public String buildPlatformInterveneStateName() {
		if(this.platformInterveneState == 0){
			return "未介入";
		}else if(this.platformInterveneState == 1){
			return "买家申请平台介入中";
		}else if(this.platformInterveneState == 2){
			return "卖家申请平台介入中";
		}else if(this.platformInterveneState == 3){
			return "买家申请平台介入结束";
		}else if(this.platformInterveneState == 4){
			return "卖家申请平台介入结束";
		}else{
			return "未知";
		}
	}
}
