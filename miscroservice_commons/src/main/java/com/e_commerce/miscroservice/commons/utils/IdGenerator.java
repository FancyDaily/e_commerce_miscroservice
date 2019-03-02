package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 描述
 * @author hyq
 * @date 2018/9/25 10:36
 * @return
 */
public class IdGenerator {
	
	public static String randomUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getIgName() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//long millis = System.nanoTime();
		//加上三位随机数
		Random random = new Random();
		int end3 = random.nextInt(999);
		//如果不足三位前面补0
		String str = millis + String.format("%03d", end3);
		
		return str;
	}

	public static String getCurrentTimeId(String prefix){

		Date date = new Date();
		SimpleDateFormat format =  new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String string = format.format(date);
		System.out.println ("string = " + string);
		//加上两位随机数
		Random random = new Random();
		int end4 = random.nextInt(9999);
		String end = String.format ("%04d", end4);
		System.out.println ("end4 = " + end);
		//如果不足三位前面补0
		String str = (StringUtils.isEmpty(prefix)?"":prefix) +string + end;
		
		return str;
	}

	/**
	 * prefix + 时间 + middle + 4位随机数
	 *
	 * @param prefix prefix
	 * @param middle middle
	 * @return java.lang.String
	 * @author Charlie
	 * @date 2018/11/19 10:11
	 */
	public static String getCurrentTimeId(String prefix, Object middle){
		SimpleDateFormat format =  new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dataStr = format.format(new Date());
		//随机数
		Random random = new Random();
		int end4 = random.nextInt(9999);
		String end = String.format ("%04d", end4);

		String middleStr = middle == null ? EmptyEnum.string :middle.toString ();

		//如果不足三位前面补0
		String str = new StringBuilder ().append (StringUtils.isEmpty (prefix) ? EmptyEnum.string : prefix).append (dataStr).append (middleStr).append (end).toString ();
		return str;
	}

	public static void main(String[] args) {
		System.out.println(new Random().nextInt(9999));
		System.out.println(getCurrentTimeId("****", "userId"));
	}
	
}
