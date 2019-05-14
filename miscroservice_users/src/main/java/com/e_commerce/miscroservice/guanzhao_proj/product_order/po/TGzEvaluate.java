package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(commit = "观照-评价表")
@Data
@Builder
public class TGzEvaluate implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long lessonId;

    private Long subjectId;

    @Column(commit = "好评级别(差评、中评、好评)", length = 11, defaultVal = "2")
    private Integer level;

    @Column(commit = "描述")
    private String comment;

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