package com.e_commerce.miscroservice.commons.entity.user;

import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/18 19:39
 * @Copyright 玖远网络
 */
@Data
public class StoreWxaShopAuditDataQuery{

    private Long id;
    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 店铺名称(共享小程序浏览名称)
     */
    private String shopName;
    /**
     * 店主名称
     */
    private String bossName;
    /**
     * 行业
     */
    private String industry;
    /**
     * 主营业务
     */
    private String mainBusiness;
    /**
     * 地址
     */
    private String address;
    /**
     * 是否删除 0正常,1删除,4草稿
     */
    private Integer delStatus;

    private Long inShopMemberId;
}
