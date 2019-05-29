package com.e_commerce.miscroservice.guanzhao_proj.product_order;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-24 16:52
 */
public class Property {
    // 店铺属性
    String name;
    // 距离，单位:米
    Integer distance;
    // 销量，月售
    Integer sales;
    // 价格，这里简单起见就写一个级别代表价格段
    Integer priceLevel;

    public Property(String name, int distance, int sales, int priceLevel) {
        this.name = name;
        this.distance = distance;
        this.sales = sales;
        this.priceLevel = priceLevel;
    }
    // getter setter 省略
}
