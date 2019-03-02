package com.e_commerce.miscroservice.commons.entity.application.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 账户
 * @author hyf
 * @version V1.0
 * @date 2018/11/13 9:35
 * @Copyright 玖远网络
 * APP用户的账户
 */
@Data
@Table("yjj_storebusiness_account")
public class YjjStoreBusinessAccount {
    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 总金额
     */
    @Column(value = "count_money",length = 11,precision = 2,defaultVal = "0.00",commit = "总金额")
    private Double countMoney;

    /**
     * 冻结金额
     */
    @Column(value = "frozen_money",length = 11,precision = 2,defaultVal = "0.00",commit = "冻结金额")
    private Double frozenMoney;

    /**
     * 货款可用金额
     */
    @Column(value = "use_money",length = 11,precision = 2,defaultVal = "0.00",commit = "货款可用金额不能提现")
    private Double useMoney;
    /**
     * 可用金额
     */
    @Column(value = "real_use_money",length = 11,precision = 2,defaultVal = "0.00",commit = "实际可用金额可以提现")
    private Double realUseMoney;

    /**
     * 待结算金额
     */
    @Column(value = "wait_in_money",length = 11,precision = 2,defaultVal = "0.00",commit = "待结算金额")
    private Double waitInMoney;

    /**
     * 用户id
     */
    @Column(value = "user_id",length = 20,commit = "用户id")
    private Long userId;

    /**
     * 是否删除 0 正常  1 删除
     */
    @Column(value = "del_status",length = 4,defaultVal = "0",commit = "是否删除 0 正常  1 删除")
    private Integer delStatus;

    /**
     * 创建时间
     */
    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

    /**
     * 是否冻结 0 正常 1 冻结
     */
    @Column(value = "status",length = 4,defaultVal = "0",commit = "是否冻结 0 正常 1 冻结")
    private Integer status;

}
