package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

@Table
@Data
public class TGzLesson implements Serializable {
    @Id
    private Long id;

    private Long subjectId;

    @Column(commit = "章节名称")
    private String name;

    @Column(commit = "可用状态")
    private Integer avaliableStatus;

    @Column(commit = "可用日期")
    private Integer availableDate;

    @Column(commit = "可用时间")
    private Integer availableTime;

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