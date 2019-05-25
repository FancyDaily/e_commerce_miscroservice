package com.e_commerce.miscroservice.guanzhao_proj.product_order;

import com.e_commerce.miscroservice.commons.util.colligate.AliOSSUtil;
import com.e_commerce.miscroservice.commons.util.colligate.UUIDGenerator;
import com.e_commerce.miscroservice.commons.util.colligate.pay.QRCodeUtil;
import org.apache.poi.ss.formula.functions.T;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-15 09:18
 */
public class Test {

    public InnerClazz getNewInnerClazz() {
        return new InnerClazz();
    }

    public static void main(String[] args) {
        /*Test test = new Test();
        InnerClazz newInnerClazz = test.getNewInnerClazz();
        Map<String, Object> cache = InnerStaticClazz.cache;
        String myKey = "DonaldTrumpIsADick";
        cache.put(myKey, true);
        test = new Test();
        cache = InnerStaticClazz.cache;
        Object o = cache.get(myKey);
        System.out.println(o);*/
        long time = 935809407;
        long day = 24l * 60 * 60 * 1000;
        long dayCnt = time / day;
        time = time - dayCnt * day;
        System.out.println(time);
        long hour = 60l * 60 * 1000;
        long hourCnt = time / hour;
        time = time - hourCnt * hour;
        System.out.println(time);
        long minute = 60l * 1000;
        long minCnt = time / minute;
        time = time - minCnt * minute;
        System.out.println(time);
        System.out.println(dayCnt + "," + hourCnt + "," + minCnt);
    }

    public static class InnerStaticClazz {
        private static Map<String, Object> cache = new HashMap<>();
    }

    public class InnerClazz {

    }

}
