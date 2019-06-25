package com.e_commerce.miscroservice.guanzhao_proj.product_order;

import com.e_commerce.miscroservice.commons.util.colligate.AliOSSUtil;
import com.e_commerce.miscroservice.commons.util.colligate.UUIDGenerator;
import com.e_commerce.miscroservice.commons.util.colligate.pay.QRCodeUtil;
import com.e_commerce.miscroservice.user.po.TUser;
import org.apache.poi.ss.formula.functions.T;
import redis.clients.jedis.BinaryClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-15 09:18
 */
public class Test {

    public InnerClazz getNewInnerClazz() {
        return new InnerClazz();
    }

    @FunctionalInterface
    interface AFunctional<T> {
        boolean test(T t);
    }

    public static boolean doAfunctional(Object param, AFunctional functional) {
        return functional.test(param);
    }

    public static void donate(int money, Consumer<Integer> consumer) {
        consumer.accept(money);
    }

    public static List<Integer> supply(Integer num, Supplier<Integer> supplier) {
        List<Integer> resultList = new ArrayList<>();
        for(int x=0;x<num; x++)
            resultList.add(supplier.get());
        return resultList;
    }

    public static Integer convert(String str, Function<String, Integer> function) {
        return function.apply(str);
    }

    public static List<String> filter(List<String> fruit, Predicate<String> predicate) {
        List<String> f = new ArrayList<>();
        for(String s: fruit) {
            if(predicate.test(s)) {
                f.add(s);
            }
        }
        return f;
    }

    public static void main(String[] args) {
//        boolean isAdult = doAfunctional(20, x -> (int)x >= 18); //判断参数大于18
//        System.out.println(isAdult);
//        TUser tUser = new TUser();
//        boolean isTuser = doAfunctional(tUser, param -> param instanceof TUser);
//        System.out.println(isTuser);
//        donate(10, money -> System.out.println("他捐赠了" + money + "$"));
//        List<Integer> list = supply(10, () -> (int)(Math.random()*1000));   //装载十个伪随机数
//        list.forEach(System.out::println);
//        Integer theValue = convert("29", x -> Integer.parseInt(x));
//
//        List<String> fruit = Arrays.asList("香蕉", "哈密瓜", "榴莲", "火龙果", "水蜜桃");
//        fruit = filter(fruit, (f) -> f.length() == 2);
//        System.out.println(fruit);
//        List aList = new ArrayList();
        //不使用stream API
        /*Property p1 = new Property("叫了个鸡", 1000, 500, 2);
        Property p2 = new Property("张三丰饺子馆", 2300, 1500, 3);
        Property p3 = new Property("永和大王", 580, 3000, 1);
        Property p4 = new Property("肯德基", 6000, 200, 4);
        List<Property> properties = Arrays.asList(p1, p2, p3, p4);
        Collections.sort(properties, (x, y) -> x.distance.compareTo(y.distance));
        String name = properties.get(0).name;
        System.out.println("距离我最近的店铺:" + name);

        //使用stream API
        String name2 = properties.stream()
                .sorted(Comparator.comparingInt(x -> x.distance))
                .findFirst()
                .get().name;
        System.out.println("距离我最近的店铺:" + name2);

        long count = properties.stream()
                .filter(p -> p.sales > 1000)
                .count();
        System.out.println(count);*/

       /* String path = "/Users/xufangyi/Downloads";
        File[] files = new File(path).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isHidden();
            }
        });
        if(files!=null) {
            for(File file:files) {
                System.out.println(file.getName());
            }
        }

        System.out.println("--------------");
        File[] listFiles = new File(path).listFiles(File::isHidden);
        if(files!=null) {
            for(File file:files) {
                System.out.println(file.getName());
            }
        }
        System.out.println("--------------");
        Arrays.stream(listFiles).forEach(a->System.out.println(a.getName()));*/

		boolean odd = isOdd(2);
		boolean odd1 = isOdd(1);
		System.out.println(odd);
		System.out.println(odd1);

		System.out.println("3e52088bc0d40d8fa1f602467453269c".length());

	}

    public static boolean isOdd(int i) {
    	return (i & 1) == 1;
	}

   /* public static void main(String[] args) {
        *//*Test test = new Test();
        InnerClazz newInnerClazz = test.getNewInnerClazz();
        Map<String, Object> cache = InnerStaticClazz.cache;
        String myKey = "DonaldTrumpIsADick";
        cache.put(myKey, true);
        test = new Test();
        cache = InnerStaticClazz.cache;
        Object o = cache.get(myKey);
        System.out.println(o);*//*
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
    }*/

    public static class InnerStaticClazz {
        private static Map<String, Object> cache = new HashMap<>();
    }

    public class InnerClazz {

    }

}
