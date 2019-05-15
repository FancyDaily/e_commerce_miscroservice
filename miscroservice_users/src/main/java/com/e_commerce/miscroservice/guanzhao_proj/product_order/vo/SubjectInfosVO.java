package com.e_commerce.miscroservice.guanzhao_proj.product_order.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-10 14:01
 */
@Data
public class SubjectInfosVO {
    private Long id;

    private Long payCreateTimeMills;    //未支付订单创建时间点

    private Long surplusPayMills;   //剩余时间戳

    private Long surplusToAvailableTime;    //距离开始(开放)时间的时间戳

    private Integer surplusToAvailableDayCnt;  //距离开始时间的天数

    private String surplusToAvailableFormatStr;   //格式化的距离开始时间的字符串h:m:s

    private String orderNo;   //微信支付订单号

    @Column(commit = "课程")
    private String name;

    @Column(commit = "周期(天数时长)", length = 11)
    private Integer period;

    @Column(commit = "课程封面图")
    private String subjectHeadPortraitPath;

    @Column(commit = "原价", precision = 2)
    private Double price;

    @Column(commit = "优惠价", precision = 2)
    private Double forSalePrice;

    @Column(commit = "优惠状态", length = 11)
    private Integer forSaleStatus;

    @Column(commit = "优惠剩余个数", length = 11)
    private Integer forSaleSurplusNum;

    @Column(commit = "课程期次", length = 11)
    private Integer seriesIndex;

    @Column(commit = "可用状态", length = 11)
    private Integer avaliableStatus;

    @Column(commit = "可用日期")
    private String availableDate;

    @Column(commit = "可用时间")
    private String availableTime;

    @Column(commit = "结束日期(课程结束日期)")
    private String endDate;

    @Column(commit = "结束时间")
    private String endTime;

    @Column(commit = "描述图片地址数组", length = 2048)
    private String[] descPicArray;

    private List<TGzLesson> lessonList;

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
