package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(commit = "观照-订单表")
@Data
public class TGzOrder implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    @Column(commit = "课程名称")
    private Long subjectName;

    @Column(commit = "订单状态", length = 11)
    private Integer status;

    @Column(commit = "订单价格")
    private Double price;

    @Column(commit = "订单创建时间戳")
    private Long orderTime;

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