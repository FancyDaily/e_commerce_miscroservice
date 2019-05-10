package com.e_commerce.miscroservice.guanzhao_proj.product_order.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-10 14:01
 */
@Data
public class SubjectInfosVO {
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
    private String availableDate;

    @Column(commit = "可用时间")
    private String availableTime;

    @Column(commit = "描述图片地址数组", length = 2048)
    private String[] descPicArray;

    @Column(commit = "描述图片地址", length = 2048)
    private String descPic;

    @Column(commit = "描述")
    private String remarks;

    @Column(commit = "扩展字段")
    private String extend;

    @Column(commit = "创建者编号")
    private Long createUser;

    @Column(commit = "创建时间戳")
    private Timestamp createTime;

    @Column(commit = "更新者编号")
    private Long updateUser;

    @Column(commit = "更新时间戳")
    private Timestamp updateTime;

    @Column(commit = "有效性")
    private String isValid;

    private static final long serialVersionUID = 1L;
}
