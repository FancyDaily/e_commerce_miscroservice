package com.e_commerce.miscroservice.commons.entity.application.activity;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table("shop_sign_log")
public class SignLog implements Serializable {

    /**
     *id
     */
    @Id
    private Integer id;

    /**
     *用户id
     */
    @Column(value = "user_id",commit = "用户id",length = 20)
    private Long userId;


    /**
     *添加日期
     */
    @Column(value = "add_date",commit = "添加日期",length = 256)
    private String addDate;

    /**
     *备注
     */
    @Column(value = "remark",commit = "备注",length = 256)
    private String remark;

    @Column(value = "del_status",commit = "删除状态 默认 0  正常 1 删除",defaultVal = "0",length = 4)
    private Integer delStatus;

    @Column(value = "create_time",commit = "创建时间",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
    private Timestamp createTime;

    @Column(value = "update_time",commit = "修改时间",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
    private Timestamp updateTime;
}