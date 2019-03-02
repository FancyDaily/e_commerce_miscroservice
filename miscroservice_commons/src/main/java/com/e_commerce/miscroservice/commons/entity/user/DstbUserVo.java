package com.e_commerce.miscroservice.commons.entity.user;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/7 19:25
 * @Copyright 玖远网络
 */
@Data
public class DstbUserVo{


    /**
     * 会员id
     */
    private Long userId;
    /**
     * 分销级别
     */
    private Integer grade;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 绑定手机号
     */
    private String bindPhone;
    /**
     * 所属商家名
     */
    private String storeBusinessName;
    /**
     * 直属上级userId
     */
    private Long superiorUserId;
    /**
     * 直属上级昵称
     */
    private String superiorUserName;
    /**
     * 直属上级等级
     */
    private Integer superiorUserGrade;
    /**
     * 推荐人userId
     */
    private Long recommendUserId;
    /**
     * 推荐人昵称
     */
    private String recommendUserName;
    /**
     * 推荐人级别
     */
    private String recommendUserGrade;
    /**
     * 团队人数
     */
    private Long teamUserCount;
    /**
     * 粉丝人数
     */
    private Long fansUserCount;
    /**
     * 累计现金收益
     */
    private BigDecimal historyCashEarning;
    /**
     * 累计金币收益
     */
    private BigDecimal historyGoldCoinEarning;
    /**
     * 用户注册登录时间
     */
    private Long createTime;
    /**
     * 用户注册登录时间
     */
    private String createTimeReadable;

    /**
     * 粉丝级别
     */
    private Integer whichFans;
    /**
     * 0女,1男,2未知
     */
    private Integer userSex;

}
