package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收益vo
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/9 19:07
 * @Copyright 玖远网络
 */
@Data
@AllArgsConstructor
public class EarningsVo {


    /**
     * 收益的总金额 = {@link EarningsVo#goldCoin} + {@link EarningsVo#cash}
     */
    private BigDecimal totalMoney;


    /**
     * 收益的金币(总金额拆分得到)
     */
    private BigDecimal goldCoin;


    /**
     * 收益的现金(总金额拆分得到)
     */
    private BigDecimal cash;

    /**
     * 收益的策略
     */
    private DstbEarningsStrategy strategy;

    /**
     * 分销级别
     */
    private Integer grade;
}
