package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

@Table(commit = "观照-用户课程关联表")
@Data
public class TGzUserSubject implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    @Column(commit = "状态")
    private Integer status;

    @Column(commit = "笔记数量")
    private Integer notesNum;

    @Column(commit = "作业总数")
    private Integer homeworkExpectedNum;

    @Column(commit = "作业完成数")
    private Integer homeworkDoneNum;

    @Column(commit = "学习进度")
    private Integer completion;

    @Column(commit = "失效时间戳")
    private Long expireTime;

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