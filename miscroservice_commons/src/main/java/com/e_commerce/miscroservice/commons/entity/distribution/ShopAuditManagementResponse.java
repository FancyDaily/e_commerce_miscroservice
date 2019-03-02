package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopAuditManagement;
import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/16 10:20
 * @Copyright 玖远网络
 */
@Data
public class ShopAuditManagementResponse extends ShopAuditManagement {

    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 店家名称
     */
    private String storeName;

    /**
     * 店铺id
     */
    private Long storeId;

}
