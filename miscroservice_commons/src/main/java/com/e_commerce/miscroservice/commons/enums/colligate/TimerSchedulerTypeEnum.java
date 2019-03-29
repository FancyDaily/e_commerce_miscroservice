package com.e_commerce.miscroservice.commons.enums.colligate;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

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

    BONUS_PACKAGE_SEND_BACK_TASK(4, "bonus_package_send_back_task"),
    /**
     * 订单超时自动结束
     */
    ORDER_OVERTIME_END(3, "order_overtime_end"),
    /**
     * 订单发送消息
     */
    ORDER_SEND_MESSAGE(6, "order_send_message"),
    /**
     * 订单结束自动支付
     */
    ORDER_OVERTIME_PAY(7, "order_overtime_pay"),
    /**
     * 取消订单后拒绝时间赠礼
     */
    REMOVE_ORDER_PUNISHMENT(8, "remove_order_punishment"),
    /**
     * 订单支付自动评价
     */
    ORDER_OVERTIME_REMARK(5, "order_overtime_remark"),

    ORDER_DELAY_TASK(-20000, "orders6_delay_task");
    private int key;

    private String value;

    static {
        Set<Integer> keySets = new HashSet<>();
        EnumSet<TimerSchedulerTypeEnum> enums = EnumSet.noneOf(TimerSchedulerTypeEnum.class);
        TimerSchedulerTypeEnum[] values = values();
        for (TimerSchedulerTypeEnum schedulerTypeEnum : values) {
            if (!keySets.add(schedulerTypeEnum.key)) {
                enums.add(schedulerTypeEnum);
            }
        }

        if (enums.size() > 0) {
            throw new RuntimeException("timerSchedulerTypeEnum has conflict \n" + enums);
        }

    }


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
