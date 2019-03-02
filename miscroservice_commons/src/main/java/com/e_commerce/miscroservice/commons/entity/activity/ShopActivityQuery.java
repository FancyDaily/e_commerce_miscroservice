package com.e_commerce.miscroservice.commons.entity.activity;

import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 9:59
 * @Copyright 玖远网络
 */
@Data
public class ShopActivityQuery{

    private Long id;
    /**
     * 商家号ID
     */
    private Long storeId;
    /**
     * 活动标题
     */
    private String activityTitle;
    /**
     * 活动商品ID
     */
    private Long shopProductId;
    /**
     * 活动商品款号
     */
    private String clothesNumber;
    /**
     * 活动商品名称
     */
    private String shopProductName;
    /**
     * 活动商品主图
     */
    private String shopProductMainimg;
    /**
     * 活动商品橱窗图
     */
    private String shopProductShowcaseImgs;
    /**
     * 活动商品原价格
     */
    private Double activityProductPrice;
    /**
     * 活动价格
     */
    private Double activityPrice;
    /**
     * 活动商品数量
     */
    private Integer activityProductCount;

    /**
     * 活动有效开始时间
     */
    private Long activityStartTime;
    /**
     * 活动有效截止时间
     */
    private Long activityEndTime;
    /**
     * 活动手工结束时间：0表示未手工结束
     */
    private Long activityHandEndTime;
    /**
     * 参与人数
     */
    private Integer activityMemberCount;

    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 修改时间
     */
    private Long updateTime;
    /**
     * 已下单件数
     */
    private Integer orderedProductCount;


    /**
     * 1 团购,2秒杀
     */
    private Integer activityType;
}
