package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 19:22
 */
@Table
@Data
public class TGzKeyValue {

    @Id
    private Long Id;

    @Column(commit = "类别")
    private Integer type;

    @Column(commit = "键")
    private String key;

    @Column(commit = "值")
    private String value;

    @Column(defaultVal = "1")
    private String isValid;
}
