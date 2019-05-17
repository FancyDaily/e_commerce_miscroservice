package com.e_commerce.miscroservice.guanzhao_proj.product_order;

import java.math.BigDecimal;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-15 09:18
 */
public class Test {
    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        long l = 6l * 24 * 3600 * 1000 * 30;
        System.out.println(l);
        System.out.println(currentTimeMillis);
        System.out.println(l + currentTimeMillis);

        String availableDate = "20190516";
        String year = availableDate.substring(0, 4);
        String month = availableDate.substring(4,6);
        String day = availableDate.substring(6);
        System.out.println(year + "," + month + "," + day);
        
    }

}
