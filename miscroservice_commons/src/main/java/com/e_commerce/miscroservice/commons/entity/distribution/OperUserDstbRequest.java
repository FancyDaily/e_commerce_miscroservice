package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/19 9:13
 * @Copyright 玖远网络
 */
@Data
public class OperUserDstbRequest extends BaseEntity{

    /**
     * 1:查询现金,2:查询金币
     */
    private Integer queryCashOrGoldGoin;

    /**
     * 会员ID
     */
    private Long userId;
    /**
     * 会员昵称
     */
    private String userNickName;
    /**
     * 分销角色 0无等级,1店长,2分销商,3合伙人
     */
    private Integer userDstbRoleType;
    /**
     * 1:进账,2:出账
     */
    private Integer inOutType;
    /**
     * @see OperUserDstbRequest#types 实际使用的查询字段
     *
     * 0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,
     * 10.分销商的团队收益入账,11.合伙人的团队收益入账,
     * 20.签到,21.签到阶段奖励,
     * 30.订单取消,31.订单抵扣,
     * 50.提现
     */
    private Integer outInContentType;
    /**
     * 实际的查询
     *
     * 0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,
     * 10.分销商的团队收益入账,11.合伙人的团队收益入账,
     * 20.签到,21.签到阶段奖励,
     * 30.订单取消,31.订单抵扣,
     * 50.提现
     */
    private List<Integer> types;
    /**
     * 1:待结算,2:已结算
     */
    private Integer status;
    /**
     * 所属商家名
     */
    private String storeName;
    /**
     * 交易开始时间
     */
    private Long createTimeCeil;
    /**
     * 交易结束时间
     */
    private Long createTimeFloor;
    /**
     * 结算开始时间(前端传)
     */
    private String cashSettleTimeFloor;
    /**
     * 结算结束时间(前端传)
     */
    private String cashSettleTimeCeil;
    /**
     * 结算开始时间(数据库实际查)
     */
    private Long operTimeFloor;
    /**
     * 结算结束时间(数据库实际查)
     */
    private Long operTimeCeil;
/*
    private String operTimeFloor;
    private String operTimeCeil;
    private String storeName;
    private Long userId;
    private String userNickName;*/
    /**
     * 历史现金收入低值
     */
    private BigDecimal historyCashInCeil;
    /**
     * 历史现金收入低值
     */
    private BigDecimal historyCashInFloor;
    /**
     * 历史金币收入高值
     */
    private BigDecimal historyGoldCoinInCeil;
    /**
     * 历史金币收入低值
     */
    private BigDecimal historyGoldCoinInFloor;
    /**
     * 团队人数低值
     */
    private Long teamUserCountCeil;
    /**
     * 团队人数高值
     */
    private Long teamUserCountFloor;
    /**
     * 粉丝人数低值
     */
    private Long fansUserCountCeil;
    /**
     * 粉丝人数高值
     */
    private Long fansUserCountFloor;

    public void setOperTimeFloorStr(String operTimeFloor) {
        this.operTimeFloor = TimeUtils.str2Long (operTimeFloor);
    }

    public void setOperTimeCeilStr(String operTimeCeil) {
        this.operTimeCeil = TimeUtils.str2Long (operTimeCeil);
    }


    public void setCreateTimeCeilStr(String createTimeCeil) {
        this.createTimeCeil = TimeUtils.str2Long (createTimeCeil);
    }

    public void setCreateTimeFloorStr(String createTimeFloor) {
        this.createTimeFloor = TimeUtils.str2Long (createTimeFloor);
    }
}
