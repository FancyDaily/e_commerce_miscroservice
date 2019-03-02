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
@Table("yjj_proxy_goods")
@javax.persistence.Table(name = "yjj_proxy_goods")
public class ProxyGoods implements Serializable {
    @Id
    @javax.persistence.Id
    private Long id;

//    @ColumnType(column = "user_id")
//    @Column(value = "user_id", commit = "用户ID", length = 20)
//    private Long userId;

    @ColumnType(column = "type_id")
    @Column(value = "type_id", commit = "商品类型ID", length = 20)
    private Long typeId;

    @Column(value = "goods_name", commit = "商品名称", length = 36)
    private String goodsName;

    @Column(value = "goods_price", commit = "商品价格", length = 36 ,precision = 2)
    private BigDecimal goodsPrice;

    @Column(value = "sales_volume", commit = "销量", length = 7, defaultVal = "0")
    private Integer salesVolume;

    @Column(value = "all_volume", commit = "销量", length = 7, defaultVal = "0")
    private Integer allVolume;

    @Column(value = "time_num", commit = "时间数量", length = 2, defaultVal = "1")
    private Integer timeNum;

    @Column(value = "time_unit", commit = "时间单位 0 年 1 月 2 日", length = 2, defaultVal = "0")
    private Integer timeUnit;

    @Column(value = "goods_images", commit = "商品详情图", length = 4096)
    private String goodsImages;

    @Column(value = "main_images", commit = "商品主要图", length = 150)
    private String mainImages;

    @Column(value = "goods_detail", commit = "商品详情", length = 4096)
    private String goodsDetail;

    @Column(value = "status",commit = "状态 0 下架 1 上架",defaultVal ="1" ,length = 2)
    private Integer status;

    @Column(value = "del_status",commit = "删除状态 0 未删除 1 删除",defaultVal ="0" ,length = 2)
    private Integer delStatus;

    @Column(value = "create_time", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "修改时间")
    private Timestamp updateTime;


}
