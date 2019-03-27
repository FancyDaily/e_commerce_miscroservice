package com.e_commerce.miscroservice.commons.enums.colligate;

/**
 * 定时任务类型枚举
 * 普通定是任务命名随意
 * 延迟队列任务命名 xx_DELAY_TASK 并且key是负数 key是表示具体延迟的时间 单位是毫秒
 * eg ORDER_DELAY_TASK(-10000, "order_delay_task") 代表订单的延迟队列 延迟的时间为10秒
 */
public enum TimerSchedulerTypeEnum {
    /**
     * CALLBACK_REQUEST 回调请求的定时枚举 ORDER_DELAY_TASK 订单延迟队列枚举
     */
    CALLBACK_REQUEST(1, "callback_request"),
    TEST(2, "test"),
    WITHDRAW_DEPOSIT_BANK_DELAY_TASK(-30000, "withdraw_deposit_bank_delay_task"),
    ORDER_OVERTIME_END(3, "order_overtime_end"),
    ORDER_OVERTIME_PAY(4, "order_overtime_pay"),
    ORDER_DELAY_TASK(-20000, "orders6_delay_task");
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
