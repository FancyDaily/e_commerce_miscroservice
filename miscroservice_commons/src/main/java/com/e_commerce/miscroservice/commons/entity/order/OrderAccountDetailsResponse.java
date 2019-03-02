package com.e_commerce.miscroservice.commons.entity.order;

import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 我的-账户-收支详情
 *
 * @author hyf
 * @version V1.0
 * @date 2018/10/18 10:19
 * @Copyright 玖远网络
 */
@Data
public class OrderAccountDetailsResponse implements Serializable {


    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
     */
    private Integer orderStatus;
/**
     * 订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
     */
    private Integer orderType;

    /**
     * 昵称
     */
    private String userNickname;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 确认收货时间
     */
    private String confirmSignedTime;

    private String orderNo;


    /**
     * 总数
     */
    private Integer count;


    /**
     * 支付金额
     */
    private Double payMoney;

    /**
     * 邮费
     */
    private Double expressMoney;

    /**
     * 优惠金额
     */
    private Double saleMoney;

    /**
     * 收益 现金
     */
    private Double money;

    /**
     * 收益 金币
     */
    private Double coin;

    /**
     * 收益比率
     */
    private Double orderEarningsSnapshoot;

    /**
     * 金币
     */
    private Double goldCoin;

    /**
     * 金币现金比
     */
    private Double earningsRatio;
    /**
     * 操作金币
     */
    private Double operGoldCoin;
    /**
     * 操作现金
     */
    private Double operCash;
    /**
     * 操作时间
     */
    private String operTime;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 流水
     */
    private String paymentNo;
    /**
     * 0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,10.分销商的团队收益入账,11.合伙人的团队收益入账,20.签到,21.签到阶段奖励,30.订单取消,31.订单抵扣,50.提现-总额,51.提现-佣金,52提现-管理金
     */
    private Integer type;
    /**
     *1:待结算,2:已结算,3:已冻结,5,预结算(仅插入一条记录,还未进入待结算状态)
     */
    private Integer status;
    /**
     *  isCoin，是否为金币0是1现金
     */
    private Integer isCoin;

//    public void setPayTime(String payTime) {
//        this.payTime = TimeUtils.longFormatString(Long.parseLong(payTime));
//    }

   private List<OrderItemSkuVo> orderItemSkuVoList;
}
