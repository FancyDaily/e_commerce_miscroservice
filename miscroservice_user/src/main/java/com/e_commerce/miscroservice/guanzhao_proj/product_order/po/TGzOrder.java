package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.OrderDetailVO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(commit = "观照-订单表")
@Data
@Builder
@NoArgsConstructor
public class TGzOrder implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    private Long voucherId;

    @Column(commit = "本笔订单是否使用优惠")
    private Integer isSalePrice;

    @Column(commit = "订单编号")
    private String tgzOrderNo;
    @Column(commit = "课程名称")
    private String subjectName;

    @Column(commit = "订单状态", length = 11, defaultVal = "1", isNUll = false)
    private Integer status;

    @Column(commit = "订单价格", precision = 2)
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

    public OrderDetailVO copyOrderDetailVO() {
        return null;
    }

}
