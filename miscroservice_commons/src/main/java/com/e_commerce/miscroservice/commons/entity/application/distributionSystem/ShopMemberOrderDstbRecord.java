package com.e_commerce.miscroservice.commons.entity.application.distributionSystem;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/15 23:59
 * @Copyright 玖远网络
 */
@Data
@Table ("shop_member_order_dstb_record")
public class ShopMemberOrderDstbRecord{

    @Id
    private Long id;

    @Column(value = "order_no", commit = "订单编号", isNUll = false, length = 60)
    private String orderNo;

    /**
     * 自分佣受益人
     */
    @Column(value = "self_commission_user_id", commit = "自分佣受益人", length = 20, defaultVal = "0")
    private Long selfCommissionUserId;

    /**
     * 上级(1级)分佣受益人
     */
    @Column(value = "higher_commission_user_id", commit = "上级(1级)分佣受益人", length = 20, defaultVal = "0")
    private Long higherCommissionUserId;

    /**
     * 上上级(二级)分佣受益人
     */
    @Column(value = "top_commission_user_id", commit = "上上级(2级)分佣受益人", length = 20, defaultVal = "0")
    private Long topCommissionUserId;


    /**
     * 订单分销商受益人
     */
    @Column(value = "distributor", commit = "订单分销商受益人", length = 20, defaultVal = "0")
    private Long distributor;

    /**
     * 订单合伙人受益人
     */
    @Column(value = "partner", commit = "订单合伙人受益人", length = 20, defaultVal = "0")
    private Long partner;

    /**
     * 分销团队(佣金)总收益(现金),待结算+已结算
     */
    @Column(value = "total_commission_cash", commit = "分销团队(佣金)总收益(现金)", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal totalCommissionCash;

    /**
     * 分销团队(佣金)总收益(金币),待结算+已结算
     */
    @Column(value = "total_commission_gold_coin", commit = "分销团队(佣金)总收益(金币)", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal totalCommissionGoldCoin;

    /**
     * 分销团队(管理金)总收益(现金),待结算+已结算
     */
    @Column(value = "total_manager_cash", commit = "分销团队(管理金)总收益(现金)", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal totalManagerCash;

    /**
     * 分销团队(管理金)总收益(金币),待结算+已结算
     */
    @Column(value = "total_manager_gold_coin", commit = "分销团队(管理金)总收益(金币)", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal totalManagerGoldCoin;


    /**
     * 订单金额(存个快照,以防以后订单价格改价)
     */
    @Column(value = "order_money", commit = "订单金额", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal orderMoney;


    /**
     * 人民币金币兑换比例
     */
    @Column(value = "exchange_rate", commit = "人民币金币兑换比例,一块钱兑换多少金币", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal exchangeRate;

    @Column(value = "pay_time", commit = "支付时间", length = 20, defaultVal = "0")
    private Long payTime;

    @Column( value = "order_success_time", commit = "订单成功时间(确认收货)" , length = 20, defaultVal = "0")
    private Long orderSuccessTime;



}
