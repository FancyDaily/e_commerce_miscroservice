package com.e_commerce.miscroservice.commons.enums.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskEnum {

    // 注册类型
    TASK_RIGESTER(0, Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_REGISTER.getPrice()), "注册奖励"),
    // 实名认证
    TASK_AUTH(1, Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()), "实名认证奖励"),
    // 完善个人主页
    TASK_PAGE(2, 15l, "完善个人主页奖励"),

    TASK_SKILL(3,15l,"首次创建技能"), // 首次创建技能
    TASK_FIRST_ITEM_DONE(4, Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次完成互助"),    //首次完成互助
    TASK_FIRST_HELP_SEND(5,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次发布求助"),    //首次发布求助
    TASK_FIRST_SERV_SEND(6,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次发布服务"),    //首次发布服务
    TASK_FIRST_COMMENT(7,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次对互助进行评价"),        //首次对互助进行评价
    TASK_FIRST_JOIN_COMPANY(8,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次加入组织"),    //首次加入组织
    TASK_FIRST_PUBLIC_WELFARE_ACTY_DONE( 9,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"首次完成公益活动"),    //首次完成公益活动
    //重复
    TASK_INVITE(20,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"邀请好友注册成功"),    //邀请好友注册成功
    TASK_SIGN_UP(21,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"每日签到"),    //每日签到
    TASK_HELP_DONE(22,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"完成求助"),    //完成求助
    TASK_SERV_DONE(23,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"完成服务"),    //完成服务
    TASK_COMMENT(24,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"评价"),        //评价
    TASK_SHARE_PRODUCT(25,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"每日首次分享互助"),    //分享互助
    TASK_PUBLIC_WELFARE_ACTY_DONE(26,Long.valueOf(GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH.getPrice()),"每日首次参加公益活动");    //每日首次完成公益活动

    private Integer type;   //状态
    private Long reward; // 价值或者target_id
    private String desc;    //描述
}
