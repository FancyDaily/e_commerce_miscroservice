package com.e_commerce.miscroservice.commons.helper.util.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    
    /**
     * 根据时间获取对应的日期
     * @param time
     * @return
     */
    public static String getDateByLongTime(long time) {
		if(time <= 0 ){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(time));
	}
    
    /**
     * 根据日期获取对应的时间
     * @param
     * @return
     */
    public static long getLongTimeByDate(String date) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return simpleDateFormat.parse(date).getTime();
		} catch (ParseException e) {
			log.error("DateUtil根据日期获取对应的时间:"+e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}
    /**
     * 获取指定日期开始时间
     * @param timeInt
     * @return
     * @throws ParseException 
     */
    public static long getStartTime(int timeInt ) throws ParseException{
		 String timeString  = timeInt+"";
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(sdf.parse(timeString));
		 calendar.set(Calendar.HOUR,0);
		 calendar.set(Calendar.MINUTE,0);
		 calendar.set(Calendar.SECOND,0);
		 calendar.set(Calendar.MILLISECOND,0);
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		 String dateString = formatter.format(calendar.getTime());
		 long statrtTimleLong = Long.parseLong(dateString);
		 return  statrtTimleLong;
    }
    /**
     * 根据int获取指定日期结束时间
     * @param timeInt
     * @return
     * @throws ParseException 
     */
    public static long getEndTime(int timeInt) throws ParseException{
    	 String timeString  = timeInt+"";
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(sdf.parse(timeString));
		 calendar.set(Calendar.HOUR,23);
		 calendar.set(Calendar.MINUTE,59);
		 calendar.set(Calendar.SECOND,59);
		 calendar.set(Calendar.MILLISECOND,999);
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		 String dateString = formatter.format(calendar.getTime());
		 long endTimeLong = Long.parseLong(dateString);
    	 return endTimeLong;
    }
    /**
     * 根据long获取指定日期结束时间
     * @param longTime
     * @return
     * @throws ParseException 
     */
    public static long getLongEndTime(long longTime) throws ParseException{
    	 
    	 Date date=new Date(longTime);
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		 calendar.set(Calendar.HOUR,23);
		 calendar.set(Calendar.MINUTE,59);
		 calendar.set(Calendar.SECOND,59);
		 calendar.set(Calendar.MILLISECOND,999);
    	 return calendar.getTimeInMillis();
    }
    
    /**
     * 获取明天日期int值
     * @return
     */
    public static int getTomorrow(){
		 Date date=new Date();
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		//把日期往前增加一天
		 calendar.add(calendar.DATE,1);
		 date=calendar.getTime(); 
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		 String dateString = formatter.format(date);
		 Integer tomorrowDateInt = Integer.valueOf(dateString);
		 return tomorrowDateInt;
    }
    /**
     * 获取昨天日期int值
     * @return
     */
    public static int getyesterday(){
    	 Date date=new Date();
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		//把日期减少一天
		 calendar.add(calendar.DATE,-1);
		 date=calendar.getTime(); 
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		 String dateString = formatter.format(date);
		 Integer yesterdayDateInt = Integer.valueOf(dateString);
		 return yesterdayDateInt;
    }
    
    /**
     * 获取十天之前int值
     * @return
     */
    public static int getTenDaysAgoDate(){
    	 Date date=new Date();
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		//把日期减少一天
		 calendar.add(calendar.DATE,-10);
		 date=calendar.getTime(); 
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		 String dateString = formatter.format(date);
		 Integer tenDaysAgoDateInt = Integer.valueOf(dateString);
		 return tenDaysAgoDateInt;
    }
    
    /**
     * 获取今天日期int值
     * @return
     */
    public static int getToday(){
    	Date date=new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		date=calendar.getTime(); 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		Integer todayDateInt = Integer.valueOf(dateString);
    	return todayDateInt;
    }
    
    /**
     * 获取时间戳日期int值
     * time 时间戳
     * @return 格式：20170809
     */
    public static int getDateInt(long time){
    	if(time == 0){
    		return 0;
    	}else{
	    	Date date=new Date(time);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			date=calendar.getTime(); 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String dateString = formatter.format(date);
			Integer todayDateInt = Integer.valueOf(dateString);
	    	return todayDateInt;
    	}
    }

   

    public static int getDayOfMonth(Date date) {
        return getDayOfMonth(date, null, 0);
    }

    public static int getDayOfMonth(Date date, Integer field, int offset) {
        return getCalendar(date, field, offset).get(Calendar.DAY_OF_MONTH);
    }

    public static Date getDate(Date date, Integer field, int offset) {
        return getCalendar(date, field, offset).getTime();
    }

    public static Calendar getCalendar(Date date, Integer field, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (offset != 0) {
            calendar.add(field, offset);
        }
        return calendar;
    }

    public static Date parseDate(String stringDate, String pattern, Date defaultValue) {
        try {
            if (StringUtils.isNotBlank(stringDate)) {
                return new SimpleDateFormat(pattern).parse(stringDate);
            }
        } catch (ParseException e) {
            log.error("parseDate", e);
        }
        return defaultValue;
    }

    public static Date getMonday(int weekOffset) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -((5 + c.get(Calendar.DAY_OF_WEEK)) % 7) + 7 * weekOffset);
        return DateUtils.truncate(c.getTime(), Calendar.DAY_OF_MONTH);
    }

    public static Date parse(String text, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(text);
    }

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String format(long time, String pattern) {
        return format(new Date(time), pattern);
    }
    
    public static long getDayZeroTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static String formatSimpleDate(long pastTime) {
        Calendar now = Calendar.getInstance();
        int yearCurrent = now.get(Calendar.YEAR);
        long timeCurrent = now.getTimeInMillis();
        long zeroTimeCurrent = getDayZeroTime(timeCurrent);

        Calendar past = Calendar.getInstance();
        past.setTimeInMillis(pastTime);
        int year = past.get(Calendar.YEAR);

        long timeDiff = timeCurrent - pastTime;
        if (timeDiff < DateUtils.MILLIS_PER_SECOND) {
            return "刚刚";
        } else if (timeDiff < DateUtils.MILLIS_PER_MINUTE) {// 一分钟之内
            return timeDiff / DateUtils.MILLIS_PER_SECOND + " 秒前";
        } else if (timeDiff < DateUtils.MILLIS_PER_HOUR) {// 一小时之内
            return timeDiff / DateUtils.MILLIS_PER_MINUTE + " 分钟前";
        }

        if (pastTime >= zeroTimeCurrent) {
            return new SimpleDateFormat("今天  HH:mm").format(new Date(pastTime));
        } else if (pastTime >= zeroTimeCurrent - DateUtils.MILLIS_PER_DAY) {
            return new SimpleDateFormat("昨天  HH:mm").format(new Date(pastTime));
        }

        if (yearCurrent - year <= 0) {
            int month = past.get(Calendar.MONTH) + 1;
            int day = past.get(Calendar.DAY_OF_MONTH);
            return month + "月" + day + "日  " + new SimpleDateFormat("HH:mm").format(new Date(pastTime));
        }

        String format = "yyyy-MM-dd";
        return new SimpleDateFormat(format).format(new Date(pastTime));
    }
    
    public static long getTodayStart(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
    
    public static long getTodayEnd(){
        return getTodayStart() + DateUtils.MILLIS_PER_DAY;
    }
    public static long getyesTodayStart(){
        return getTodayStart() - DateUtils.MILLIS_PER_DAY;
    }
    
    public static long parseStrTime2Long(String time) throws ParseException {
        SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return secFormatter.parse(time).getTime();
    }
    public static long parseStrTime2Long2(String time) throws ParseException {
        SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd");
        secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return secFormatter.parse(time).getTime();
    }
    
    public static String parseLongTime2Str(long time) {
		if(time <= 0 ){
			return "";
		}
		SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return secFormatter.format(new Date(time));
	}
    public static String parseLongTime2Str3(long time) {
		if(time <= 0 ){
			return "";
		}
		SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return secFormatter.format(new Date(time));
	}
	
	public static String parseLongTime2Str2(long time) {
		SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd");
		secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return secFormatter.format(new Date(time));
	}
    
    public static Date getWeekStart() {
    	Calendar cal = Calendar.getInstance();
    	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	return cal.getTime();
    }
    
    public static Calendar getStartMounth(){  
    	Calendar calendar = new GregorianCalendar();
    	calendar.set(Calendar.HOUR_OF_DAY,0);  
    	calendar.set(Calendar.MINUTE, 0);    
    	calendar.set(Calendar.SECOND, 0);    
    	calendar.set(Calendar.MILLISECOND, 0);    
    	
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));   
        return calendar;  
    } 
    
	public static long convertToMSEL(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.equals(time, "")){return 0;}

		else{return  sdf.parse(time).getTime();}

	}
	/**
	 * 获取交易编号:时间戳+5位随机数
	 * @return
	 */
	public static  String getRandomNumber() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
        Date date = new Date();  
        String str = simpleDateFormat.format(date);  
		Random random = new Random();
		int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
		return str+rannum;
	}
	
	
	/**
	 * 获取上个月的第一天
	 */
	public static long getBeforeFirstMonthdate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return Long.valueOf(format.format(calendar.getTime()));
	}
	/**
	 * 
	 *  获取上个月的最后一天
	 */
	public static long getBeforeLastMonthdate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		Calendar calendar=Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return Long.valueOf(format.format(calendar.getTime()));
	}

	/**
	 * 获取时间戳
	 * @param date
	 * @return
	 */
	public static long getTime(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1=null;
		try {
			date1 = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date1.getTime();
	}


	/**
	 *计算年月日  获取时间戳
	 * @param yearTo yearTo
	 * @param monthTo monthTo
	 * @param dayTo dayTo
	 * @return long
	 * @author hyf
	 * @date 2018/8/17 14:55
	 */
	public static long afterTime(int yearTo,int monthTo,int dayTo){

		Calendar calendar = Calendar.getInstance();
		int year = NumberUtils.adds(0,calendar.get(Calendar.YEAR),yearTo).intValue();
		int month = NumberUtils.adds(0,calendar.get(Calendar.MONTH),monthTo).intValue();
		int day = NumberUtils.adds(0,calendar.get(Calendar.DATE),dayTo).intValue();
		calendar.set(Calendar.YEAR,year);
		calendar.set(Calendar.MONTH,month);
		calendar.set(Calendar.DATE,day);
		return calendar.getTime().getTime();
	}


    public static void main(String[] args) throws ParseException {
    	long time = 1501732226246L;
    	System.out.println(getDateInt(time));
    	String attachment = getRandomTime();
    	final StringBuffer stringBuffer = new StringBuffer("_");
    	stringBuffer.append(attachment);
    	
    	System.out.println(attachment);
    	String orderNo = "50001";
    	String out_trade_no ="S"+orderNo+stringBuffer.toString();
    	System.out.println(out_trade_no);
    	
    	int startIndex = out_trade_no.indexOf("S")+1;
    	int endIndex = out_trade_no.indexOf("_");
    	System.out.println(out_trade_no.substring(endIndex));
    	
    	System.out.println("startIndex:"+startIndex+",endIndex:"+endIndex);
        orderNo = out_trade_no.substring(startIndex,endIndex);
    	System.out.println(orderNo);
    	
//    	System.out.println(DoubleUtil.sub(15.06, 12.01));
//    	int getyesterday = getyesterday();
//    	int tomorrow = getTomorrow();
//    	int today = getToday();
//    	System.out.println( Calendar.getInstance().getTimeInMillis());
//    	System.out.println(getEndTime(20171116));
//    	System.out.println(getDayZeroTime(time));
//    	String timeStr = parseLongTime2Str(time);
//        System.out.println("昨天"+getyesterday);
//        System.out.println("今天"+today);
//        System.out.println("明天"+tomorrow);
//        DateTime begin = new DateTime("2012-02-01");  
//        DateTime end = new DateTime("2012-05-01");  
//          
//        //计算区间毫秒数  
//        Duration d = new Duration(begin, end);  
//        long time = d.getMillis();  
//          
//        //计算区间天数  
//        Period p = new Period(begin, end, PeriodType.seconds());  
//        int seconds = p.getSeconds();
    	
//    	SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
//    	Calendar calendar=Calendar.getInstance();
//    	calendar.add(Calendar.MONTH, -1);
//    	calendar.set(Calendar.DAY_OF_MONTH, 1);
//    	System.out.println(Long.valueOf(format.format(calendar.getTime())));
//    	System.out.println(getLongEndTime( parseStrTime2Long2("2017-11-17")));
    }
    
    public static int addDay(int days){
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DATE, days);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	int date = Integer.parseInt(sdf.format(calendar.getTime()));
    	return date;
    }
    /**
     * 转换int类型日期格式
     * @param reportDate 格式：20170809
     * @return 格式：2017-08-09
     */
	public static String buildDateToStr(int reportDate) {
		Date dateOld = new Date();
		try {
			SimpleDateFormat sdfOld = new SimpleDateFormat("yyyyMMdd");
			dateOld = sdfOld.parse(reportDate+"");
		} catch (Exception e) {
			log.error("DateUtil转换int类型日期格式:"+e.getMessage());
			e.printStackTrace();
			return "";
		}
		SimpleDateFormat sdfNew = new SimpleDateFormat("yyyy-MM-dd");
    	String date = sdfNew.format(dateOld.getTime());
		return date;
	}
	
    /**
     * 转换int类型日期格式
     * @param reportDate 格式：20170809
     * @return 格式：2017-08-09
     */
	public static int strToDateInt(String reportDate) {
		Date dateOld = new Date();
		try {
			SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd");
			dateOld = sdfOld.parse(reportDate+"");
		} catch (Exception e) {
			log.error("DateUtil转换int类型日期格式:"+e.getMessage());
			e.printStackTrace();
			return 0;
		}
		SimpleDateFormat sdfNew = new SimpleDateFormat("yyyyMMdd");
    	int date = Integer.valueOf(sdfNew.format(dateOld.getTime()));
		return date;
	}

	/** 
	 *  
	 * @param
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式 
	 * @author fy.zhang 
	 */  
	public static String formatDuring(long mss) {  
	    long days = mss / (1000 * 60 * 60 * 24);  
	    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
	    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);  
	    long seconds = (mss % (1000 * 60)) / 1000;  
	    return days + "天" + hours + "小时" + minutes + "分"  
	            + seconds + "秒";  
	}  
	
	/**
	 * 获取当前时间的日期，时，分，秒
	 * @return
	 */
	public static String getRandomTime(){
		String time = "";
		SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
		time = sdf.format(new Date());
		return time;
	}
	
}