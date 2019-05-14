package com.e_commerce.miscroservice.guanzhao_proj.product_order.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-10 17:05
 */
@Data
public class MyLessonVO {

    private Long userId;

    private Long subjectId;

    private Long lessonId;

    private Integer videoOnloadStatus;  //视频装载状态

    @Column(commit = "章节序号", length = 11)
    private Integer lessonIndex;

    @Column(commit = "可用日期")
    private String availableDate;

    @Column(commit = "可用时间")
    private String availableTime;

    @Column(commit = "可用状态", length = 11)
    private Integer avaliableStatus;

    @Column(commit = "章节名称")
    private String name;

    @Column(commit = "章节对应视频签名")
    private String sign;

    @Column(commit = "视频进度", length = 11)
    private Integer videoCompletion;

    @Column(defaultVal = "0",commit = "章节完成状态", length = 11)
    private Integer lessonCompletionStatus;

    @Column(commit = "扩展字段")
    private String extend;

    @Column(commit = "创建者编号")
    private Long createUser;

    @Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
    private Timestamp createTime;

    @Column(commit = "更新者编号")
    private Long updateUser;

    @Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
    private Timestamp updateTime;

    @Column(commit = "有效性", defaultVal = "1")
    private String isValid;
}
