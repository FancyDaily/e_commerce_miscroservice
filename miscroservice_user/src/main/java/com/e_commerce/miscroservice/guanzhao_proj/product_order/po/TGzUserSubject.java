package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(commit = "观照-用户课程关联表")
@Data
@Builder
@NoArgsConstructor
public class TGzUserSubject implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    @Column(commit = "状态", length = 11)
    private Integer status;

    @Column(commit = "笔记数量", length = 11, defaultVal = "0")
    private Integer notesNum;

    @Column(commit = "作业总数", length = 11, defaultVal = "0")
    private Integer homeworkExpectedNum;

    @Column(commit = "作业完成数", length = 11, defaultVal = "0")
    private Integer homeworkDoneNum;

    @Column(commit = "学习进度", length = 11, defaultVal = "0")
    private Integer completion;

    @Column(commit = "失效时间戳")
    private Long expireTime;

    @Column(commit = "扩展字段")
    private String extend;

    @Column(commit = "创建者编号", isNUll = false)
    private Long createUser;

    @Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
    private Timestamp createTime;

    @Column(commit = "更新者编号", isNUll = false)
    private Long updateUser;

    @Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
    private Timestamp updateTime;

    @Column(commit = "有效性", defaultVal = "1")
    private String isValid;

    private static final long serialVersionUID = 1L;

}
