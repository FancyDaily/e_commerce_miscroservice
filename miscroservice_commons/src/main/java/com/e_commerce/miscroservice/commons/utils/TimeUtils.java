package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 15:15
 * @Copyright 玖远网络
 */
public class TimeUtils{

    private static Log logger = Log.getInstance (TimeUtils.class);

    public static final String yMdHms = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD = "yyyy-MM-dd";

    public static SimpleDateFormat sdf() {
        return new SimpleDateFormat (yMdHms);
    }

    public static SimpleDateFormat ymd() {
        return new SimpleDateFormat (YMD);
    }

    public static String cal2Str(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        return sdf ().format (calendar.getTime ());
    }

    public static String stamp2Str(Timestamp createTime) {
        if (createTime == null) {
            return "";
        }
        return sdf ().format (createTime);
    }

    /**
     * 获取当前时间的秒数字符串
     *
     * @return
     */
    public static String getNowTimeStr() {
        String str = Long.toString (System.currentTimeMillis () / 1000);
        return str;
    }

    /**
     * 获取日期
     *
     * @param a
     * @param day
     * @return
     * @throws ParseException
     */
    public static String dateAndDay(String a, String day) throws ParseException {

        Date d = getDate (a);
        d.setDate (d.getDate () + Integer.parseInt (day));

        String sdate = ymd ().format (d);

        return sdate;
    }


    /**
     * 计算年月日 若 只计算其中一个 其余 设为0即可
     *
     * @param d
     * @param day
     * @return
     */
    public static Date rollDate(Date d, int year, int mon, int day) {
        Calendar cal = Calendar.getInstance ();
        cal.setTime (d);
        cal.add (Calendar.YEAR, year);
        cal.add (Calendar.MONTH, mon);
        cal.add (Calendar.DAY_OF_MONTH, day);
        return cal.getTime ();
    }

    /**
     * 年月日格式
     *
     * @param date
     * @return
     */
    public static String dateFormatString(Date date) {
        String sdate = ymd ().format (date);
        return sdate;
    }

    /**
     * 年月日 时分秒格式
     *
     * @param date
     * @return
     */
    public static String dateFormatString2(Date date) {
        String sdate = sdf ().format (date);
        return sdate;
    }

    /**
     * 时间戳 转换 年月日 时分秒格式
     *
     * @param time
     * @return
     */
    public static String longFormatString(Long time) {
        if (time == null || time == 0) {
            return "";
        }
        Date date = new Date (time);
        String sdate = sdf ().format (date);
        return sdate;
    }

    /**
     * 日期转换成秒
     *
     * @param date
     * @return
     */
    public static String dateFormatSecond(Date date) {
        String str = Long.toString (date.getTime () / 1000);
        return str;
    }

    /**
     * @return
     */
    public static long genTimeStamp() {
        return System.currentTimeMillis () / 1000;
    }

    /**
     * 将秒 转换为 字符串 “yyyy-MM-dd” 格式
     *
     * @param times
     * @return
     */
    public static String secondToStringDate(String times) {
        Date da = getDate (times);
        return dateFormatString (da);
    }

    /**
     * 判断两个日期是否在同一个月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean dateEqMonth(String date1, String date2) {
        try {
            Date dt1 = ymd ().parse (date1);
            Date dt2 = ymd ().parse (date2);
            Calendar calendar1 = Calendar.getInstance ();
            calendar1.setTime (dt1);
            Calendar calendar2 = Calendar.getInstance ();
            calendar2.setTime (dt2);
            return calendar1.get (Calendar.YEAR) == calendar2.get (Calendar.YEAR) && calendar1.get (Calendar.MONTH) == calendar2.get (Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        return false;
    }

    /**
     * 获取年 或 月 或 日
     *
     * @param date
     * @param type 1： 获取 年   2： 获取 月  5 ：获取 日
     * @return
     */
    public static Integer getYearOrMonthOrDay(Date date, Integer type) {
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime (date);
        if (type == Calendar.YEAR) {
            return calendar.get (Calendar.YEAR);
        }
        if (type == Calendar.DAY_OF_MONTH) {
            return calendar.get (Calendar.DAY_OF_MONTH);
        }
        if (type == Calendar.MONTH) {
            return calendar.get (Calendar.MONTH) + 1;
        }
        return 0;
    }



    /**
     * 将秒转换成时间
     *
     * @param times
     * @return
     */
    public static Date getDate(String times) {
        long time = Long.parseLong (times);
        return new Date (time * 1000);
    }

    /**
     * 日期转换成时间戳("yyyy-MM-dd")
     *
     * @param time time
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/11/8 18:01
     */
    public static Long ymdString2Long(String time) {
        if (StringUtils.isBlank (time)) {
            return null;
        }

        try {
            Date date = ymd ().parse (time);
            return date.getTime ();
        } catch (ParseException e) {
            //ignore
            return null;
        }
    }


    /**
     * 优先使用 yyyy-MM-dd HH:mm:ss转换,失败则再次尝试 yyyy-MM-dd 日期转换
     *
     * @param ymdOrYmdHms ymdOrYmdHms
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/11/16 10:16
     */
    public static Long str2Long(String ymdOrYmdHms) {
        if (StringUtils.isBlank (ymdOrYmdHms)) {
            return null;
        }

        Date date;
        try {
            date = sdf ().parse (ymdOrYmdHms);
        } catch (ParseException e) {
            logger.info ("尝试 yyyy-MM-dd 进行日期转换 ymdOrYmdHms={}", ymdOrYmdHms);
            return ymdString2Long (ymdOrYmdHms);
        }

        return date.getTime ();
    }


    /**
     * 今天的最后时间
     *
     * @return java.util.Calendar
     * @author Charlie
     * @date 2018/11/21 21:02
     */
    public static Calendar endOfToday() {
        Calendar today = Calendar.getInstance ();
        today.set (Calendar.HOUR_OF_DAY, 23);
        today.set (Calendar.MINUTE, 59);
        today.set (Calendar.SECOND, 59);
        return today;
    }


    /**
     * 今天的最后时间
     *
     * @return java.util.Calendar
     * @author Charlie
     * @date 2018/11/21 21:02
     */
    public static Calendar startOfToday() {
        Calendar today = Calendar.getInstance ();
        today.set (Calendar.HOUR_OF_DAY, 0);
        today.set (Calendar.MINUTE, 0);
        today.set (Calendar.SECOND, 0);
        return today;
    }

    /**
     * 今天的最后时间
     *
     * @return java.util.Calendar
     * @author Charlie
     * @date 2018/11/21 21:02
     */
    public static Calendar endOfDay(Date date) {
        Calendar today = Calendar.getInstance ();
        today.setTime (date);
        today.set (Calendar.HOUR_OF_DAY, 23);
        today.set (Calendar.MINUTE, 59);
        today.set (Calendar.SECOND, 59);
        return today;
    }


}
