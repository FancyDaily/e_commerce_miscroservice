package com.e_commerce.miscroservice.commons.entity.application.distributionSystem;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 分销收益策略表
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/8 14:51
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_distribution_earnings_strategy")
public class DstbEarningsStrategy{

    @Id
    private Long id;

    /**
     *0 无等级 1 店长 2分销商 3合伙人
     */
    @Column(value = "user_role_type",commit = "用户角色类型 0:无等级,1:店长,2:分销商,3:合伙人", isNUll = false, length = 4)
    private Integer userRoleType;

    /**
     * 0:本人下单返利,1:一级粉丝下单返利,2:二级粉丝下单返利,10:分销商团队收益
     */
    @Column(value = "earnings_type",commit = "返利类型 0:本人下单返利,1:一级粉丝下单返利,2:二级粉丝下单返利,10:团队收益", isNUll = false, length = 4)
    private Integer earningsType;

    /**
     * 收益比例
     */
    @Column(value = "earnings_ratio",commit = "收益比例", defaultVal = "0",length = 7,precision = 2)
    private BigDecimal earningsRatio;

    /**
     * 货币比例(货币比例,收益拆成多少钱的金币和现金)
     */
    @Column(value = "currency_ratio",commit = "货币比例,收益拆成多少钱的金币和现金", defaultVal = "0",length = 7,precision = 2)
    private BigDecimal currencyRatio;

    /**
     * 状态 0:正常,1:删除
     */
    @Column(value = "del_status", commit = "状态 0:正常,1:删除", length = 4, defaultVal = "0", isNUll = false)
    private Integer delStatus;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;


}
