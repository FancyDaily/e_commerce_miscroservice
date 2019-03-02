package com.e_commerce.miscroservice.commons.entity.application.activity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 17:45
 * @Copyright 玖远网络
 */
@Data
@Table("shop_sign")
public class Sign {
    @Id
    private Long id;

    @Column(value = "user_id",commit = "用户id",length = 20)
    private Long userId;

    @Column(value = "total_number",commit = "总签到天数",length = 11)
    private Integer totalNumber;

    @Column(value = "add_date",commit = "签到日期",length = 256)
    private String addDate;

    @Column(value = "number",commit = "当月连续签到天数",length = 11)
    private Integer number;
    @Column(value = "continue_number",commit = "最大连续签到天数",length = 11)
    private Integer continueNumber;

    @Column(value = "del_status",defaultVal = "0",length = 4,commit = "是否删除 默认0 未删除  1删除")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;
}
