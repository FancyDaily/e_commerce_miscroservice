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

    private Integer type;

    private String key;

    private String value;

    @Column(defaultVal = "1")
    private String isValid;

    public Long getId() {
        return Id;
    }

    public Integer getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
