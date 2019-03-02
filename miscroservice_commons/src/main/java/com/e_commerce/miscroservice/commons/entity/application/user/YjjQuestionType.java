package com.e_commerce.miscroservice.commons.entity.application.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/19 16:23
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_question_type")
public class YjjQuestionType {
    @Id
    private Long id;

    @Column(value = "type_num",length = 11,commit = "问题类型 num")
    private Integer typeNum;

    @Column(value = "type_value",length = 256,commit = "问题类型 value")
    private String typeValue;

    @Column(value = "create_by",defaultVal = "-",length = 256,commit = "问题创建人")
    private String createBy;

    @Column(value = "sort",defaultVal = "0",length = 11,commit = "排序")
    private Integer sort;


    @Column(value = "question_size",defaultVal = "0",length = 11,commit = "相关联的问题数量")
    private Integer questionSize;

    @Column(value = "del_status",defaultVal = "0",length = 4,commit = "是否删除 0 默认 正常 1删除")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

}
