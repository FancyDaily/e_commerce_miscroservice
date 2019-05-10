package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

@Table(commit = "观照-订单表")
@Data
public class TGzOrder implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    @Column(commit = "课程名称")
    private Long subjectName;

    @Column(commit = "订单状态")
    private Integer status;

    @Column(commit = "订单价格")
    private Double price;

    @Column(commit = "订单创建时间戳")
    private Long orderTime;

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