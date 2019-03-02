package com.e_commerce.miscroservice.commons.enums.colligate;

/**
 * 任务构建的path枚举
 */
public enum TaskBuildPathEnum {
    TIMER_SCHEDULING_TASK_PATH("/timerSchedulingTask"), TIMER_RED_PACKETS_ENDED_TASK("/timerRedPacketsEndedTask");

    TaskBuildPathEnum(String path) {
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }
}
