package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

@Table(commit = "观照-课程表")
@Data
public class TGzSubject implements Serializable {
    @Id
    private Long id;

    @Column(commit = "课程")
    private String name;

    @Column(commit = "周期(天数时长)")
    private Integer period;

    @Column(commit = "课程封面图")
    private String subjectHeadPortraitPath;

    @Column(commit = "原价", precision = 2)
    private Double price;

    @Column(commit = "优惠价", precision = 2)
    private Double forSalePrice;

    @Column(commit = "优惠状态")
    private Integer forSaleStatus;

    @Column(commit = "优惠剩余个数")
    private Integer forSaleSurplusNum;

    @Column(commit = "课程期次")
    private Integer seriesIndex;

    @Column(commit = "可用状态")
    private Integer avaliableStatus;

    @Column(commit = "可用日期")
    private Integer availableDate;

    @Column(commit = "可用时间")
    private Integer availableTime;

    @Column(commit = "描述图片地址", length = 2048)
    private String descPic;

    @Column(commit = "描述")
    private String remarks;

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