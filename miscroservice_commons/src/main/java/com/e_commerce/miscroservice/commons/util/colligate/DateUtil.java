package com.e_commerce.miscroservice.commons.util.colligate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import org.apache.commons.collections.bag.SynchronizedSortedBag;


/**
 * 功能描述: 日期工具
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月5日 下午2:27:32
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */

public class DateUtil {

    public static Long interval = (long) (24 * 60 * 60 * 1000);

    /*
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param formatStr
     * @return
     */

    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

	public static String timeStamp2Date(Long timeStamp, String format) {
    	if(timeStamp==null) {
    		return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(timeStamp)));
	}

    /*
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param formatStr
     * @return
     */

    public static String timeStamp2Date(Long timeStamp) {
        String seconds = String.valueOf(timeStamp / 1000);
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /*
     * 时间戳转换成月份字符串
     * @param time
     * @return
     */

    public static String timeStamp2Month(Long time) {
        String date = timeStamp2Date(time);
        String[] split = date.split("-");
        return split[1].toString(); // 初始月数
    }

    /*
     * 将时间转换为时间戳
     */

    public static String dateToStamp(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将精确到分钟的时间转换为时间戳
     */

    public static String dateTimeToStamp(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 功能描述: 将年月字符串转换成时间戳区间
     * 作者: 许方毅
     * 创建时间: 2018年11月7日 上午11:58:55
     * @param ymString eg. -> 2019-01-03
     * @return
     */

    public static Map<String, Object> ym2BetweenStamp(String ymString) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (ymString.endsWith("-")) {
            ymString = ymString.substring(0, ymString.length() - 1);
        }
        String[] split = ymString.split("-");
        String year = split[0].toString();
        String month = split[1].toString();
        String beginDate = new StringBuilder().append(year).append("-").append(month).append("-").append("1")
                .toString();
        String endDate = ym2EndDate(year, month);
        String beginStamp = dateToStamp(beginDate);
        String endStamp = String.valueOf(Long.parseLong(dateToStamp(endDate)) + 82800000 + 3599999);
        resultMap.put("begin", beginStamp);
        resultMap.put("end", endStamp);
        return resultMap;
    }

    /*
     * 功能描述: 将年月转换成最后日期
     * 作者: 许方毅
     * 创建时间: 2018年11月7日 下午2:54:42
     * @param year
     * @param month
     * @return
     */

    public static String ym2EndDate(String year, String month) {
        //处理月份信息
        if (month.startsWith("0")) {
            month = month.substring(1);
        }

        String jan = "1";
        String feb = "2";
        String mar = "3";
        String apr = "4";
        String may = "5";
        String jae = "6";
        String jul = "7";
        String aug = "8";
        String sep = "9";
        String oct = "10";
        String nov = "11";
        String dec = "12";

        List<String> day31List = new ArrayList<String>();
        day31List.add(jan);
        day31List.add(mar);
        day31List.add(may);
        day31List.add(jul);
        day31List.add(aug);
        day31List.add(oct);
        day31List.add(dec);

        List<String> day30List = new ArrayList<String>();
        day30List.add(apr);
        day30List.add(jae);
        day30List.add(sep);
        day30List.add(nov);

        String tail = "";

        // 月份判断
        if (day31List.contains(month)) { // 31天
            tail = "31";
        }

        if (day30List.contains(month)) { // 30天
            tail = "30";
        }

        if (feb.equals(month)) {
            // 闰年判断
            int yearInt = Integer.parseInt(year);
            if ((yearInt % 4 == 0 && yearInt % 100 != 0) || yearInt % 400 == 0) { // 29天
                tail = "29";
            }
            // 28天
            tail = "28";
        }

        // 构建结果
        return new StringBuilder().append(year).append("-").append(month).append("-").append(tail).toString();

    }

    /*
     * 功能描述: 年份返回起止时间戳
     * 作者: 许方毅
     * 创建时间: 2018年11月10日 下午3:08:17
     * @param year
     * @return
     */

    public static Map<String, Object> y2BetweenStamp(Integer year) {
        Map<String, Object> map = new HashMap<String, Object>();
        String startDate = year.toString() + "-1" + "-1";
        String endDate = year.toString() + "-12" + "-31";
        Long startTimeStamp = Long.valueOf(dateToStamp(startDate));
        Long endTimeStamp = Long.valueOf(dateToStamp(endDate));
        map.put("betLeft", startTimeStamp);
        map.put("betRight", endTimeStamp);
        return map;
    }

    /*
     * 功能描述: 比较最后签到日为昨天、今天、其他日期	->
     * 作者: 许方毅
     * 创建时间: 2018年11月24日 下午1:50:24
     * @param paramStamp
     * @return
     */

    public static String curtMillesVsYesMilles(Long paramStamp) {
        String status = null;
        Long thisStamp = System.currentTimeMillis(); // 当前时间戳
        String date = timeStamp2Date(thisStamp); // 当前日期00:00:00 时刻
        Long timeStamp = Long.valueOf(dateToStamp(date)); // 今日00:00:00 对应时间戳
        Long yesStamp = timeStamp - interval; // 昨日00:00:00 对应时间戳
        Long toEndStamp = timeStamp + interval; // 明日00:00:00 对应时间戳
        // 昨天
        if (paramStamp > yesStamp && paramStamp < timeStamp) {
            status = "0";
        }
        // 今天
        else if (paramStamp > timeStamp && paramStamp < toEndStamp) {
            status = "1";
        }
        // 其他日期(断签)
        else {
            status = "-1";
        }
        return status;
    }

    /*
     * 功能描述: 判读两个时间戳是否为连续的日期（支持跨年、跨月）
     * 作者: 许方毅
     * 创建时间: 2018年11月24日 下午1:51:24
     * @param paramLeft 相对较晚的时间戳
     * @param paramRight 相对较早的时间戳
     * @return
     */

    public static boolean oneMillesVsAnother(Long paramLeft, Long paramRight) {
        boolean result = false;
        Long leftStartStamp = Long.valueOf(dateToStamp(timeStamp2Date(paramLeft)));// 对应当日00:00:00 时间戳
        Long rightStartStamp = Long.valueOf(dateToStamp(timeStamp2Date(paramRight)));// 对应当日00:00:00 时间戳
        if ((leftStartStamp - rightStartStamp) <= interval) {
            result = true;
        }
        return result;
    }

    /*
     * 功能描述: 判断时间戳是否为今日
     * 作者: 许方毅
     * 创建时间: 2018年11月13日 下午6:56:04
     * @param updateTime
     * @return
     */

    public static boolean isToday(Long updateTime) {
        boolean result = false;
        if (timeStamp2Date(updateTime).equals(timeStamp2Date(System.currentTimeMillis()))) {
            result = true;
        }
        return result;
    }

    /*
     * 功能描述: 获取当前的日期
     * 作者: 许方毅
     * 创建时间: 2018年11月24日 下午3:17:33
     * @return
     */

    public static String getThisYmString() {
        long currentTimeMillis = System.currentTimeMillis();
        return timeStamp2Date(currentTimeMillis);
    }

    /*
     * 功能描述: 根据当前日期在七日连签中的位置返回反正签到日期list
     * 作者: 许方毅
     * 创建时间: 2018年11月25日 下午5:02:30
     * @param position
     * @return
     * @throws Exception
     */

    public static String[] getDateListWithinSeven(Integer position) {
        // 请求参数判断
        if (position > 7 || position < 0) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "错误的标志位！");
        }

        // 获取时间戳、当日日期
        long currentTimeMillis = System.currentTimeMillis();
        String today = timeStamp2Date(currentTimeMillis); // 当日日期
        Long todayStamp = Long.valueOf(dateToStamp(timeStamp2Date(currentTimeMillis))); // 当日00:00:00 时间戳
        // 初始化数组
        String[] strs = new String[7];
        // 数字化标志位
        int positionInt = 1;
        if (position != null) {
            positionInt = position;
        }
        int index = positionInt - 1;
        strs[index] = today;

        for (int i = 0; i < strs.length; i++) {
            if (i == index) {
                strs[i] = "今日";
                continue;
            }
            int num = i - index;
            long thisInterval = num * interval;
            long thisStamp = todayStamp + thisInterval;
            String thisDate = timeStamp2Date(thisStamp);
            strs[i] = thisDate;
        }
        return strs;
    }

    /*
     * 功能描述: 获取当日伊始时间戳
     * 作者: 许方毅
     * 创建时间: 2018年11月25日 下午6:28:57
     * @param timeStamp
     */

    public static long getStartStamp(long timeStamp) {
        return Long.valueOf(dateToStamp(timeStamp2Date(timeStamp)));
    }

    /*
     * 功能描述: 获取昨天伊始时间戳
     * 作者: 许方毅
     * 创建时间: 2018年11月25日 下午6:27:28
     * @return
     */

    public static long getYesStartStamp() {
        long currentTimeMillis = System.currentTimeMillis();
        return getStartStamp(currentTimeMillis) - interval;
    }

    /*
     * 功能描述: 获取明日伊始时间戳
     * 作者: 许方毅
     * 创建时间: 2019年1月14日 下午8:36:38
     * @return
     */

    public static long getEndStamp(long timeStamp) {
        return getStartStamp(timeStamp) + interval - 1;
    }

    /*
     * 功能描述: 根据时间戳返回当月起止时间Map
     * 作者: 许方毅
     * 创建时间: 2019年1月14日 下午3:11:50
     * @param timeMillis
     * @return
     */

    public static Map<String, Object> getMonthBetween(long timeMillis) {
        String timeStamp2Date = timeStamp2Date(timeMillis);
        String[] split = timeStamp2Date.split("-");
        return ym2BetweenStamp(split[0] + "-" + split[1]);
    }

    /*
     * 功能描述: 精确到时分秒的时间戳转化
     * 作者: 许方毅
     * 创建时间: 2019年1月19日 下午5:14:54
     * @param timeStamp
     * @return
     */

    public static String timeStamp2Seconds(Long timeStamp) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(timeStamp));
    }

    /*
     * 功能描述: 获取发布者期望的日期数组(针对于重复性求助/服务)
     * 作者: 许方毅
     * 创建时间: 2019年2月19日 下午4:07:06
     * @param timeStamp
     * @param dateWeekArray
     * @param servcieValidityTerm
     * @return
     */

    public static List<String> getWillingDateWeekArray(Long timeStamp, String[] dateWeekArray, Integer servcieValidityTerm) {
        //获取系统建议的日期数组
        long startStamp = getStartStamp(timeStamp);
        Integer validTerm = servcieValidityTerm;
        Long[] dates = new Long[validTerm];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = startStamp + i * interval;

        }

        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < validTerm; i++) {
            String date = timeStamp2Date(startStamp);
            String weekDay = getWeekDay(date);
            for (String dateWeek : dateWeekArray) {
                if (weekDay.equals(dateWeek)) {
                    resultList.add(date);
                }
            }
        }
        return resultList;
    }

    public static Integer getWeekDayInt(long mills) {
		String date = timeStamp2Date(mills);
		int weekDayIndex = getWeekDayIndex(date);
		return ++weekDayIndex;
	}

    /*
     * 功能描述:	将日期转换成周几
     * 作者: 许方毅
     * 创建时间: 2019年2月19日 下午4:11:52
     * @param date
     * @return
     */
    public static String getWeekDay(String date) {
		if(date == null) {
			return null;
		}
		int weekDayIndex = getWeekDayIndex(date);
		String[] weekDayStrs = AppConstant.WEEK_DAY_STRS;
        return weekDayStrs[weekDayIndex];
    }

    private static int getWeekDayIndex(String date) {
		if (date == null) {
			return 0;    //TODO
		}
		if (date.length() > 10) {
			date = date.substring(0, 11);
		}
		String[] split = date.split("-");
		String yearStr = split[0];
		String monthStr = split[1];
		String dayStr = split[2];
		int year = Integer.valueOf(yearStr);
		int month = Integer.valueOf(monthStr);
		int day = Integer.valueOf(dayStr);
		//根据基姆拉尔森公式计算
		if (month == 1) {
			month = 13;
			year--;
		}
		if (month == 2) {
			month = 14;
			year--;
		}
		return (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400) % 7;
	}

    public static Map<String, Object> mills2DHms(Long availableMills) {
        long hourInterval = 60 * 60 * 1000;
        long minuteInterval = 60 * 1000;
        long secondInterval = 1000;
        long day = availableMills / interval;
        availableMills -= day * interval;
        long hour = availableMills / hourInterval;
        availableMills -= hour * hourInterval;
        long minute = availableMills / minuteInterval;
        availableMills -= minute * minuteInterval;
        long second = availableMills / secondInterval;
        Map<String, Object> map = new HashMap<>();
        String hourStr = String.valueOf(hour);
        hourStr = hourStr.length() > 1 ? hourStr : "0" + hourStr;
        String minuteStr = String.valueOf(minute);
        minuteStr = minuteStr.length() > 1 ? minuteStr: "0" + minuteStr;
        String secondStr = String.valueOf(second);
        secondStr = secondStr.length() > 1? secondStr: "0" + secondStr;
        String hms = hourStr + ":" + minuteStr + ":" + secondStr;
        map.put("day", String.valueOf(day));
        map.put("hms", hms);
        return map;
    }

    public static Long yyyymmddToTime(String time){
        String format = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date parse = null;
        try {
            parse = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse.getTime();
    }

    public static void main(String[] args) {
		/*long currentTimeMillis = System.currentTimeMillis();
		System.out.println(currentTimeMillis);
		System.out.println(interval);
		System.out.println(timestamp2Days(currentTimeMillis / interval));*/
		String s = wholeDateToStamp("2019-01-03 23:53:00");
		System.out.println(s);
	}

	public static int getContinueDayCnt(List<Long> createTimeList) {
    	//要求参数createTimeList为倒序
		int count = 0;
		long lastTimeStamp = System.currentTimeMillis();
		for(Long creatTime:createTimeList) {
			if(lastTimeStamp - creatTime > interval) {	//如果当前时间比上轮时间超过24小时连续中断
				break;
			}
			lastTimeStamp = creatTime;
			count ++;
		}
		return count;
	}

	public static int timestamp2Days(long time) {
		Long longVal = time / interval;
    	return longVal.intValue();
	}

	public static String wholeDateToStamp(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = simpleDateFormat.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}
}
