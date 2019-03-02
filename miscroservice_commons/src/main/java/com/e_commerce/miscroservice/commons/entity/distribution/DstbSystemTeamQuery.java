package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台分销用户查询
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/11/7 16:25
 * @Copyright 玖远网络
 */
@Data
public class DstbSystemTeamQuery extends BaseEntity{
    /**
     * 查询用户id
     */
    private Long userId;
    /**
     * 查询用户的拥有的team中的uesrId
     */
    private Long teamMemberId;
    /**
     * 会员昵称
     */
    private String memberName;
    /**
     * 会员角色
     */
    private Integer grade;
    /**
     * 直属上级id
     */
    private Long superiorUserId;
    /**
     * 直属上级昵称
     */
    private String superiorUserName;
    /**
     * 直属上级角色
     */
    private Integer superiorUserGrade;

    /**
     * 推荐人用户id
     */
    private Long recommendUserId;
    /**
     * 推荐人用户名
     */
    private String recommendUserName;
//    /**
//     * 注册时间(前端传)
//     */
//    private String registerTimeReadableCeil;
//    /**
//     * 注册时间(前端传)
//     */
//    private String registerTimeReadableFloor;

    /**
     * 注册时间(sql查)
     */
    private Long registerTimeCeil;

    /**
     * 注册时间(sql查)
     */
    private Long registerTimeFloor;


    /**
     * 累计现金收益
     */
    private BigDecimal historyCashEarningCeil;
    /**
     * 累计现金收益
     */
    private BigDecimal historyCashEarningFloor;
    /**
     * 累计金币收益
     */
    private BigDecimal historyGoldCoinEarningCeil;
    /**
     * 累计金币收益
     */
    private BigDecimal historyGoldCoinEarningFloor;

    /**
     * 粉丝级别 1:1级粉丝,2:2级粉丝, null:查询所有
     */
    private Integer fansType;

    /**
     * 分销商的名字
     */
    private String distributorUserName;
    /**
     * 合伙人的名字
     */
    private String partnerUserName;
    /**
     * 小程序所属商家 商户名
     */
    private String storeBusinessName;


    private Long topUserId;
    private Long higherUserId;


    public void setRegisterTimeFloorStr(String registerTimeFloor) {
        this.registerTimeFloor = TimeUtils.str2Long (registerTimeFloor);
    }


    public void setRegisterTimeCeilStr(String registerTimeCeil) {
        this.registerTimeCeil = TimeUtils.str2Long(registerTimeCeil);
    }

}
