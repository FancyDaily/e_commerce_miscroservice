package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户流水
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/8 15:19
 * @Copyright 玖远网络
 */
@Data
public class ShopMemAcctCashOutInQuery extends ShopMemberAccountCashOutIn{

    private Integer pageSize;
    private Integer pageNumber;

    private String userName;

    /**
     * 商户名
     */
    private String storeName;

    private Long userMemberId;

    private Integer userMemberGrade;

    /**
     * 提现的佣金
     */
    private BigDecimal cashOutCommission;
    /**
     * 提现的管理金
     */
    private BigDecimal cashOutTeamIn;
    /**
     * 时间戳 转 格式化 时间
     */
    private String strTime;

    /**
     * 收益现金,低值
     */
    private BigDecimal operCashCeil;
    /**
     * 收益现金,高值
     */
    private BigDecimal operCashFloor;
    /**
     * 收益金币,低值
     */
    private BigDecimal operGoldCoinCeil;
    /**
     * 收益金币,高值
     */
    private BigDecimal operGoldCoinFloor;

    /**
     * 支付时间
     */
    private Long payTimeCeil;
    /**
     * 支付时间
     */
    private Long payTimeFloor;


    private Long teamUserCount;
    private Long fansUserCount;

    private Long orderCount;
    private BigDecimal orderDealMoney;


    private BigDecimal totalCommissionCash;
    private BigDecimal totalCommissionGoldCoin;
    private BigDecimal totalManagerCash;
    private BigDecimal totalManagerGoldCoin;
    private BigDecimal totalCash;
    private BigDecimal totalGoldCoin;

    /**
     * 收益烈性
     */
    private List<Integer> typeList;
    /**
     * 1-只查询现金收益
     */
    private Integer justCash;

    /**
     * 1-只查询金币收益
     */
    private Integer justGoldCoin;

    private String createTimeCeil;
    private String createTimeFloor;


    public void setPayTimeCeilStr(String payTimeCeil) {
        this.payTimeCeil = TimeUtils.str2Long (payTimeCeil);
    }

    public void setPayTimeFloorStr(String payTimeFloor) {
        this.payTimeFloor = TimeUtils.str2Long (payTimeFloor);
    }
}
