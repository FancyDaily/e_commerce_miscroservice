package com.e_commerce.miscroservice.commons.enums.colligate;

/**
 * 公共的mq的通道名称
 */
public enum MqChannelEnum {

    /**
     * service_exchange 到  time服务注册与发现用r_scheduler_timer_accept 定时任务调度接受
     * <p>
     * timer_scheduler_timer_send_ 定时任务调度发送前缀(后缀请见TimerSchedulerTypeEnum的char)
     * <p>
     * collect_client_info 用来做收集客户端请求参数的消息队列
     * </p>
     */
    SERVICE_EXCHANGE("service_exchange"), TIMER_SCHEDULER_TIMER_ACCEPT("timer_scheduler_timer_accept"),
    TIMER_SCHEDULER_TIMER_SEND_("timer_scheduler_timer_send_"), COLLECT_CLIENT_INFO("collect_client_info"),
    NEW_USER_GRANT_HB("new_user_grant_hb"), INVITATION_USER_GRANT_HB("invitation_user_grant_hb"), IDFA_OR_IMEI("idfa_or_imei"),
//    SING_UP_GIRT_RED_PACKET("sing_up_gift_red_packet"),INVITATION_GIFT_JD_CARD("invitation_gift_jd_card"),TEAM_GIRT_JD_CAARD("team_gift_jd_card"),
    NEW_YEAR_GRANT_GIFT("new_year_grant_gift"), GRANT_GIFT_TO_USER("grant_gift_to_user"), GENERATE_CONTRACT("generate_contract"), DATA_ANALYSIS("data_analysis"),
    LOAN_MONEY("loan_money"), AUTO_UP_BORROW("auto_up_borrow");

    private String value;

    MqChannelEnum(String value) {
        this.value = value;
    }

    public String toName() {
        return value;
    }
}
