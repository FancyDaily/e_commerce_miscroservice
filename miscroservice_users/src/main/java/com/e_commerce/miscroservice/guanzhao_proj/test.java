package com.e_commerce.miscroservice.guanzhao_proj;

import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;

public class test {
    public static void main(String[] args) {
        String str = "1.23.mp4";
        if(str.contains(".")) {
            str = str.substring(0, str.lastIndexOf("."));
        }
    }

}
