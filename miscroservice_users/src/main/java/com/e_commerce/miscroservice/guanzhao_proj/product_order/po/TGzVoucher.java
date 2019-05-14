package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyVoucherVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(commit = "观照-代金券表")
@Data
@Builder
public class TGzVoucher implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    @Column(commit = "代金券金额")
    private Double price;

    @Column(commit = "满减下限", length = 11)
    private Double reductionLimit;

    @Column(commit = "有效时长(时间戳)")
    private Long effectiveTime;

    @Column(commit = "激活时间点(时间戳)")
    private Long activationTime;

    @Column(commit = "可用状态(未激活、可用、已使用、已过期 etc.)", length = 11)
    private Integer availableStatus;

    @Column(commit = "类型(通用、特定类型)", length = 11)
    private Integer type;

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

    public MyVoucherVo copyMyVoucherVo() {
        return null;
    }

}