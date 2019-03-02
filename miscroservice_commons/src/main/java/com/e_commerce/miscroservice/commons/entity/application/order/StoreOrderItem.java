package com.e_commerce.miscroservice.commons.entity.application.order;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * <p>
 * 用户订单细目表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
//@TableName("store_OrderItem")
@Data
public class StoreOrderItem {


    /**
     * 退款状态
     */
//    public static final int NO_REFUND = 0; //0(无退款)
//    public static final int REFUNDING = 1; //1(退款进行中)
//    public static final int PART_REFUNDED = 2; //2(部分退款完成)
//    public static final int ALL_REFUNDED = 3; //3(全部退款完成)

    /**
     * id
     */
    //@TableId(value="Id", type= IdType.AUTO)
    private Long id;
    /**
     * 新订单表OrderNo
     */
    @JSONField(name = "orderNos", serializeUsing = String.class)
    private Long orderNo;
    /**
     * 用户id
     */
    private Long storeId;
    /**
     * 对应Product表的id
     */
    private Long productId;
    /**
     * 对应ProductSKU的id
     */
    private Long skuId;
    /**
     * 订单金额总价，不包含邮费
     */
    private Double totalMoney;
    /**
     * 邮费总价
     */
    private Double totalExpressMoney;
    /**
     * 订单细目单价，不包含邮费
     */
    private Double money;
    /**
     * 邮费单价
     */
    private Double expressMoney;
    /**
     * 总玖币
     */
    private Integer totalUnavalCoinUsed;
    /**
     * 玖币
     */
    private Integer unavalCoinUsed;
    /**
     * 订购数量
     */
    private Integer buyCount;
    /**
     * sku快照，json
     */
    private String skuSnapshot;
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
     * 关联的品牌id
     */
    private Long brandId;
    /**
     * 仓库id
     */
    private Long lOWarehouseId;
    /**
     * 实付价
     */
    private Double totalPay;
    /**
     * 市场价
     */
    private Double totalMarketPrice;
    private Double marketPrice;
    /**
     * 总提现金额
     */
    private Double totalAvailableCommission;
    /**
     * sku 货架位置
     */
    private String position;
    /**
     * 供应商ID
     */
    private Long supplierId;


    /**
     * 购买会员套餐类型 0:无,1会员套餐,2至尊套餐
     */
    //@TableField("member_package_type")
    private Integer memberPackageType;

//	 /**
//     * 退款状态：0(无退款)、1(退款进行中)、2(部分退款完成)、3(全部退款完成)
//     */
//	@TableField("refund_status")
//	private Integer refund_status;

//	 /**
//     * 退款完成金额
//     */
//	@TableField("refund_finish_cost")
//	private Integer refundFinishCost;
//	
//	 /**
//     * 退款完成件数
//     */
//	@TableField("refund_finish_count")
//	private Integer refundFinishCount;
//	public Integer getRefund_status() {
//		return refund_status;
//	}
//
//	public void setRefund_status(Integer refund_status) {
//		this.refund_status = refund_status;
//	}
//
//	public Integer getRefundFinishCost() {
//		return refundFinishCost;
//	}
//
//	public void setRefundFinishCost(Integer refundFinishCost) {
//		this.refundFinishCost = refundFinishCost;
//	}
//	public Integer getRefundFinishCount() {
//		return refundFinishCount;
//	}
//
//	public void setRefundFinishCount(Integer refundFinishCount) {
//		this.refundFinishCount = refundFinishCount;
//	}

}
