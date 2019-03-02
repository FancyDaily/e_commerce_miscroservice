package com.e_commerce.miscroservice.commons.entity.application.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;
import tk.mybatis.mapper.annotation.ColumnType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 描述 代理商品表
 *
 * @author hyq
 * @date 2018/9/18 13:52
 */
@Data
@Table("yjj_proxy_goods_type")
@javax.persistence.Table(name = "yjj_proxy_goods_type")
public class ProxyGoodsType implements Serializable {
    @Id
    @javax.persistence.Id
    private Long id;

    @Column(value = "one_level", commit = "一级返", length = 10,precision = 5)
    private BigDecimal oneLevel;

    @Column(value = "two_level", commit = "二级返利", length = 10,precision = 5)
    private BigDecimal twoLevel;

    @Column(value = "type_desc", commit = "描述", length = 256)
    private String typeDesc;

    @Column(value = "create_time", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "修改时间")
    private Timestamp updateTime;


}
