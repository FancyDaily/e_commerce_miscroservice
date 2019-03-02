package com.e_commerce.miscroservice.commons.entity.order;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrderItem;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/9 10:04
 * @Copyright 玖远网络
 */
@Data
public class ShopMemberOrderItemQuery extends ShopMemberOrderItem{


    /**
     * 商品详情图片
     */
    private String detailImage;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品款号
     */
    private String clothesNumber;
    private Long liveProductId;

}
