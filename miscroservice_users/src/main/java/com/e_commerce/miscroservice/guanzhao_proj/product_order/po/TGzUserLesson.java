package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

@Table(commit = "观照-用户章节关联表")
@Data
public class TGzUserLesson implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    private Long lessonId;

    @Column(commit = "章节对应视频签名")
    private String sign;

    @Column(commit = "我的章节状态(可用、不可用)")
    private Integer status;

    @Column(commit = "视频进度")
    private Integer videoCompletion;

    @Column(defaultVal = "0",commit = "章节完成状态")
    private Integer lessonCompletionStatus;

    @Column(commit = "扩展字段")
    private String extend;

    @Column(commit = "创建者编号")
    private Long createUser;

    @Column(commit = "创建者姓名")
    private String createUserName;

    @Column(commit = "创建时间戳")
    private Long createTime;

    @Column(commit = "更新者编号")
    private Long updateUser;

    @Column(commit = "更新者姓名")
    private String updateUserName;

    @Column(commit = "更新时间戳")
    private Long updateTime;

    @Column(commit = "有效性")
    private String isValid;

    private static final long serialVersionUID = 1L;


}