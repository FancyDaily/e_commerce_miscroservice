package com.e_commerce.miscroservice.user.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/1 14:13
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_question_search")
public class YjjQuestionSearch {

    @Id
    private Long id;

    @Column(value = "question_id",length = 20,commit = "问题id")
    private Long questionId;
    @Column(value = "user_id",length = 20,commit = "用户id")
    private Long userId;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

    @Column(value = "del_status",length = 4,defaultVal = "0",commit = "是否删除 0 正常  1 删除")
    private Integer delStatus;
}
