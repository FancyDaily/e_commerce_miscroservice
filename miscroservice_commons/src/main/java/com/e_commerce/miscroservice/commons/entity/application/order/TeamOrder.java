package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/16 11:46
 * @Copyright 玖远网络
 */
@Data
public class TeamOrder extends ShopMemberOrder implements Serializable {

    /**
     * 分销团队总收益(现金)
     */
    private BigDecimal totalCash;

    /**
     * 分销团队总收益(金币)
     */
    private BigDecimal totalGoldCoin;
    /**
     * 订单金额
     */
    private BigDecimal orderMoney;
    private String userNickname;

}
