package com.e_commerce.miscroservice.user.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class TaskHallView {
    LevelView levelView;    //等级VO

    List<NoobTask> noobTasks;   //新手任务

    List<DailyTask> dailyTasks; //日常任务
}
