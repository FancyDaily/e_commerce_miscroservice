package com.e_commerce.miscroservice.user.config;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

@Table(value = "t_timer_scheduling", commit = "定时器调度的PO")
@Data
@ToString
public class TimerSchedulerPO {

    @Id
    private Integer id;
    @Column(commit = "定时任务的uuid", length = 512)
    private String uuid;
    @Column(commit = "定时任务名称", length = 512)
    private String timerSchedulerName;
    @Column(commit = "定时任务 时间表达式(eg: * * * * * ?)", length = 512)
    private String timerSchedulerCron;
    @Column(commit = "定时任务参数(json格式的字符串)")
    private String timerSchedulerParam;
    @Column(commit = "定时任务的类型 更多参考timerSchedulerTypeEnum枚举", length = 3)
    private Integer timerSchedulerType;


    @Column(commit = "创建时间", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
    private Timestamp createTime;

    @Column(commit = "最后修改时间", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
    private Timestamp updateTime;
    @Column(commit = "是否删除 0 false 1 true", length = 2, defaultVal = "0")
    private Integer isDeleted;


    /**
     * 检查是否为空
     *
     * @return true 为空 false 为全不空
     */
    public boolean wasEmpty() {
        if (StringUtils.isAnyEmpty(uuid, timerSchedulerName, timerSchedulerParam)) {
            return true;
        }
        return false;
    }


}
