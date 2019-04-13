package com.e_commerce.miscroservice.commons.enums.application;

import lombok.Data;

@Data
public enum TaskEnum {

    // 注册类型
    TASK_RIGESTER(0, Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_REGISTER.getPrice()), "注册奖励",-1,0l),
    // 实名认证
    TASK_AUTH(1, Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()), "实名认证奖励",-1,1l),
    // 完善个人主页
    TASK_PAGE(2, Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_PAGE.getPrice()), "完善个人主页奖励",-1,2l),

    TASK_SKILL(3,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_SKILL.getPrice()),"首次创建技能",-1,3l), // 首次创建技能
    TASK_FIRST_ITEM_DONE(4, Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次完成互助",-1,4l),    //首次完成互助
    TASK_FIRST_HELP_SEND(5,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次发布求助",-1,5l),    //首次发布求助
    TASK_FIRST_SERV_SEND(6,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次发布服务",-1,6l),    //首次发布服务
    TASK_FIRST_COMMENT(7,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次对互助进行评价",-1,7l),        //首次对互助进行评价
    TASK_FIRST_JOIN_COMPANY(8,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次加入组织",-1,8l),    //首次加入组织
    TASK_FIRST_PUBLIC_WELFARE_ACTY_DONE( 9,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次完成公益活动",-1,9l),    //首次完成公益活动
    //重复
    TASK_INVITE(20,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"邀请好友注册成功",-1,20l),    //邀请好友注册成功
    TASK_SIGN_UP(21,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"每日签到",1,21l),    //每日签到
    TASK_HELP_DONE(22,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"完成求助",5,22l),    //完成求助
    TASK_SERV_DONE(23,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"完成服务",5,23l),    //完成服务
    TASK_COMMENT(24,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"评价",5,24l),        //评价
    TASK_SHARE_PRODUCT(25,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"每日首次分享互助",1,25l),    //分享互助
    TASK_PUBLIC_WELFARE_ACTY_DONE(26,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"每日首次参加公益活动",1,26l);    //每日首次完成公益活动

    private Integer type;   //状态
    private Long reward; // 价值或者target_id
    private String desc;    //描述

    private Integer dailyMaxNum; //当日最大次数
    private Long targetId;

    TaskEnum(Integer type, Long reward, String desc, Integer dailyMaxNum, Long targetId) {
        this.type = type;
        this.reward = reward;
        this.desc = desc;
        this.dailyMaxNum = dailyMaxNum;
        this.targetId = targetId;
    }
}
