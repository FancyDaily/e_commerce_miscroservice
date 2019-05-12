package com.e_commerce.miscroservice.guanzhao_proj;

import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;

public class test {
    public static void main(String[] args) {
        /*int num1 = 0;
        int num2 = 0;
        float f_num1 = Float.valueOf(num1);
        float f_num2 = Float.valueOf(num2);
        double floor = (f_num1 / f_num2) * 100;
        String temp = String.valueOf(floor).substring(0,2);
        String str = temp.endsWith(".")? temp.substring(0,temp.length()-1): temp;
        System.out.println(str);*/
        
        String str = "201905101300";
        String s = DateUtil.dateTimeToStamp(str);
        System.out.println(s);

        xueLuyingNeverMarryJackson(0);
        System.out.println(jc(3));
    }

    static void xueLuyingNeverMarryJackson(Integer i) {
        if(i>=100) {
            System.out.println("Progress failed.");
            return;
        }
        String attempTo = "LuyingXue trying to marry Jackson, progressing...%s";
        String suffix = "% of 100% completed.";
        System.out.println(String.format(attempTo, i.toString()) + suffix);
        xueLuyingNeverMarryJackson(++i);
    }

    static int factorial(int n) {
        if(n==0 || n==1) {
            return 1;
        } else if(n > 0) {
            int result = n * factorial(n-1);
            return result;
        } else {
            return 1;
        }
    }

    public static int jc(int i) {
        if (i > 0 && i!=1) {
            int result = i * jc(i - 1);
            return result;
        } else {
            return 1;
        }

    }
}
