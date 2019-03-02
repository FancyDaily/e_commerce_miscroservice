package com.e_commerce.miscroservice.commons.entity.application.distributionSystem;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 账户流水
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/8 15:19
 * @Copyright 玖远网络
 */
@Data
@Table("shop_member_account_cash_out_in")
public class ShopMemberAccountCashOutIn{

    @Id
    private Long id;

    @Column(value = "source_id", commit = "父级id", length = 20, defaultVal = "0")
    private Long sourceId;

    /**
     * 流水产生的id,比如订单表id
     */
    @Column(value = "from_id", commit = "流水产生的id", length = 20, defaultVal = "0")
    private Long fromId;

    /**
     * 1:进账,2:出账
     */
    @Column(value = "in_out_type", commit = "1:进账,2:出账", isNUll = false, length = 4)
    private Integer inOutType;
    /**
     * 小程序用户id
     */
    @Column(value = "user_id", commit = "小程序用户id", isNUll = false, length = 20)
    private Long userId;
    /**
     * 用户分销级别 -1.未知, 0 无等级 1 店长 2分销商 3合伙人
     */
    @Column(value = "user_dstb_grade", commit = "用户分销级别", defaultVal = "-1", length = 20)
    private Integer userDstbGrade;
    /**
     * 下单订单号/提现订单号
     */
    @Column(value = "order_no", commit = "下单订单号/提现订单号", length = 40)
    private String orderNo;
    /**
     * 微信提现成功后订单号
     */
    @Column(value = "payment_no", commit = "微信返回订单号,没有则为平台自动生成", length = 64)
    private String paymentNo;
    /**
     * 状态: 1:待入账,2:已入账,3:已冻结
     */
    @Column(value = "status", commit = "1:待结算,2:已结算,3:已冻结", isNUll = false, length = 4)
    private Integer status;
    /**
     * 详细状态,1:待结算,2:已结算成功(已入账),3:已冻结, 4:待结算到成功的中间状态--待发放/提现中(待结算到已结算的中间状态), -2:已结算失败, -4:待结算到失败的中间状态
     */
    @Column( value = "detail_status" , commit = "详细状态,1:待结算,2:已结算成功(已入账),3:已冻结, 4:待结算到成功的中间状态--待发放/提现中(待结算到已结算的中间状态), -2:已结算失败, -4:待结算到失败的中间状态", isNUll = false, length = 4)
    private Integer detailStatus;


    /**
     * 操作现金
     */
    @Column(value = "oper_cash",commit = "操作现金", defaultVal = "0",length = 7,precision = 2)
    private BigDecimal operCash;

    /**
     * 操作后现金余额(提现后余额)
     */
    @Column(value = "balance_cash", commit = "操作后现金余额", defaultVal = "0",length = 7,precision = 2)
    private BigDecimal balanceCash;

    /**
     * 操作金币
     */
    @Column(value = "oper_gold_coin",commit = "操作金币", defaultVal = "0",length = 7,precision = 2)
    private BigDecimal operGoldCoin;

    /**
     * 操作时间
     */
    @Column(value = "oper_time",commit = "操作时间(时间戳)", length = 20, defaultVal = "0")
    private Long operTime;

    /**
     * 原有金币(结算入账时记录)
     */
    @Column(value = "original_gold_coin", commit = "原有金币", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal originalGoldCoin;
    /**
     * 原有现金(结算入账时记录)
     */
    @Column(value = "original_cash", commit = "原有现金", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal originalCash;

    /**
     * 当时订单收益快照(这笔订单多少收益,不拆分成金币和现金时的总金额)
     */
    @Column(value = "order_earnings_snapshoot", commit = "当时订单收益快照", length = 7,precision = 2, defaultVal = "0")
    private BigDecimal orderEarningsSnapshoot;


    /**
     * 收益比例
     */
    @Column(value = "earnings_ratio",commit = "收益比例", defaultVal = "0",length = 7,precision = 2)
    private BigDecimal earningsRatio;

    /**
     * 货币比例(现金和金币比列)
     */
    @Column(value = "currency_ratio",commit = "货币比例", defaultVal = "0",length = 7,precision = 2)
    private BigDecimal currencyRatio;


    /**
     * 0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,
     * 10.分销商的团队收益入账,11.合伙人的团队收益入账,
     * 20.签到,21.签到阶段奖励,
     * 30.订单取消,31.订单抵扣,
     * 40.分享收益,41.被邀请新用户,
     * 50.提现-总额,51.提现-佣金,52提现-管理金
     */
    @Column(value = "type",
            commit = "0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账," +
                    "10.分销商的团队收益入账,11.合伙人的团队收益入账," +
                    "20.签到,21.签到阶段奖励," +
                    "30.订单取消,31.订单抵扣," +
                    "40.分享收益,41.被邀请新用户," +
                    "50.提现-总额,51.提现-佣金,52提现-管理金",
            isNUll = false, length = 4)
    private Integer type;

    @Column(value = "del_status", commit = "0:正常,1:删除,2:失效(该流水已生成新的流水),3:隐藏", length = 4, defaultVal = "0")
    private Integer delStatus;

    @Column( value = "remark", commit = "备注", length = 500 )
    private String remark;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

}
