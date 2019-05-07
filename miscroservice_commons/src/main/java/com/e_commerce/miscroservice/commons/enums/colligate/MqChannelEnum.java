package com.e_commerce.miscroservice.commons.enums.colligate;

/**
 * 公共的mq的通道名称
 */
public enum MqChannelEnum {

    /**
     * service_exchange 服务注册与发现用到  timer_scheduler_timer_send 发送定时任务调度
     * <p>
     * timer_scheduler_timer_accept_ 接受定时任务调度前缀(后缀请见TimerSchedulerTypeEnum的char)
     * <p>
     * collect_client_info 用来做收集客户端请求参数的消息队列
     * </p>
     */
    SERVICE_EXCHANGE("service_exchange"), TIMER_SCHEDULER_TIMER_SEND("timer_scheduler_timer_send"),
    TIMER_SCHEDULER_TIMER_ACCEPT_("timer_scheduler_timer_accept_"), COLLECT_CLIENT_INFO("collect_client_info"),
    DATA_ANALYSIS("data_analysis");

    private String value;

    MqChannelEnum(String value) {
        this.value = value;
    }

    public String toName() {
        return value;
    }
}
