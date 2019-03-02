package com.e_commerce.miscroservice.commons.enums.colligate;

/**
 * 定时任务类型枚举
 * 普通定是任务命名随意
 * 延迟队列任务命名 xx_DELAY_TASK 并且key是负数 key是表示具体延迟的时间 单位是毫秒
 * eg ORDER_DELAY_TASK(-10000, "order_delay_task") 代表订单的延迟队列 延迟的时间为10秒
 */
public enum TimerSchedulerTypeEnum {
    /**
     * RED_PACKET 红包枚举  ORDER_DELAY_TASK 订单延迟队列枚举
     */
    RED_PACKET_ENDED(1, "red_packet_ended"),
    INVEST_STATIS_MESSAGE(2, "invest_statis_message"),
    ORDER_DELAY(3, "order_delay"),
    ORDER_FAILURE_INSPECT(4, "order_failure_inspect"),
    SEND_GOOD_AFTER_15DAYS(5, "send_good_after_15days"),
    WITHDRAW_DEPOSIT_BANK_DELAY_TASK(-30000, "withdraw_deposit_bank_delay_task"),
    ORDER_DELAY_TASK(-20000, "orders6_delay_task"),
    //    WITHDRAW_DEPOSIT_BANK_DELAY_TASK(-5000, "withdraw_deposit_bank_delay_task"),
    WITHDRAW_DEPOSIT_BANK(5, "withdraw_deposit_bank");
    //    ORDER_DELAY_TASK(-20000, "orders6_delay_task");
    private int key;

    private String value;

    TimerSchedulerTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 转成数字
     *
     * @return
     */
    public int toNum() {
        return key;
    }

    /**
     * 转成字符串
     *
     * @return
     */
    public String toChar() {
        return value;
    }

    /**
     * 根据数字 转成字符串
     *
     * @param num
     * @return
     */
    public static String num2Char(Integer num) {

        String result = "";
        if (num == null) {

            return result;
        }

        TimerSchedulerTypeEnum[] values = TimerSchedulerTypeEnum.values();

        for (TimerSchedulerTypeEnum typeEnum : values) {
            if (typeEnum.key == num) {
                result = typeEnum.value;
                break;
            }
        }
        return result;


    }


}
