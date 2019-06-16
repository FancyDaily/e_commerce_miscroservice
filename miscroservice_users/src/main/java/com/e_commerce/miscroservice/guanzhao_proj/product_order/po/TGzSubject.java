package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.SubjectInfosVO;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(commit = "观照-课程表")
@Data
@Builder
public class TGzSubject implements Serializable {
    @Id
    private Long id;

	@Transient
    private boolean enrollable;	//可报名状态

    @Column(commit = "课程")
    private String name;

    @Column(commit = "周期(天数时长)", length = 11)
    private Integer period;

    @Column(commit = "简介(手机端竖图)")
    private String introPic;

    @Column(commit = "大纲(手机端竖图)")
    private String outLinePic;

    @Column(commit = "课程封面图")
    private String subjectHeadPortraitPath;

    @Column(commit = "原价", precision = 2)
    private Double price;

    @Column(commit = "优惠价", precision = 2)
    private Double forSalePrice;

    @Column(commit = "优惠状态", length = 11, defaultVal = "0")
    private Integer forSaleStatus;

    @Column(commit = "优惠剩余个数", length = 11, defaultVal = "0")
    private Integer forSaleSurplusNum;

    @Column(commit = "课程期次", length = 11)
    private Integer seriesIndex;

    @Column(commit = "可用状态", length = 11, defaultVal = "0")
    private Integer avaliableStatus;

    @Column(commit = "可用日期")
    private String availableDate;

    @Column(commit = "可用时间")
    private String availableTime;

    @Column(commit = "结束日期(课程结束日期)")
    private String endDate;

    @Column(commit = "结束时间")
    private String endTime;

    @Column(commit = "描述图片地址", length = 2048)
    private String descPic;

    @Column(commit = "描述")
    private String remarks;

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

//    private static final long serialVersionUID = 1L;

    public SubjectInfosVO copySubjectInfosVO() {
        return null;
    }
}
