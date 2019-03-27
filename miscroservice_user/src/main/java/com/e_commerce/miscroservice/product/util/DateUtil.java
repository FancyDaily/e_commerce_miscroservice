package com.e_commerce.miscroservice.product.util;

import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
		return commonParse(dateTime, "yyyyMMddHHmm");
	}
	/**
	 * 将字符串日期格式转换为毫秒值
	 * @param dateTime 字符串时间日期
	 * @return
	 */
	public static Long parseDate(String dateTime) {
		return commonParse(dateTime, "yyyyMMdd");
	}

	/**
	 * 公共解析 日期或时间 需要传递解析格式
	 * @param dateTime 字符串时间日期
	 * @param reg 时间日期的格式
	 * @return
	 */
	public static Long commonParse(String dateTime, String reg) {
		SimpleDateFormat sdf = new SimpleDateFormat(reg);
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
		return commonFormat(timestamp, "yyyyMMddHHmm");
	}
	/**
	 * 将毫秒值转换为字符串时间日期
	 * @param timestamp 时间戳
	 * @return
	 */
	public static String formatShow(Long timestamp) {
		return commonFormat(timestamp, "yyyy-MM-dd HH:mm");
	}
	/**
	 * 将毫秒值转换为字符串日期
	 * @param timestamp 时间戳
	 * @return
	 */
	public static String formatDate(Long timestamp) {
		return commonFormat(timestamp, "yyyyMMdd");
	}

	/**
	 * 通用format 时间戳转字符串格式
	 * @param timestamp  要格式的时间戳
	 * @param reg 格式化的规则
	 * @return
	 */
	public static String commonFormat(Long timestamp, String reg) {
		SimpleDateFormat sdf = new SimpleDateFormat(reg);
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
	 * 获取指定周期的下一个周  1、周一  7、周日 不包含weekDay
	 * @param weekDayNumberArray  指定周期的数组
	 * @param weekDay 指定的周
	 * @return
	 */
	public static int getNextWeekDay(int[] weekDayNumberArray, int weekDay) {
		for (int i = 0; i < weekDayNumberArray.length; i++) {
			if (weekDayNumberArray[i] > weekDay) {
				return weekDayNumberArray[i];
			}
		}
		return weekDayNumberArray[0];
	}
	/**
	 * 获取指定周期最近的一个周X 可能包含weekDay  1、周一  7、周日
	 * @param weekDayNumberArray  指定周期的数组
	 * @param weekDay 指定的周
	 * @return
	 */
	public static int getMostNearWeekDay(int[] weekDayNumberArray, int weekDay) {
		for (int i = 0; i < weekDayNumberArray.length; i++) {
			if (weekDayNumberArray[i] >= weekDay) {
				return weekDayNumberArray[i];
			}
		}
		return weekDayNumberArray[0];
	}


	/**
	 * 获取当前日期的周几
	 * @param date 开始时间字符串 yyyyMMddHHmm
	 * @return 周一为1  周日为7
	 */
	public static Integer getWeekDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			Date parse = sdf.parse(date);
			return getWeekDay(parse.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new MessageException("日期时间格式错误");
		}

	}

	/**
	 * 获取当前日期的周几
	 * @param startTime 开始时间毫秒值
	 * @return 周一为1  周日为7
	 */
	public static int getWeekDay(Long startTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startTime);
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

	/**
	 * 获取时间戳的日期  年月日
	 * @param timestamp 时间戳
	 * @return  yyyyMMdd
	 */
	public static String getDate(Long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(timestamp);
		return date;
	}

	/**
	 * 给指定毫秒值加指定的天数
	 * @param timestamp 指定的毫秒值
	 * @param addDays 指定的天数
	 * @return 增加后的毫秒值
	 */
	public static Long addDays(Long timestamp, int addDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		cal.add(Calendar.DAY_OF_YEAR, addDays);
		return cal.getTimeInMillis();
	}
	/**
	 * 给指定毫秒值加指定的小时
	 * @param timestamp 指定的毫秒值
	 * @param addHours 指定的小时
	 * @return 增加后的毫秒值
	 */
	public static Long addHours(Long timestamp, int addHours) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		cal.add(Calendar.HOUR, addHours);
		return cal.getTimeInMillis();
	}

	public static int[] getWeekDayArray(String dateWeekNumber) {
		String[] weekDayArray = dateWeekNumber.split(",");
		int[] WeekDayNumberArray = getIntArray(weekDayArray);
		//对星期进行升序排序
		Arrays.sort(WeekDayNumberArray);
		return WeekDayNumberArray;
	}

	/**
	 * 将字符串数组转换为int数组
	 *
	 * @param weekDayArray 字符串数值数组
	 * @return int数组
	 * @author 马晓晨
	 */
	private static int[] getIntArray(String[] weekDayArray) {
		int[] WeekDayNumberArray = new int[weekDayArray.length];
		for (int i = 0; i < weekDayArray.length; i++) {
			Integer weekDay = Integer.parseInt(weekDayArray[i]);
			WeekDayNumberArray[i] = weekDay;
		}
		return WeekDayNumberArray;
	}

	/**
	 * 获取下一张订单的开始时间和结束时间
	 * @param productStartTime 开始时间
	 * @param productEndTime 结束时间
	 * @param weekDayNumberArray 星期数组
	 * @param contains 是否包含当前时间
	 * @return
	 */
	public static DateResult getNextOrderBeginAndEndTime(Long productStartTime, Long productEndTime,int[] weekDayNumberArray, boolean contains) {
		DateResult result = new DateResult();
		//获取开始的时间是星期X
		int startWeekDay = DateUtil.getWeekDay(productStartTime);
		//获取订单开始的时间是星期X  离商品开始星期X最近的星期Y
		int orderWeekDay = 0;
		if (contains) {
			orderWeekDay = DateUtil.getMostNearWeekDay(weekDayNumberArray, startWeekDay);
		} else {
			orderWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, startWeekDay);
		}
		int addDays = (orderWeekDay + 7 - startWeekDay) % 7;
//			//订单开始的时间戳
		Long startTimeMill = DateUtil.addDays(productStartTime, addDays);
		Long endTimeMill = DateUtil.addDays(productEndTime, addDays);
		//参数星期的下一个星期X(不包含这个参数星期)
//			int orderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, orderWeekDay);
		//如果重复中包含当天的订单，查看结束时间是否小于当前时间，小于当前时间就是已经过了今天的，直接发下一个星期X的
		int orderNextWeekDay;
		while (true) {
			if (endTimeMill > System.currentTimeMillis()) {
				break;
			}
			orderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, orderWeekDay);

			addDays = (orderNextWeekDay + 7 - orderWeekDay) % 7;
			if (addDays == 0) {
				addDays = 7;
			}
			startTimeMill = DateUtil.addDays(startTimeMill, addDays);
			endTimeMill = DateUtil.addDays(endTimeMill, addDays);
			orderWeekDay = orderNextWeekDay;
		}
		result.setDays(addDays);
		result.setStartTimeMill(startTimeMill);
		result.setEndTimeMill(endTimeMill);
		return result;
	}

	/**
	 * 根据时间格式"yyyyMMddHHmmss"字符串生成cron
	 * @param date
	 * @return
	 */
	public static String genCron(String date) {
		String year = date.substring(0, 4);
		//月
		String month = date.substring(4, 6);
		//日
		String day = date.substring(6,8);
		//时
		String hour = date.substring(8, 10);
		//分
		String minute = date.substring(10, 12);
		//秒

		String second = null;
		try {
			second = date.substring(12, 14);
		} catch (Exception e) {
			second = "00";
		}

		String corn = second+" "+minute+" "+hour+" "+day+" "+month+" "+"? "+year;
		System.out.println(corn);
		return corn;
	}

	/**
	 * 根据毫秒值获取cron语法
	 * @param timestamp 时间戳
	 * @return
	 */
	public static String genCron(Long timestamp) {
		String stringDateTime = format(timestamp);
		return genCron(stringDateTime);
	}
}
