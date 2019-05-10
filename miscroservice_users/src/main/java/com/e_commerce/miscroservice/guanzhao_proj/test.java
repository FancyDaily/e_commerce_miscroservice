package com.e_commerce.miscroservice.guanzhao_proj;

public class test {
    public static void main(String[] args) {
        int num1 = 0;
        int num2 = 0;
        float f_num1 = Float.valueOf(num1);
        float f_num2 = Float.valueOf(num2);
        double floor = (f_num1 / f_num2) * 100;
        String temp = String.valueOf(floor).substring(0,2);
        String str = temp.endsWith(".")? temp.substring(0,temp.length()-1): temp;
        System.out.println(str);
    }
}
