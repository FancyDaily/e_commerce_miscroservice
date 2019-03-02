package com.e_commerce.miscroservice.commons.entity.application.distributionSystem;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 分销账户表
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/8 15:16
 * @Copyright 玖远网络
 */
@Data
@Table("shop_member_account")
public class ShopMemberAccount{

    @Id
    private Long id;
    /**
     * 小程序用户id
     */
    @Column(value = "user_id", commit = "小程序用户id", isNUll = false, length = 20)
    private Long userId;



    // ======================= 待结算 结算用的到(待结算细目用户小程序端展示) =======================
    /**
     * 待结算总现金(各种资金的待入账汇总)
     */
    @Column(value = "wait_in_total_cash",commit = "待结算总现金(各种资金的待入账汇总)", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal waitInTotalCash;

    /**
     * 待结算总金币(各种资金的待入账汇总)
     */
    @Column(value = "wait_in_total_gold_coin",commit = "待结算总金币(各种资金的待入账汇总)", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal waitInTotalGoldCoin;

    /**
     * 佣金奖待结算总现金
     */
    @Column(value = "commission_wait_in_total_cash",commit = "佣金奖待结算总现金", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal commissionWaitInTotalCash;

    /**
     * 佣金奖待结算总金币
     */
    @Column(value = "commission_wait_in_total_gold_coin",commit = "佣金奖待结算总金币", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal commissionWaitInTotalGoldCoin;

    /**
     * 管理奖待结算总现金
     */
    @Column(value = "manage_wait_in_total_cash",commit = "管理奖待结算总现金", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal manageWaitInTotalCash;

    /**
     * 管理奖待结算总金币
     */
    @Column(value = "manage_wait_in_total_gold_coin",commit = "管理奖待结算总金币", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal manageWaitInTotalGoldCoin;
    // ======================= 待结算 =======================





    // ======================= 可用 提现时用的到 =======================
    /**
     * 可用金币
     */
    @Column(value = "alive_gold_coin",commit = "可用金币", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal aliveGoldCoin;

    /**
     * 可用现金
     */
    @Column(value = "alive_cash",commit = "可用现金", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal aliveCash;

    /**
     * 佣金奖可用金币
     */
    @Column(value = "commission_alive_gold_coin",commit = "佣金奖可用金币", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal commissionAliveGoldCoin;

    /**
     * 佣金奖可用现金
     */
    @Column(value = "commission_alive_cash",commit = "佣金奖可用现金", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal commissionAliveCash;

    /**
     * 管理奖可用现金
     */
    @Column(value = "manage_alive_cash",commit = "管理奖可用现金", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal manageAliveCash;

    /**
     * 管理奖可用金币
     */
    @Column(value = "manage_alive_gold_coin",commit = "管理奖可用金币", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal manageAliveGoldCoin;
    // ======================= 可用 提现时用的到 =======================



    /**
     * 累计签到金币
     */
    @Column(value = "sign_gold_coin",commit = "累计签到金币", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal signGoldCoin;

    /**
     * 累计提现(实际提现现金)
     */
    @Column(value = "history_cash_out",commit = "累计提现", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal historyCashOut;

    /**
     * 金币花费(各种花费统计)
     */
    @Column( value = "history_gold_coin_out", commit = "累计金币花费", defaultVal = "0", length = 11, precision = 2 )
    private BigDecimal historyGoldCoinOut;

    /**
     * 累计现金收益(已结算)
     */
    @Column( value = "history_cash_earning" , commit = "累计现金收益(已结算)", defaultVal = "0", length = 11, precision = 2)
    private BigDecimal historyCashEarning;

    /**
     * 累计金币收益(已结算)
     */
    @Column( value = "history_gold_coin_earning" , commit = "累计金币收益(已结算)", defaultVal = "0", length = 11, precision = 2)
    private BigDecimal historyGoldCoinEarning;

    /**
     * 冻结金额(出账)
     */
    @Column(value = "frozen_cash",commit = "冻结金额", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal frozenCash;


    /**
     * 冻结金币(出账)
     */
    @Column(value = "frozen_gold_coin",commit = "冻结金币", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal frozenGoldCoin;


    //==================== 总 ====================
    /**
     * 总现金(待结算+可用总现金)
     * <p>只有总现金,总金币对待结算做了求和,可用总现金已经扣除了待出账的</p>
     */
    @Column(value = "all_cash",commit = "总现金(待结算+可用总现金)", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal allCash;
    /**
     * 总金币(待结算+可用总金币)
     * <p>只有总现金,总金币对待结算做了求和,可用总现金已经扣除了待出账的</p>
     */
    @Column(value = "all_gold_coin",commit = "总金币(待结算+可用总金币)", defaultVal = "0",length = 11,precision = 2)
    private BigDecimal allGoldCoin;
    //==================== 总 ====================


    /**
     * 状态 0:正常,1:删除
     */
    @Column(value = "del_status", commit = "状态 0:正常,1:删除", length = 4, defaultVal = "0", isNUll = false)
    private Integer delStatus;
    /**
     * 版本号
     */
    @Column(value = "version", commit = "版本号", length = 20, defaultVal = "0")
    private Long version;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

}
