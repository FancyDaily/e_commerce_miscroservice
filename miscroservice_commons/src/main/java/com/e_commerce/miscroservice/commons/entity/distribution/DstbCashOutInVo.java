package com.e_commerce.miscroservice.commons.entity.distribution;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * 分销流水
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 14:14
 * @Copyright 玖远网络
 */
@Data
public class DstbCashOutInVo{

    private Log logger = Log.getInstance(DstbCashOutInVo.class);

    private String orderNo;
    /**
     * 订单金额
     */
    private String orderMoney;
    /**
     * 分销商
     */
    private Long distributor;
    /**
     * 合伙人
     */
    private Long partner;

    /**
     * RMB金币汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 用户自己账号
     */
    private ShopMemberAccount selfAccount;

    /**
     * 上级用户账号
     */
    private ShopMemberAccount higherAccount;

    /**
     * 上上级用户账号
     */
    private ShopMemberAccount topAccount;

    /**
     * 分销商账号
     */
    private ShopMemberAccount dstbAccount;

    /**
     * 合伙人账号
     */
    private ShopMemberAccount partnerAccount;


    /**
     * 自有分佣
     */
    private ShopMemAcctCashOutInQuery selfCommission;
    /**
     * 一级分佣
     */
    private ShopMemAcctCashOutInQuery higherCommission;

    /**
     * 2级分佣
     */
    private ShopMemAcctCashOutInQuery topCommission;

    /**
     * 分销商团队收益
     */
    private ShopMemAcctCashOutInQuery dstbTeamIn;

    /**
     * 合伙人团队收益
     */
    private ShopMemAcctCashOutInQuery partnerTeamIn;


    private void setDstbTeamIn(ShopMemAcctCashOutInQuery dstbTeamIn) {
        this.dstbTeamIn = dstbTeamIn;
    }

    private void setPartnerTeamIn(ShopMemAcctCashOutInQuery partnerTeamIn) {
        this.partnerTeamIn = partnerTeamIn;
    }

    /**
     * 添加分销商团队收益
     *
     * @param addInfo addInfo
     * @return com.e_commerce.miscroservice.commons.entity.distribution.DstbCashOutInVo
     * @author Charlie
     * @date 2018/10/10 14:27
     */
    public DstbCashOutInVo appendDstbTeamIn(ShopMemAcctCashOutInQuery addInfo) {
        logger.info ("记录分销商团队收益 addInfo.isNull={}", addInfo==null);

        if (this.dstbTeamIn == null) {
            logger.info ("没有团队收益,直接初始化");
            setDstbTeamIn (addInfo);
            return this;
        }
        if (addInfo == null) {
            logger.info ("没有团队收益,不进行操作");
            return this;
        }
        if (! ObjectUtils.nullSafeEquals (dstbTeamIn.getOrderNo (), addInfo.getOrderNo ())
                || ! ObjectUtils.nullSafeEquals (dstbTeamIn.getUserId (), addInfo.getUserId ())
                ) {
            throw ErrorHelper.me ("添加分销商团队收益状态错误, 订单号或者分销商用户不匹配");
        }

        logger.info ("追加分销商团队收益 已有的管理金总收益={},已有的管理金现金={},已有的管理金金币={}," +
                        "新添的管理金总收益={},新添的管理金现金={},新添的管理金金币={}",
                dstbTeamIn.getOrderEarningsSnapshoot (), dstbTeamIn.getOperCash (), dstbTeamIn.getOperGoldCoin (),
                addInfo.getOrderEarningsSnapshoot(), addInfo.getOperCash (), addInfo.getOperGoldCoin ()
        );

        this.dstbTeamIn.setOrderEarningsSnapshoot (dstbTeamIn.getOrderEarningsSnapshoot ().add (addInfo.getOrderEarningsSnapshoot ()));
        this.dstbTeamIn.setOperCash (dstbTeamIn.getOperCash ().add (addInfo.getOperCash ()));
        this.dstbTeamIn.setOperGoldCoin (dstbTeamIn.getOperGoldCoin ().add (addInfo.getOperGoldCoin ()));
        return this;
    }


    /**
     * 添加合伙人团队收益
     *
     * @param addInfo addInfo
     * @return com.e_commerce.miscroservice.commons.entity.distribution.DstbCashOutInVo
     * @author Charlie
     * @date 2018/10/10 14:27
     */
    public DstbCashOutInVo appendPartnerTeamIn(ShopMemAcctCashOutInQuery addInfo) {
        logger.info ("记录合伙人团队收益 addInfo.isNull={}", addInfo==null);
        //没有直接添加,有了更新相加
        if (this.partnerTeamIn == null) {
            logger.info ("没有合伙人收益,直接初始化");
            setPartnerTeamIn (addInfo);
            return this;
        }
        if (addInfo == null) {
            logger.info ("没有团队收益,不进行操作");
            return this;
        }
        if (! ObjectUtils.nullSafeEquals (partnerTeamIn.getOrderNo (), addInfo.getOrderNo ())
                || ! ObjectUtils.nullSafeEquals (partnerTeamIn.getUserId (), addInfo.getUserId ())
                ) {
            throw ErrorHelper.me ("添加分销商团队收益状态错误, 订单号或者分销商用户不匹配");
        }
        logger.info ("追加合伙人团队收益 已有的管理金总收益={},已有的管理金现金={},已有的管理金金币={}," +
                "新添的管理金总收益={},新添的管理金现金={},新添的管理金金币={}",
                partnerTeamIn.getOrderEarningsSnapshoot (), partnerTeamIn.getOperCash (), partnerTeamIn.getOperGoldCoin (),
                addInfo.getOrderEarningsSnapshoot(), addInfo.getOperCash (), addInfo.getOperGoldCoin ()
                );

        this.partnerTeamIn.setOrderEarningsSnapshoot (partnerTeamIn.getOrderEarningsSnapshoot ().add (addInfo.getOrderEarningsSnapshoot ()));
        this.partnerTeamIn.setOperCash (partnerTeamIn.getOperCash ().add (addInfo.getOperCash ()));
        this.partnerTeamIn.setOperGoldCoin (partnerTeamIn.getOperGoldCoin ().add (addInfo.getOperGoldCoin ()));
        return this;
    }


    public boolean isTeamInNull() {
        return this.dstbTeamIn == null && this.partnerTeamIn == null;
    }

    public boolean isCommissionNull() {
        return this.selfCommission == null &&
                this.higherCommission == null &&
                this.topCommission == null;
    }


}
