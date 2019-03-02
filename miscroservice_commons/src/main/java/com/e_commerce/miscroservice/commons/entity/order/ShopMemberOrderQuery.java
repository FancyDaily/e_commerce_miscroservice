package com.e_commerce.miscroservice.commons.entity.order;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 18:55
 * @Copyright 玖远网络
 */
@Data
public class ShopMemberOrderQuery extends ShopMemberOrder{

    private Integer pageSize;
    private Integer pageNumber;

    /**
     * 下单时间
     */
    private Long createTimeCeil;


    /**
     * 下单时间
     */
    private Long createTimeFloor;

    /**
     * 商家商户名
     */
    private String storeName;

    /**
     * 下单人昵称
     */
    private String memberName;

    /**
     * 分销角色
     */
    private Integer memberGrade;



    private String createTimeStr;

    /**
     * 更新时间
     */
    private String updateTimeStr;


    /**
     * 发货时间时间
     */
    private String deliveryTimeStr;


    /**
     * 订单关闭时间
     */
    private String orderStopTimeStr;


    /**
     * 订单完成时间
     */
    private String orderFinishTimeStr;

    /**
     * 支付时间
     */
    private String payTimeStr;

    /**
     * 订单提货时间
     */
    private String takeDeliveryTimeStr;

    private List<ShopMemberOrderItemQuery> shopMemberOrderItemList;

    /**
     * 物流公司
     */
    private String expressInfoCom;

    /**
     * 物流公司
     */
    private String expressInfoCompany;

    /**
     * 物流公司订单好
     */
    private String expressInfoNo;

    /**
     * 物流信息
     */
    List<Map<String, Object>> expressInfoList;


    /**
     * 平台优惠金额
     */
    private BigDecimal platformCoupon;

    /**
     * 商家优惠金额
     */
    private BigDecimal businessCoupon;


    /**
     * 应付金额
     */
    private BigDecimal shouldBePayMoney;

    /**
     * 实付现金
     */
    private BigDecimal realPayMoney;

    /**
     * 实付金币
     */
    private BigDecimal realPayGoldCoin;

    /**
     * 现金金币比例
     */
    private String cashGoldCoinRate;

    /**
     * 订单分销收益数量
     */
    private Long earningCount;

    /**
     * 订单现金收益
     */
    private BigDecimal cashEarning;
    /**
     * 订单金币收益
     */
    private BigDecimal goldCoinEarning;


    public void setCreateTimeCeilStr(String createTimeCeil) {
        this.createTimeCeil = TimeUtils.str2Long (createTimeCeil);
    }

    public void setCreateTimeFloorStr(String createTimeFloor) {
        this.createTimeFloor = TimeUtils.str2Long (createTimeFloor);
    }

}
