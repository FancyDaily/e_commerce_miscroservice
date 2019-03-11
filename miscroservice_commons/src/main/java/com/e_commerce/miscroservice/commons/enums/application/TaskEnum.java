package com.e_commerce.miscroservice.commons.enums.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskEnum {

    // 任务 -> 注册类型
    TASK_RIGESTER(0, 30l, "注册奖励"),
    // 任务 -> 实名认证
    TASK_AUTH(1, 30l, "实名认证奖励"),
    // 任务 -> 完善个人主页
    TASK_PAGE(2, 15l, "完善个人主页奖励"),
    // 任务 -> 签到类型
    TASK_SIGNUP(3, 3l, "签到奖励"),
    // 任务 -> 邀请好友
    TASK_INVITE(4, 30l, "邀请好友奖励"),
    // 任务 -> 完成首次互助
    TASK_HELP(5, 30l, "完成首次互助奖励");

    private Integer type;   //状态
    private Long reward; // 价值或者target_id
    private String desc;    //描述
}
