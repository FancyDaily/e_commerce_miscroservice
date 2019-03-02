package com.e_commerce.miscroservice.commons.entity.order;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/26 15:47
 * @Copyright 玖远网络
 */
@Data
public class OrderItemSkuVo {
    /**
     * 商品主图
     */
    private String img;
    /**
     * 商品标题
     */
    private String name;
    /**
     * 商品颜色名称
     */
    private String color;
    /**
     * 商品尺码名称
     */
    private String size;
/**
     * 数量
     */
    private Integer count;
/**
     * 金额
     */
    private Double price;

}
