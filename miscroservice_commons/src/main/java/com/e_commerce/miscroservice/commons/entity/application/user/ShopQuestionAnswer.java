package com.e_commerce.miscroservice.commons.entity.application.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/19 10:52
 * @Copyright 玖远网络
 */
@Data
@Table("shop_question_answer")
public class ShopQuestionAnswer implements Serializable {
    @Id
    private Long id;

    @Column(value = "question_type_id",length = 20,commit = "问题类型id")
    private Long questionTypeId;

    @Column(value = "question",length = 256,commit = "问题")
    private String question;

    @Column(value = "answer",length = 2048,commit = "答案")
    private String answer;

    @Column(value = "create_by",length = 256,defaultVal = "-",commit = "创建人")
    private String createBy;

    @Column(value = "useful",length = 11,defaultVal = "0",commit = "有用")
    private Integer useful;
    @Column(value = "useless",length = 11,defaultVal = "0",commit = "无用")
    private Integer useless;
//    @Column(value = "page_view ",length = 11,commit = "浏览量")
//    private Integer pageView ;
    @Column(value = "sort",length = 11,defaultVal = "0",commit = "排序")
    private Integer sort ;

    @Column(value = "status",length = 2,defaultVal = "0",commit = "是否展示 0 展示 1隐藏")
    private Integer status;

    @Column(value = "search_time",length = 11,defaultVal = "0",commit = "浏览量")
    private Integer searchTime;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

    @Column(value = "del_status",length = 4,defaultVal = "0",commit = "是否删除 0 正常  1 删除")
    private Integer delStatus;
}
