package com.e_commerce.miscroservice.user.vo;

import lombok.Data;

import java.util.List;

@Data
public class TaskHallView {
    LevelView levelView;    //等级VO

    List<NoobTask> noobTasks;   //新手任务

    List<DailyTask> dailyTasks; //日常任务
}
