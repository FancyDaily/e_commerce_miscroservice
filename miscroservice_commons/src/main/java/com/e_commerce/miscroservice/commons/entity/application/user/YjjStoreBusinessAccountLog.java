package com.e_commerce.miscroservice.commons.entity.application.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 账单日志流水
 * @author hyf
 * @version V1.0
 * @date 2018/11/13 9:35
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_storebusiness_account_log")
public class YjjStoreBusinessAccountLog {
    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 订单号
     */
    @Column(value = "order_no",length = 256,commit = "订单号：充值订单号仅供显示，商品订单号则可用于相关查询")
    private String orderNo;
    /**
     * 备注
     */
    @Column(value = "remarks",length = 256,commit = "备注-类型状态 详见=com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums")
    private String remarks;
    /**
     * 类型
     */
    @Column(value = "type",length = 4,commit = "类型 0 平台充值提现，1 用户充值提现，1001,提现成功,1101,充值成功,1201,转入成功" +
            ",1401,退款成功,1501,发放成功,1601,扣款成功,1701,扣款成功,1801,下单成功,1901,结算成功" +
            "详见=com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums",isNUll = false)
    private Integer type;

    /**
     * 收入支出
     */
    @Column(value = "in_out",length = 4,commit = "收入支出 0收入 1 支出",isNUll = false)
    private Integer inOutType;
    /**
     * 操作金额
     */
    @Column(value = "oper_money",length = 11,precision = 2,defaultVal = "0.00",commit = "操作金额")
    private Double operMoney;
    /**
     * 余额
     */
    @Column(value = "remainder_money",length = 11,precision = 2,defaultVal = "0.00",commit = "余额")
    private Double remainderMoney;

    /**
     * 用户id
     */
    @Column(value = "user_id",length = 20,commit = "用户id")
    private Long userId;
    /**
     * 小程序id
     */
    @Column(value = "member_id",length = 20,commit = "小程序id")
    private Long memberId;

    /**
     * 相关订单号
     */
    @Column(value = "about_order_no",length = 256,commit = "订单号：充值订单号仅供显示，商品订单号则可用于相关查询")
    private String aboutOrderNo;
    /**
     * 是否删除 0 正常  1 删除
     */
    @Column(value = "del_status",length = 4,defaultVal = "0",commit = "是否删除 0 正常  1 删除")
    private Integer delStatus;
    /**
     * 是否冻结 0 正常  1 删除
     */
    @Column(value = "status",length = 4,defaultVal = "0",commit = "是否冻结 0 正常  1 冻结")
    private Integer statusType;

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



}
