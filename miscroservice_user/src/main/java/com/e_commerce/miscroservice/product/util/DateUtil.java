package com.e_commerce.miscroservice.product.util;

import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 与业务契合的日期工具类
 *
 * @author 马晓晨
 * @date 2019/3/5
 */
public class DateUtil {

	/**
	 * 星期日
	 */
	public static final int Sunday = 7;
	/**
	 * 星期一
	 */
	public static final int Monday = 1;
	/**
	 * 星期二
	 */
	public static final int Tuesday = 2;
	/**
	 * 星期三
	 */
	public static final int Wednesday = 3;
	/**
	 * 星期四
	 */
	public static final int Thursday = 4;
	/**
	 * 星期五
	 */
	public static final int Friday = 5;
	/**
	 * 星期六
	 */
	public static final int Saturday = 6;

	/**
	 * 给定时间段和星期几，计算该时间段内共有多少个给定的星期几
	 *
	 * @param start 开始时间,格式yyyy-MM-dd
	 * @param end   结束时间，格式yyyy-MM-dd
	 * @param weekday     星期几，从星期一到星期天，分别用数字1-7表示
	 * @return 星期几统计数
	 */
	public static long countWeek(String start, String end, int weekday) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		long sunDay = 0;
		//计数
		try {
			Calendar startDate = Calendar.getInstance();
			//开始时间
			startDate.setTime(format.parse(start));
			Calendar endDate = Calendar.getInstance();
			//结束时间
			endDate.setTime(format.parse(end));
			//开始日期是星期几
			int SW = startDate.get(Calendar.DAY_OF_WEEK) - 1;
			//结束日期是星期几
			int EW = endDate.get(Calendar.DAY_OF_WEEK) - 1;
			long diff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
			//给定时间段内一共有多少天
			long days = diff / (1000 * 60 * 60 * 24);
			//给定时间内，共有多少个星期
			long w = Math.round(Math.ceil(((days + SW + (7 - EW)) / 7.0)));
			sunDay = w;
			//总的星期几统计数
			if (weekday < SW)
				sunDay--; //给定的星期几小于起始日期的星期几，需要减少一天
			if (weekday > EW)
				sunDay--; //给定的星期几大于结束日期的星期几，需要减少一天
		} catch (Exception se) {
			se.printStackTrace();
		}
		return sunDay;
	}

	/**
	 * 将字符串日期时间格式转换为毫秒值
	 * @param dateTime 字符串时间日期
	 * @return
	 */
	public static Long parse(String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			Date date = sdf.parse(dateTime);
			sdf = null;
			return date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	/**
	 * 将毫秒值转换为字符串时间日期
	 * @param timestamp 时间戳
	 * @return
	 */
	public static String format(Long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			String format = sdf.format(timestamp);
			sdf = null;
			return format;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取当前日期的周几
	 * @param date
	 * @return 周一为1  周日为7
	 */
	public static Integer getWeekDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		try {
			Date parse = sdf.parse(date);
			cal.setTime(parse);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new MessageException("日期时间格式错误");
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		//根据基姆拉尔森公式计算
		if (month == 1) {
			month = 13;
			year--;
		}
		if (month == 2) {
			month = 14;
			year--;
		}
		int week = (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400) % 7;
		return week + 1;
	}
}
