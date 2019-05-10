package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;

import java.io.Serializable;

@Table
public class TGzVoucher implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    @Column(commit = "代金券金额")
    private Integer price;

    @Column(commit = "满减下限")
    private Integer reductionLimit;

    @Column(commit = "有效时长(时间戳)")
    private Long effectiveTime;

    @Column(commit = "激活时间点(时间戳)")
    private Long activationTime;

    @Column(commit = "可用状态(未激活、可用、已使用、已过期 etc.)")
    private Integer availableStatus;

    @Column(commit = "类型(通用、特定类型)")
    private Integer type;

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