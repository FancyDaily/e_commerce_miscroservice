package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(commit = "观照-用户章节关联表")
@Data
@Builder
public class TGzUserLesson implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    private Long lessonId;

    @Column(commit = "章节对应视频签名")
    private String sign;

    @Column(commit = "我的章节状态(可用、不可用)", length = 11, defaultVal = "1")
    private Integer status;

    @Column(commit = "视频进度", length = 11, defaultVal = "0")
    private Integer videoCompletion;

    @Column(commit = "视频完成状态", length = 11, defaultVal = "0")
    private Integer videoCompletionStatus;

    @Column(commit = "章节完成状态", length = 11, defaultVal = "0")
    private Integer lessonCompletionStatus;

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