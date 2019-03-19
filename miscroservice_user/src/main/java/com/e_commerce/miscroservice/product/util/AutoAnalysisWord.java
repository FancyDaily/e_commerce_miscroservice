package com.e_commerce.miscroservice.product.util;

import com.e_commerce.miscroservice.product.util.AnsjUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.huaban.analysis.jieba.JiebaSegmenter;
import lombok.Data;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class AutoAnalysisWord {
    private static final String ADDRESS_NAME = "address.txt";
    //            private final String ADDRESS_URL = "http://api.map.baidu.com/geocoder/v2/?address=%s%s&output=json&ak=a8gm7W3L5GRt58CFFXOlNOsrBjnfMHmy&callback=showLocatioN";
    private static final String ADDRESS_URL = "https://restapi.amap.com/v3/geocode/geo?address=%s%s&output=json&key=44bb35ddcd6fece8876ddb39499c9389";
    private Logger logger = LoggerFactory.getLogger(AutoAnalysisWord.class);

    /****省:<市:区>**/
    private static Table<String, String, List<String>> allRegionCache;


    private static Pattern numberTimePattern = Pattern.compile("\\d{1,2}[:点](整|一刻|\\d{2})?");
    private static Pattern timerPattern = Pattern.compile("\\d{1,2}:\\d{1,2}");
    private static Pattern characterTimePattern = Pattern.compile("(一|两|二|三|四|五|六|七|八|九|十|\\d{2})点(整|一刻)?");
    private static Pattern periodWeekPattern = Pattern.compile("((每|每个)?(周|星期|礼拜)(一|二|三|四|五|六|七|天|日|[1-7]))|((每|每个)天)");
    private static Pattern baiduLngPattern = Pattern.compile("(?<=\"lng\":)[\\d.]+");
    private static Pattern baiduLatPattern = Pattern.compile("(?<=\"lat\":)[\\d.]+");
    private static Pattern gaodePattern = Pattern.compile("(?<=\"location\":\")[\\d.,]+");
    private static Pattern isNumPattern = Pattern.compile("[0-9]*");
    private static Pattern regionPattern = Pattern.compile("[\\u4e00-\\u9fa5]{2,}");
    private static Pattern normalPattern = Pattern.compile("一|二|三|四|五|六|七|八|九|十");
    private static Pattern weekTimesPattern = Pattern.compile("(本|这|这个|下|下下|下下个)(周|星期|礼拜)");
    private static Pattern dayPattern = Pattern.compile("(?<=周|星期|礼拜).*");
    //      autoAnalysisWord.parse("哈的点点滴滴我需要20个人");
//        autoAnalysisWord.parse("哈的点点滴滴我要20个人");
//        autoAnalysisWord.parse("哈的点点滴滴我要20人");
//        autoAnalysisWord.parse("哈的点点滴滴总共20人");
//        autoAnalysisWord.parse("哈的点点滴滴要求20人");
//        autoAnalysisWord.parse("哈的点点滴滴参加人数20人");
    private Pattern personCountPattern = Pattern.compile("(总共|要求|需要|人数|要)(\\d{1,3}|一|两|二|三|四|五|六|七|八|九|十|人)(个人|人|人数)");
    //花200分钟
    //给200分钟
    //用200分钟
    //花十分钟
    //花一刻钟
    //用互助时200分钟
    //支付200分钟
    //支付1小时
    //支付两小时20分钟
    //付四十分钟
    private Pattern timePayPattern = Pattern.compile("(花|用|给|互助时|支付|付)(\\d+|一|两|二|三|四|五|六|七|八|九|十)(分钟|刻钟|分|小时)((\\d+|一|两|二|三|四|五|六|七|八|九|十)(分钟|分))?");


    private Pattern pushTypePattern = Pattern.compile("(发布|.*)(求助|服务|需要)");

    private static String DATE_LINE = "-";
    private static Map<String, String> characterToNumberRelation = new HashMap<>();
    private static Map<String, Integer> monthAndDayRelation = new HashMap<>();
    private static Map<String, Integer> dateCharacterToNumberRelation = new HashMap<>();
    private static Map<String, Pattern> characterPatternCache = new LinkedHashMap<>();
    private static DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
    private static DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
    private static DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy" + DATE_LINE + "MM" + DATE_LINE + "dd");
    private static DateTimeFormatter weekDayFormatter = DateTimeFormatter.ofPattern("EEEE");


    static {
        init();
    }

    /**
     * 初始化配置
     */
    private static void init() {

        loadAddress();
        loadConfig();

    }

    public static void test() {


        AutoAnalysisWord autoAnalysisWord = new AutoAnalysisWord();
//        System.out.println(autoAnalysisWord.parse("哈的点点滴滴哈后天到25号每个星期四,每周5,每周1上午10:40到15:20我要花200分钟在滨江星光大道举办一个party"));

        //-----------——日期----------

        //3月24号
        //这个月24号
        //本月24号
        //本周六
        //下个月二号
        //下个月八号
        //下下个星期六
        //这个星期六
        //今天
        //明天
        //xxxx年3月24号
        //24号
        //周六
        //大后天
        //大大后天
        //礼拜六
        //周天、星期天、礼拜天、周日、星期日

        //定期-------
//        autoAnalysisWord.getDate("哈多多岛哈3月25号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈多多岛哈3月25日上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈多多岛哈本月25日上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈多多岛哈这个月25日上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈这周六上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈本周六上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈下个月2号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈下下个月二号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈十月八号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈本周六上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈下个月二号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈下下个星期六上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈这个星期六上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈星期六上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈今天上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈明天上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈2019年3月24号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈明年3月24号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈19年3月24号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈大后年3月24号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈24号上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈大后天上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈大大后天上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈礼拜六上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈周天上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈星期天上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈礼拜天上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈周日上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈星期日上午10:40", "10:40");
//        autoAnalysisWord.getDate("哈的点点滴滴哈星期4上午10:40", "10:40");
        //开始测试时间的准确性
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈下星期四上午10:40", "10:40",""));;
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈下下星期四上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈这个星期四上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈本星期四上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈后天上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈大后天上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈大大后天上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈明天上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈本星期六上午10:40", "10:40",""));

        //测试跨月和跨天的
        //周期性的
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈每星期四,星期三上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈每星期四,礼拜2,星期天上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈每个星期四,礼拜2,星期天上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈每个星期四,每个礼拜2,每星期天,每个礼拜5上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈这个月四号到下个月25号每个星期四,每个礼拜2,每星期天,每个礼拜5上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈明天到下个月25号每个星期四,每个礼拜2,每星期天,每个礼拜5上午10:40", "10:40",""));
//        System.out.println(autoAnalysisWord.getDate("哈的点点滴滴哈每个星期四,每个礼拜2,每星期天,每个礼拜5上午10:40", "10:40",""));


        //----------日期---------


        //--------时间-----------------------

        //明天上午十点一刻到下午四点整
        //明天上午十点一刻到下午04:00
        //明天上午十点一刻到17:40
        //明天上午10:20到下午两点一刻

//        System.out.println(autoAnalysisWord.getTime("这周六上午10:40到jsjsjsj下午4点整我要花200分钟在滨江星光大道举办一个上午party"));;
//        System.out.println(autoAnalysisWord.getTime("这周六上午10:40到下午10点一刻我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getTime("这周六上午10:40到下午04:20我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getTime("这周六上午十点一刻到下午4点整我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getTime("这周六上午11:20到下午十点整我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getTime("这周六上午10:20到下午两点一刻我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getTime("这周六上午十点一刻到下午四点整我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getTime("这周六上午十点一刻到上午12点整我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getTime("这周六上午凌晨一点一刻到凌晨2点整我要花200分钟在滨江星光大道举办一个上午party"));
//        System.out.println(autoAnalysisWord.getDate("这周六上午十点一刻到下午四点整我要花200分钟在滨江星光大道举办一个上午party", "十点一刻",""));

        //--------时间-----------------------

        //--------地理位置-----------
//        System.out.println(autoAnalysisWord.getLocation("这周六上午10:40到15:20我要花200分钟在滨江星光大道举办一个party"));
//        System.out.println(autoAnalysisWord.getLocation("这周六上午10:40到15:20我要花200分钟在上城赞成中心大厦举办一个party"));
//        new AutoAnalysisWord().getLocation("这周六上午十点一刻到15:20我要花200分钟在西湖区第七人民医院门口举办一个party");

        //--------地理位置-----------

        //------------获取详细的时间-----------
//        System.out.println(autoAnalysisWord.getTime("哈的点点滴滴哈明天到下个月25号每个星期四,每个礼拜2,每星期天,每个礼拜5上午10:40到15:20我要花200分钟在滨江星光大道举办一个party"));
//        System.out.println(autoAnalysisWord.getTime("哈的点点滴滴哈后天到25号每个星期四,每周5,每周1上午10:40到15:20我要花200分钟在滨江星光大道举办一个party"));
//        System.out.println(autoAnalysisWord.getTime("哈的点点滴滴哈后天到25号每天上午10:40到15:20我要花200分钟在滨江星光大道举办一个party"));
        //------------获取详细的时间-----------

        //获取人数=================
//        autoAnalysisWord.parse("哈的点点滴滴哈后天到25号每个星期四,每周5,每周1上午10:40到15:20我要花200分钟在滨江星光大道举办一个party");
//        System.out.println(autoAnalysisWord.getPersonCount("哈的点点滴滴我需要20个人"));
//        System.out.println(autoAnalysisWord.getPersonCount("哈的点点滴滴我要20个人"));
//        System.out.println( autoAnalysisWord.getPersonCount("哈的点点滴滴我要20人"));
//        System.out.println( autoAnalysisWord.getPersonCount("哈的点点滴滴总共20人"));
//        System.out.println( autoAnalysisWord.getPersonCount("哈的点点滴滴要求20人"));
//        System.out.println( autoAnalysisWord.getPersonCount("哈的点点滴滴参加人数20人"));
//        System.out.println( autoAnalysisWord.getPersonCount("哈的点点滴滴参加人数十人"));

        //获取花费时间==============================================
        //花200分钟
        //花十分钟
        //花一刻钟
        //用互助时200分钟
        //支付200分钟
        //支付1小时
        //支付两小时20分钟
        //四十分钟付
//        System.out.println(autoAnalysisWord.getPayCount("ddkdkkd花200分钟ksskks"));
//        System.out.println(autoAnalysisWord.getPayCount("ddkdkkd花花十分钟ksskks"));
//        System.out.println(autoAnalysisWord.getPayCount("ddkdkkd用互助时200分钟ksskks"));
//        System.out.println(autoAnalysisWord.getPayCount("ddkdkkd支付200分钟ksskks"));
//        System.out.println(autoAnalysisWord.getPayCount("ddkdkkd支付1小时ksskks"));
//        System.out.println(autoAnalysisWord.getPayCount("ddkdkkd支付两小时20分钟ksskks"));
//        System.out.println(autoAnalysisWord.getPayCount("ddkdkkd支付12小时20分钟ksskks"));

        //获取服务的类型======================
//        System.out.println(autoAnalysisWord.getPushType("惺惺相惜啊是发布求助2222"));
//        System.out.println(autoAnalysisWord.getPushType("44444发布服务3333"));


    }

    public static void main(String[] args) {

//        System.out.println(new AutoAnalysisWord().parse("我需要有人明天上午十点在上城赞成中心帮我带早餐,我可以支付十分钟", ""));
//        System.out.println(new AutoAnalysisWord().parse("我需要有人明天上午11点在赞成中心帮我带早餐,我可以支付十分钟", "杭州"));
        System.out.println(new AutoAnalysisWord().parse("下个月15号到下个月24号, 每天下午03:40开游泳party。", "杭州"));


        test();
    }

    /**
     * 时间返回类
     */
    @Data
    private class TimeInfo {
        private String startDate;
        private String endDate;
        private String startTime;
        private String endTime;
        private String text;
        private String weekDay;
    }

    /**
     * 解析文本消息
     *
     * @param text     文本
     * @param cityName 城市名称
     *                 {
     *                 "startDate":2019-03-25, //开始日期
     *                 "endDate":2019-03-25, //结束日期
     *                 "startTime":14：00, //24小时制的开始时间
     *                 "endTime":14：00, //24小时制的结束时间
     *                 "work":"xxx",//需要做什么
     *                 "weekDay":"1,4,5",//周期性的时间
     *                 "location":xxxx,//发布活动的地址名称
     *                 "latitude":xxxx,//纬度
     *                 "longitude":xxxx,//经度
     *                 "payCount":200,//支付的总的分钟数量
     *                 "personCount":30,//参加需要总的人数
     *                 "pushType":1,//服务的服务类型 1 求助 2服务
     *                 }
     * @return
     */
    public Map<String, Object> parse(String text, String cityName) {


        Map<String, Object> resultMap = new HashMap<>();

        //解析时间
        TimeInfo time = getTime(text);

        if (time.getStartDate() != null) {
            text = time.getText();

            resultMap.put("startDate", time.getStartDate());
            resultMap.put("endDate", time.getEndDate());
            resultMap.put("startTime", time.getStartTime());
            resultMap.put("endTime", time.getEndTime());
            resultMap.put("weekDay", time.getWeekDay());

        } else {


            resultMap.put("startDate", "");
            resultMap.put("endDate", "");
            resultMap.put("startTime", "");
            resultMap.put("endTime", "");
            resultMap.put("weekDay", "");
        }

        //获取参加人数
        PersonInfo personCount = getPersonCount(text);

        if (personCount.getPersonCount() != null) {
            text = personCount.getText();
            resultMap.put("personCount", personCount.getPersonCount());
        } else {

            resultMap.put("personCount", "");
        }

        //解析需要花多长时间
        PayInfo payInfo = getPayCount(text);

        if (payInfo.getPayCount() != null) {
            text = payInfo.getText();
            resultMap.put("payCount", payInfo.getPayCount());
        } else {
            resultMap.put("payCount", "");
        }

        //解析发布的类型
        PushInfo pushInfo = getPushType(text);

        if (pushInfo.getPushType() != null) {
            text = payInfo.getText();
            resultMap.put("pushType", pushInfo.getPushType());
        } else {
            resultMap.put("pushType", "");
        }


        //解析地点
        TextInfo location = getLocation(text, cityName);

        if (location.getLocation() != null) {
            resultMap.put("location", location.getLocation().getLocation());
            resultMap.put("longitude", location.getLocation().getLongitude());
            resultMap.put("latitude", location.getLocation().getLatitude());
            StringBuilder workBuild = new StringBuilder();
            if (location.getHeadInfo() != null) {
                for (WordInfo wordInfo : location.getHeadInfo()) {
                    workBuild.append(wordInfo.getWordName());
                }
            }


            if (location.getTailInfo() != null) {
                workBuild.delete(0, workBuild.length());
                for (WordInfo wordInfo : location.getTailInfo()) {
                    workBuild.append(wordInfo.getWordName());
                }
            }
            text = workBuild.toString();


//            StringBuilder workBuild = new StringBuilder();
//            if (location.getHeadInfo() != null) {
//                for (WordInfo wordInfo : location.getHeadInfo()) {
//                    workBuild.append(wordInfo.getWordName());
//                }
//            }
//
//
//            StringBuilder newWorkBuild = new StringBuilder();
//            //动词 +名次+形容词
//            if (workBuild.length() != 0) {
//
//                List<WordInfo> wordGroup = getWordGroup(workBuild.toString(), Boolean.TRUE);
//
//                System.out.println(wordGroup);
//                workBuild.delete(0, workBuild.length());
//            }
//
//
//            if (location.getTailInfo() != null) {
//
//
//                for (WordInfo wordInfo : location.getTailInfo()) {
//                    workBuild.append(wordInfo.getWordName());
//                }
//
//            }
//            System.out.println("___________");
//            System.out.println("___________");
//            System.out.println("___________");
//
//            if (workBuild.length() != 0) {
//                List<WordInfo> wordGroup = getWordGroup(workBuild.toString(), Boolean.TRUE);
//                System.out.println(wordGroup);
//
//            }

        } else {
            resultMap.put("location", "");
            resultMap.put("longitude", "");
            resultMap.put("latitude", "");

        }

        resultMap.put("work", text);
        return resultMap;


    }


    @Data
    private class PayInfo {
        private Integer payCount;
        private String text;
    }


    @Data
    private class PersonInfo {
        private Integer personCount;
        private String text;
    }

    @Data
    private class PushInfo {
        private Integer pushType;
        private String text;
    }


    /**
     * 获取发布类型
     *
     * @param text 原始数据
     * @return
     */
    private PushInfo getPushType(String text) {

        PushInfo pushInfo = new PushInfo();

        Matcher pushMatcher = pushTypePattern.matcher(text);


        if (pushMatcher.find()) {
            text = text.replaceAll(pushMatcher.group(), "");


            String pushTypeStr = pushMatcher.group(2);

            pushInfo.setPushType(dateCharacterToNumberRelation.get(pushTypeStr));


        }

        pushInfo.setText(text);

        return pushInfo;

    }


    /**
     * 获取支付
     *
     * @param text 原始数据
     * @return
     */
    private PayInfo getPayCount(String text) {

        PayInfo payInfo = new PayInfo();

        Matcher payMatcher = timePayPattern.matcher(text);
        Integer total = null;

        if (payMatcher.find()) {
            String startTimeStr = payMatcher.group(2);
            Integer startTime;

            if (isNumeric(startTimeStr)) {
                startTime = Integer.valueOf(startTimeStr);
            } else {
                startTime = dateCharacterToNumberRelation.get(startTimeStr);

            }


            String timeUnit = payMatcher.group(3);
            String endTimeStr = payMatcher.group(5);
            endTimeStr = endTimeStr == null ? "0" : endTimeStr;

            Integer endTime;

            if (isNumeric(endTimeStr)) {
                endTime = Integer.valueOf(endTimeStr);
            } else {
                endTime = dateCharacterToNumberRelation.get(endTimeStr);

            }


            text = text.replaceAll(payMatcher.group(), "");

            if (timeUnit.contains("时")) {
                total = startTime * 60 + endTime;
            } else {
                total = startTime + endTime;
            }

        }

        payInfo.setText(text);
        payInfo.setPayCount(total);

        return payInfo;

    }

    /**
     * 获取人数
     *
     * @param text 原数据
     */
    private PersonInfo getPersonCount(String text) {

        PersonInfo personInfo = new PersonInfo();

        Matcher personMatcher = personCountPattern.matcher(text);

        if (personMatcher.find()) {
            text = text.replaceAll(personMatcher.group(), "");
            String countStr = personMatcher.group(2);
            if (isNumeric(countStr)) {
                personInfo.setPersonCount(Integer.valueOf(countStr));
            } else {
                personInfo.setPersonCount(dateCharacterToNumberRelation.get(countStr));

            }
        }

        personInfo.setText(text);


        return personInfo;

    }

    /**
     * 获取时间
     *
     * @param text 原数据
     * @return
     */
    private TimeInfo getTime(String text) {

        String originalStartTime = "";
        String originalEndime = "";

        TimeInfo timeInfo = new TimeInfo();
        String startTime = "";
        String endTime = "";
        String replaceAllStr = "";

        Matcher matcher = numberTimePattern.matcher(text);
        String tmpTime = "";
        if (matcher.find()) {
            tmpTime = matcher.group();
            //格式符合xx:xx到xx:xx
            if (matcher.find()) {
                startTime = tmpTime;
                originalStartTime = tmpTime;
                endTime = matcher.group();
                originalEndime = endTime;
                //进行字符串的替换,去除时间
                replaceAllStr = "(上午|早上|凌晨)?" + startTime + ".*" + "(上午|早上|凌晨|下午|傍晚|中午|夜里|晚间|夜晚)?" + endTime;


                //转换成24小时制
                if (timerPattern.matcher(endTime).matches()) {
                    String[] arrs = endTime.split(":");
                    Integer sumTime = Integer.parseInt(arrs[0]) + 12;
                    endTime = sumTime > 24 ? arrs[0] : sumTime + ":" + arrs[1];
                    //4点整
                } else if (endTime.contains("点")) {
                    String[] arrs = endTime.split("点");
                    Integer sumTime = Integer.parseInt(arrs[0]) + 12;
                    endTime = sumTime > 24 ? arrs[0] : sumTime + ":" + characterToNumberRelation.get(arrs[1]);
                }

            }

        }

        Boolean isSingleDateFlag = Boolean.FALSE;

        String matchOringalEndTime = "";

        if (startTime.isEmpty()) {
            //三种情况，一种是全文字，一种是半文字半时间 一种是只有一个时间
            Matcher characterMatcher = characterTimePattern.matcher(text);
            String _tmpTime = "";
            if (!characterMatcher.find()) {
                //考虑另外一种情况为纯数字
                if (tmpTime.isEmpty()) {
                    return timeInfo;
                } else {
                    _tmpTime = tmpTime;
                }
            }
            if (_tmpTime.isEmpty()) {
                _tmpTime = characterMatcher.group();
            }


            if (tmpTime.isEmpty() && characterMatcher.find()) {
                //全文字
                //明天上午十点一刻到下午四点整
                startTime = _tmpTime;
                originalStartTime = _tmpTime;
                endTime = characterMatcher.group();
                originalEndime = endTime;
                matchOringalEndTime = originalEndime;

                //进行字符串的替换,去除时间
                replaceAllStr = "(上午|早上|凌晨)?" + startTime + ".*" + "(上午|早上|凌晨|下午|傍晚|中午|夜里|晚间|夜晚)?" + endTime;


            } else if (tmpTime.equals(_tmpTime) || tmpTime.isEmpty()) {
                isSingleDateFlag = Boolean.TRUE;
                //只有一个时间
                //判断是否只有一个时间,那么默认当前开始时候到结束时间
                startTime = timeFormatter.format(LocalDateTime.now());
                endTime = _tmpTime;
                //进行字符串的替换,去除时间
                replaceAllStr = "(上午|早上|凌晨|下午|傍晚|中午|夜里|晚间|夜晚)?" + endTime;
                originalStartTime = endTime;
                originalEndime = "";
                matchOringalEndTime = endTime;

            } else {
                //半文字半时间

                //明天上午十点一刻到下午04:00
                //明天上午十点一刻到下午4点整
                //明天上午十点一刻到17:40
                //明天上午10:20到下午两点一刻


                //比较谁先谁后

                if (text.indexOf(_tmpTime) < text.indexOf(tmpTime)) {
                    startTime = _tmpTime;
                    originalStartTime = _tmpTime;
                    endTime = tmpTime;
                    originalEndime = endTime;
                    matchOringalEndTime = endTime;
                } else {
                    startTime = tmpTime;
                    originalStartTime = tmpTime;
                    endTime = _tmpTime;
                    originalEndime = endTime;
                    matchOringalEndTime = endTime;

                }

                //进行字符串的替换,去除时间
                replaceAllStr = "(上午|早上|凌晨)?" + startTime + ".*" + "(上午|早上|凌晨|下午|傍晚|中午|夜里|晚间|夜晚)?" + endTime;


            }


            if (startTime.contains("点")) {
                String[] _startTimeArr = startTime.split("点");
                startTime = characterToNumberRelation.get(_startTimeArr[0]) + ":" + characterToNumberRelation.get(_startTimeArr[1]);

            }
            if (endTime.contains("点")) {
                if (endTime.endsWith("点")) {
                    endTime += "整";
                }
                String[] _endTimeArr = endTime.split("点");

                endTime = (isNumeric(_endTimeArr[0]) ? _endTimeArr[0] : characterToNumberRelation.get(_endTimeArr[0])) + ":" + characterToNumberRelation.get(_endTimeArr[1]);

            }

            //判断是否需要转换成24小时制
            if (Pattern.compile("(下午|傍晚|中午|夜里|晚间|夜晚)" + matchOringalEndTime).matcher(text).find()) {
                //转换成24小时制
                String[] arrs = endTime.split(":");
                Integer sumTime = Integer.parseInt(arrs[0]) + 12;
                endTime = sumTime > 24 ? arrs[0] : sumTime + ":" + arrs[1];

            }


        }

        timeInfo.setStartTime(startTime);
        timeInfo.setEndTime(endTime);
        DateInfo dateInfo = getDate(text, originalStartTime, originalEndime);
        timeInfo.setWeekDay(dateInfo.getPeriodWeekDay());
        if (isSingleDateFlag) {
            //增加一个判断条件如果是周期性的每天下午03:00
            if (dateInfo.getStartDate() != null && dateInfo.getEndDate() != null && dateInfo.getPeriodWeekDay() != null) {
                timeInfo.setStartTime("00:00");
                timeInfo.setStartDate(dateInfo.getStartDate());
                timeInfo.setEndDate(dateInfo.getEndDate());
            } else {
                timeInfo.setStartDate(dateFormatter.format(LocalDateTime.now()));
                timeInfo.setEndDate(dateInfo.getStartDate());
            }


        } else {

            timeInfo.setStartDate(dateInfo.getStartDate());
            timeInfo.setEndDate(dateInfo.getEndDate());
        }
        timeInfo.setText(dateInfo.getText().replaceAll(replaceAllStr, ""));
        return timeInfo;

    }


    @Data
    private class DateInfo {
        private String startDate;
        private String endDate;
        private String periodWeekDay;
        private String text;
        private String replaceStr;
    }

    /**
     * 获取周期性任务
     *
     * @param text 文本
     * @return
     */
    private DateInfo getPeriodDate(String text) {
        DateInfo periodDateInfo = new DateInfo();
        StringBuilder resultBuild = new StringBuilder();

        Set<Integer> resultSort = new TreeSet<>();
        Matcher periodMatcher = periodWeekPattern.matcher(text);
        boolean firstAccessFlag = true;
        StringBuilder replaceAllBuild = new StringBuilder();
        //计算频率时间
        while (periodMatcher.find()) {
            String dayOfWeekGroup = periodMatcher.group();
            if (replaceAllBuild.length() > 0) {
                replaceAllBuild.append("(.*)");
            }
            replaceAllBuild.append(dayOfWeekGroup);
            if (firstAccessFlag) {
                firstAccessFlag = false;
                if (!dayOfWeekGroup.startsWith("每")) {
                    break;
                } else {
                    dayOfWeekGroup = dayOfWeekGroup.substring(1);
                }
            }

            //考虑每天
            if (dayOfWeekGroup.equals("天")) {
                //添加7天
                for (int i = 1; i < 8; i++) {
                    resultSort.add(i);
                }

            } else {
                String days = dayOfWeekGroup.split("周|星期|礼拜")[1];
                Integer dayNum;
                if (isNumeric(days)) {
                    dayNum = Integer.valueOf(days);
                } else {
                    dayNum = Integer.valueOf(dateCharacterToNumberRelation.get(days));
                }

                resultSort.add(dayNum);
            }


        }
        if (replaceAllBuild.length() != 0) {
            text = text.replaceAll(replaceAllBuild.toString(), "");
            replaceAllBuild.delete(0, replaceAllBuild.length());
        }

        String dataStr;
        String startDate = "";
        String startDateReplace = "";
        String endDate = "";
        String endDateReplace = "";
        //计算日期
        for (Pattern pattern : characterPatternCache.values()) {
            Matcher characterMatcher = pattern.matcher(text);
            while (characterMatcher.find()) {
                dataStr = characterMatcher.group();

                if (startDateReplace.isEmpty()) {
                    startDateReplace = dataStr;
                } else if (endDateReplace.isEmpty()) {
                    endDateReplace = dataStr;
                }

                //将号替换成日
                if (dataStr.endsWith("号")) {
                    dataStr = dataStr.substring(0, dataStr.length() - 1) + "日";
                }

                String detailsDate = getDetailsDate(dataStr);
                if (!detailsDate.isEmpty()) {
                    if (startDate.isEmpty()) {
                        startDate = detailsDate;
                    } else if (endDate.isEmpty()) {
                        endDate = detailsDate;
                        if (endDate.compareTo(startDate) < 0) {
                            String tmp = endDate;
                            endDate = startDate;
                            startDate = tmp;
                            tmp = startDateReplace;
                            startDateReplace = endDateReplace;
                            endDateReplace = tmp;
                        }
                    } else {
                        break;
                    }
                }


            }
        }


        for (Integer _day : resultSort) {
            if (resultBuild.length() > 0) {
                resultBuild.append(",");
            }
            resultBuild.append(_day);
        }

        text = text.replaceAll(startDateReplace + "(.*)" + endDateReplace, "");

        periodDateInfo.setEndDate(endDate);
        periodDateInfo.setStartDate(startDate);
        periodDateInfo.setText(text);
        periodDateInfo.setPeriodWeekDay(resultBuild.toString());

        return periodDateInfo;


    }

    /**
     * 根据原始的文本信息和原始的开始时间获取执行的周期
     *
     * @param text              原始文本
     * @param originalStartTime 原始的开始时间
     * @param originalStartTime 原始的结束时间
     * @return
     */
    private DateInfo getDate(String text, String originalStartTime, String originalEndTime) {
        String result = "";
        DateInfo dateInfo;

        Matcher matcher = Pattern.compile(".*(?=(上午|早上|凌晨|下午|傍晚|中午|夜里|晚间|夜晚)?" + originalStartTime + ")").matcher(text);
        String dataStr = "";
        if (matcher.find()) {
            String prefixDate = matcher.group().replaceAll("(上午|早上|凌晨|下午|傍晚|中午|夜里|晚间|夜晚)", "");

            //先检查周期性任务
            dateInfo = getPeriodDate(prefixDate);

            if (!dateInfo.getPeriodWeekDay().isEmpty()) {
                Matcher suffixMatcher = Pattern.compile("(?<=" + (originalEndTime.isEmpty() ? originalStartTime : originalEndTime) + ").*").matcher(text);
                if (suffixMatcher.find()) {
                    dateInfo.setText(dateInfo.getText() + suffixMatcher.group());
                }

                return dateInfo;
            }


            for (Pattern pattern : characterPatternCache.values()) {
                Matcher characterMatcher = pattern.matcher(prefixDate);
                if (characterMatcher.find()) {
                    dataStr = characterMatcher.group();
                    text = text.replaceAll(dataStr, "");
                    //将号替换成日
                    if (dataStr.endsWith("号")) {
                        dataStr = dataStr.substring(0, dataStr.length() - 1) + "日";
                    }
                    break;
                }
            }


        }

        if (!dataStr.isEmpty()) {
            result = getDetailsDate(dataStr);
        }

        dateInfo = new DateInfo();

        dateInfo.setStartDate(result);
        dateInfo.setText(text);

        return dateInfo;
    }


    /**
     * 获取详细的时间
     *
     * @param dataStr 时间字符串
     * @return
     */
    private String getDetailsDate(String dataStr) {
        StringBuilder dateBuild = new StringBuilder();

        LocalDate now = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(now, LocalTime.now());

        String standardYear = yearFormatter.format(now);
        String standardMonth = monthFormatter.format(now);
        String standardDay = dayFormatter.format(now);
        Integer yearTmp;
        Integer monthTmp;
        Integer dayTmp;

        //计算特定日期
        if (!dataStr.contains("年")) {
            yearTmp = Integer.valueOf(standardYear);

        } else {
            String year = dataStr.split("年")[0];
            if (isNumeric(year)) {
                if (year.length() == 2) {
                    yearTmp = Integer.valueOf("20" + year);
                } else {

                    yearTmp = Integer.valueOf(year);
                }
            } else {
                //计算年数
                yearTmp = Integer.parseInt(standardYear) + dateCharacterToNumberRelation.get(year);
            }


        }


        if (!dataStr.contains("月")) {
            monthTmp = Integer.valueOf(standardMonth);

        } else {
            String[] monthArr = dataStr.split("月")[0].split("年");

            String month = monthArr[monthArr.length - 1];
            if (isNumeric(month)) {
                monthTmp = Integer.valueOf(month);
            } else {

                //计算月数
                Integer totalMonthDay = dateCharacterToNumberRelation.get(month);
                if (!normalPattern.matcher(month).matches()) {
                    totalMonthDay += Integer.parseInt(standardMonth);
                }


                if (totalMonthDay > 12) {
                    yearTmp += 1;
                    monthTmp = totalMonthDay - 12;
                } else {
                    monthTmp = totalMonthDay;
                }

            }

        }


        if (!dataStr.endsWith("日") || dataStr.endsWith("周日") ||
                dataStr.endsWith("星期日") || dataStr.endsWith("礼拜日")) {

            String[] dayArr = dataStr.split("月");
            String day = dayArr[dayArr.length - 1];
            Matcher dayTimesMatcher = weekTimesPattern.matcher(day);
            Integer times = 0;
            Integer dayNum = 0;

            if (dayTimesMatcher.find()) {

                String dayTimes = dayTimesMatcher.group(1);
                day = day.replaceAll(dayTimes, "");
                times = dateCharacterToNumberRelation.get(dayTimes);
            }
            Integer totalDay = null;

            Matcher dayMatcher = dayPattern.matcher(day);
            if (dayMatcher.find()) {
                String _dayStr = dayMatcher.group();
                if (isNumeric(_dayStr)) {
                    dayNum = Integer.valueOf(_dayStr);
                } else {
                    dayNum = dateCharacterToNumberRelation.get(dayMatcher.group());
                }
            } else {
                String _dateStr = dataStr.split("天")[0];
                totalDay = dateCharacterToNumberRelation.get(_dateStr);
            }

            //获取今天是本周周几
            Integer dayOfWeekNum = dateCharacterToNumberRelation.get(weekDayFormatter.format(localDateTime).toLowerCase());
            Integer dayOfWeek;
            if (dayOfWeekNum != null) {
                dayOfWeek = dayOfWeekNum;
            } else {
                String dayOfWeekStr = weekDayFormatter.format(localDateTime).split("周|星期|礼拜")[1];

                if (isNumeric(dayOfWeekStr)) {
                    dayOfWeek = Integer.valueOf(dayOfWeekStr);
                } else {
                    dayOfWeek = Integer.valueOf(dateCharacterToNumberRelation.get(dayOfWeekStr));
                }
            }


            if (totalDay == null) {


                if (times > 1) {
                    totalDay = (times - 1) * 7 + dayNum + (7 - dayOfWeek);

                } else {

                    if (times == 1) {

                        totalDay = 7 - dayOfWeek + dayNum;
                    } else {
                        totalDay = dayNum - dayOfWeek;

                    }
                }

            }

            dayTmp = Integer.valueOf(standardDay) + totalDay;


        } else {

            //计算天数
            String[] dayArr = dataStr.split("日")[0].split("月");

            String day = dayArr[dayArr.length - 1];

            if (isNumeric(day)) {
                dayTmp = Integer.valueOf(day);
            } else {

                Integer totalDay = dateCharacterToNumberRelation.get(day);
                if (!normalPattern.matcher(day).matches()) {
                    totalDay += Integer.valueOf(standardDay);
                }
                dayTmp = totalDay;


            }
        }


        //计算天数是否超出

        Integer claMonthNeedDayOfMonth = monthAndDayRelation.get(standardMonth);
        //润年2月加一天
        if (standardMonth.equals("2") && now.isLeapYear()) {
            claMonthNeedDayOfMonth += 1;
        }

        if (dayTmp > claMonthNeedDayOfMonth) {
            monthTmp += 1;
            if (monthTmp > 12) {
                yearTmp += 1;
                monthTmp = monthTmp - 12;
            }
            dayTmp = claMonthNeedDayOfMonth - dayTmp;
        }
        dayTmp = Math.abs(dayTmp);

        dateBuild.append(yearTmp);
        dateBuild.append(DATE_LINE);
        if (monthTmp.toString().length() == 1) {
            dateBuild.append("0");
        }
        dateBuild.append(monthTmp);
        dateBuild.append(DATE_LINE);
        if (dayTmp.toString().length() == 1) {
            dateBuild.append("0");
        }
        dateBuild.append(dayTmp);


        return dateBuild.toString();
    }

    /**
     * 判断是否是数字
     *
     * @param str 需要带判断的类型
     * @return
     */
    private boolean isNumeric(String str) {
        return isNumPattern.matcher(str).matches();
    }

    /**
     * 文本信息
     */
    @Data
    private class TextInfo {
        private List<WordInfo> headInfo;
        private LocationInfo location;
        private List<WordInfo> tailInfo;

    }

    /**
     * 根据输入文本获取地址
     *
     * @param text     需要输入的文本
     * @param cityName 城市名称
     */
    private TextInfo getLocation(String text, String cityName) {
        TextInfo textInfo = new TextInfo();

        //获取分组
        List<WordInfo> wordGroup = getWordGroup(text, Boolean.FALSE);

        if (wordGroup == null || wordGroup.isEmpty()) {
            logger.warn("词语={}获取不到分组", text);
            return textInfo;
        }

        //获取地理位置以及后面的词汇的集合
        List<WordInfo> regionNames = getRegionName(wordGroup);

        if (regionNames == null || regionNames.isEmpty()) {
            //这里有一种情况 用户只输入了地理位置没有输入城市名称
            regionNames = wordGroup;
        } else {
            //首个市城市名称并且去除城市集合
            cityName = regionNames.get(0).getWordName();
            regionNames.remove(0);

        }


        //获取输入文本中该城市的详细地址并且去除该地址的集合
        LocationInfo address = getAddress(cityName, regionNames);
        if (address == null || address.getLocation() == null || address.getLocation().isEmpty()) {
            logger.warn("词语={}获取不地理位置", text);
            return textInfo;
        }
        textInfo.setLocation(address);
        textInfo.setHeadInfo(wordGroup);
        textInfo.setTailInfo(regionNames);
        return textInfo;
    }


    @Data
    private class WordInfo {
        private String wordName;
        private String wordNature;
    }

    /**
     * 获取词分组
     *
     * @param text 代分组的字符串
     * @return
     */
    private List<WordInfo> getWordGroup(String text, Boolean needParse) {
        List<WordInfo> list = new ArrayList<>();

        if (needParse) {
            Result parse = ToAnalysis.parse(text);
            List<Term> terms = parse.getTerms();

            for (Term term : terms) {
                WordInfo wordInfo = new WordInfo();
                wordInfo.setWordName(term.getName());
                wordInfo.setWordNature(AnsjUtil.getPartOfSpeech(term.getNatureStr()));
                list.add(wordInfo);

            }

        } else {

            List<String> results = new JiebaSegmenter().sentenceProcess(text);


            for (String str : results) {

                WordInfo wordInfo = new WordInfo();

                wordInfo.setWordName(str);

                list.add(wordInfo);
            }
        }


        return list;
    }


    /**
     * 根据所有地理位置获取区域名称
     *
     * @param allAreas 所有的带检查的地址
     */
    private List<WordInfo> getRegionName(List<WordInfo> allAreas) {

        List<WordInfo> result = new LinkedList<>();

        WordInfo wordInfo;
        for (Iterator<WordInfo> iterator = allAreas.iterator(); iterator.hasNext(); ) {
            wordInfo = iterator.next();
            String area = wordInfo.getWordName();
            //先精准匹配
            if (result.isEmpty()) {

                if (allRegionCache.containsRow(area)
                        || allRegionCache.containsColumn(area)) {
                    result.add(wordInfo);
                    iterator.remove();
                } else {
                    continue;
                }

            } else {
                result.add(wordInfo);
                iterator.remove();
            }


        }

        if (!result.isEmpty()) {
            return result;
        }

        for (Iterator<WordInfo> iterator = allAreas.iterator(); iterator.hasNext(); ) {
            wordInfo = iterator.next();
            String area = wordInfo.getWordName();

            if (!result.isEmpty()) {
                result.add(wordInfo);
                iterator.remove();
                continue;
            }

            if (!regionPattern.matcher(area).matches()) {
                continue;
            }

            //模糊匹配
            List<String> collect = allRegionCache.rowKeySet().parallelStream().
                    filter(x -> x.contains(area)).collect(Collectors.toList());

            if (collect.isEmpty()) {
                collect = allRegionCache.columnKeySet().parallelStream().
                        filter(x -> x.contains(area)).collect(Collectors.toList());

            }


            if (collect.isEmpty()) {
                List areas = new ArrayList<>();
                //TODO:可以优化
                allRegionCache.values().parallelStream().forEach(x ->
                        x.stream().forEach(y -> {
                            if (y.contains(area)) {
                                areas.add(y);
                            }
                        }));

                collect.addAll(areas);

            }

            if (!collect.isEmpty()) {
                result.add(wordInfo);
                iterator.remove();
            }


        }
        return result;


    }

    /**
     * 根据输入的分粗信息获取详细城市的地区
     *
     * @param cityName 城市的名称
     * @param allAreas 所有输入的分组信息
     * @return
     */
    private LocationInfo getAddress(String cityName, List<WordInfo> allAreas) {

        LocationInfo address = null;

        int len = allAreas.size();

        StringBuilder suffixBuild = new StringBuilder();

        outer:
        for (int i = 0; i < len; i++) {


            if (suffixBuild.length() != 0) {
                suffixBuild.delete(0, suffixBuild.length());
            }


            address = isAddress(allAreas.get(i).getWordName(), cityName);

            if (address.getLocation() == null || address.getLocation().isEmpty()) {
                suffixBuild.append(allAreas.get(i).getWordName());

                for (int j = i + 1; j < len; j++) {

                    String _otherAddress = allAreas.get(j).getWordName();
                    suffixBuild.append(_otherAddress);
                    address = isAddress(suffixBuild.toString(), cityName);
                    if (address.getLocation() != null && !address.getLocation().isEmpty()) {

                        //保留剩余的集合
                        if (j + 1 != len) {
                            List<WordInfo> newList = new ArrayList<>(allAreas.subList(j + 1, len));
                            allAreas.clear();
                            if (!newList.isEmpty()) {
                                allAreas.addAll(newList);
                            }
                        } else {
                            allAreas.clear();
                        }
                        break outer;
                    }
                }

            } else {

                if (i + 1 != len) {
                    List<WordInfo> newList = new ArrayList<>(allAreas.subList(i + 1, len));
                    allAreas.clear();
                    if (!newList.isEmpty()) {
                        allAreas.addAll(newList);
                    }
                } else {
                    allAreas.clear();
                }
                break;
            }


        }

        return address;

    }


    /**
     * 地理位置的信息
     */
    @Data
    private class LocationInfo {
        private Double longitude;
        private Double latitude;
        private String location;
    }

    /**
     * 判断输入的是否是地址
     *
     * @param address 查询的地址
     * @param city    查询的城市
     * @return
     */
    private LocationInfo isAddress(String address, String city) {

        LocationInfo locationInfo = new LocationInfo();


        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) new URL(String.format(ADDRESS_URL, city, address)).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                StringBuilder allResults = new StringBuilder();
                //得到响应流
                InputStream inputStream = conn.getInputStream();
                //获取响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    allResults.append(line);
                }
                reader.close();

                if (isResultOk(allResults.toString(), address)) {
                    locationInfo.setLocation(city + address);

                    getPoint(allResults.toString(), locationInfo);

                }


            }


        } catch (IOException e) {

        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

        return locationInfo;

    }


    /**
     * 获取经纬度
     *
     * @param result 结果
     * @return
     */
    private void getPoint(String result, LocationInfo locationInfo) {

        if (ADDRESS_URL.contains("baidu")) {
            Matcher lngMatcher = baiduLngPattern.matcher(result);
            Matcher latMatcher = baiduLatPattern.matcher(result);
            if (lngMatcher.find()) {
                locationInfo.setLongitude(Double.parseDouble(lngMatcher.group()));
            }
            if (latMatcher.find()) {
                locationInfo.setLatitude(Double.parseDouble(latMatcher.group()));
            }


        } else if (ADDRESS_URL.contains("amap")) {

            Matcher pointMatcher = gaodePattern.matcher(result);
            if (pointMatcher.find()) {
                String[] groupArr = pointMatcher.group().split(",");
                locationInfo.setLongitude(Double.parseDouble(groupArr[0]));
                locationInfo.setLatitude(Double.parseDouble(groupArr[1]));
            }

        } else {
            //others
        }
    }

    /**
     * 根据结果判断是否返回成功
     *
     * @param result  结果
     * @param address 地理位置
     * @return
     */
    private boolean isResultOk(String result, String address) {
        if (ADDRESS_URL.contains("baidu")) {
            return !result.contains("UNKNOWN");
        } else if (ADDRESS_URL.contains("amap")) {

            return result.contains(address);
        } else {
            return false;
        }
    }


    /**
     * 加载所有地址
     */
    private static void loadAddress() {

        try {

            List<String> allRegions = IOUtils.readLines(new ClassPathResource("/properties/"
                    + ADDRESS_NAME).getInputStream(), "utf-8");
//            List<String> allRegions = null;
//            try {
//                allRegions = Files.readAllLines(Paths.get(AutoAnalysisWord.class.getResource("/properties/"
//                        + ADDRESS_NAME).toURI()));
//            } catch (URISyntaxException e) {
//
//            }

            allRegionCache = HashBasedTable.create();

            for (String region : allRegions) {

                String[] regionArr = region.split(",");
                if (regionArr.length != 3) {
                    continue;
                }
                List<String> areas = allRegionCache.get(regionArr[0], regionArr[1]);
                if (areas == null) {
                    areas = new ArrayList<>();
                }
                areas.add(regionArr[2]);

                allRegionCache.put(regionArr[0], regionArr[1], areas);


            }


        } catch (IOException e) {

            throw new RuntimeException("load address has error " + e.getMessage());
        }
    }

    /**
     * 加载转换的关系
     */
    private static void loadConfig() {
        characterToNumberRelation.put("整", "00");
        characterToNumberRelation.put("一刻", "15");
        characterToNumberRelation.put("一", "01");
        characterToNumberRelation.put("二", "02");
        characterToNumberRelation.put("两", "02");
        characterToNumberRelation.put("三", "03");
        characterToNumberRelation.put("四", "04");
        characterToNumberRelation.put("五", "05");
        characterToNumberRelation.put("六", "06");
        characterToNumberRelation.put("七", "07");
        characterToNumberRelation.put("八", "08");
        characterToNumberRelation.put("九", "09");
        characterToNumberRelation.put("十", "10");


        String[] allPatternArr = ("((\\d{1,4}|明|后|大后|大大后)年)?((\\d{1,2}|(一|二|三|四|五|六|七|八|九|十)|本|这个|下个|下下|下下个|下)月)?(\\d{1,2}|(一|二|三|四|五|六|七|八|九|十))(号|日) (本|这|这个|下|下下|下下个)?((今|明|后|大后|大大后)天|(周|星期|礼拜)(一|二|三|四|五|六|七|天|日|[1-7]))").split(" ");

        for (String patternStr : allPatternArr) {
            Pattern pattern = Pattern.compile(patternStr);
            characterPatternCache.put(patternStr, pattern);
        }


        dateCharacterToNumberRelation.put("今", 0);
        dateCharacterToNumberRelation.put("明", 1);
        dateCharacterToNumberRelation.put("后", 2);
        dateCharacterToNumberRelation.put("大后", 3);
        dateCharacterToNumberRelation.put("大大后", 4);
        dateCharacterToNumberRelation.put("本", 0);
        dateCharacterToNumberRelation.put("这个", 0);
        dateCharacterToNumberRelation.put("这", 0);
        dateCharacterToNumberRelation.put("下个", 1);
        dateCharacterToNumberRelation.put("下", 1);
        dateCharacterToNumberRelation.put("下下", 2);
        dateCharacterToNumberRelation.put("下下个", 2);
        dateCharacterToNumberRelation.put("一", 1);
        dateCharacterToNumberRelation.put("两", 2);
        dateCharacterToNumberRelation.put("二", 2);
        dateCharacterToNumberRelation.put("三", 3);
        dateCharacterToNumberRelation.put("四", 4);
        dateCharacterToNumberRelation.put("五", 5);
        dateCharacterToNumberRelation.put("六", 6);
        dateCharacterToNumberRelation.put("七", 7);
        dateCharacterToNumberRelation.put("天", 7);
        dateCharacterToNumberRelation.put("日", 7);
        dateCharacterToNumberRelation.put("八", 8);
        dateCharacterToNumberRelation.put("九", 9);
        dateCharacterToNumberRelation.put("十", 10);
        dateCharacterToNumberRelation.put("人", 1);
        dateCharacterToNumberRelation.put("求助", 1);
        dateCharacterToNumberRelation.put("服务", 2);
        dateCharacterToNumberRelation.put("需要", 1);
        dateCharacterToNumberRelation.put("monday", 1);
        dateCharacterToNumberRelation.put("tuesday", 2);
        dateCharacterToNumberRelation.put("wednesday", 3);
        dateCharacterToNumberRelation.put("thursday", 4);
        dateCharacterToNumberRelation.put("friday", 5);
        dateCharacterToNumberRelation.put("saturday", 6);
        dateCharacterToNumberRelation.put("sunday", 7);


        monthAndDayRelation.put("1", 31);
        monthAndDayRelation.put("01", 31);
        monthAndDayRelation.put("3", 31);
        monthAndDayRelation.put("03", 31);
        monthAndDayRelation.put("5", 31);
        monthAndDayRelation.put("05", 31);
        monthAndDayRelation.put("7", 31);
        monthAndDayRelation.put("07", 31);
        monthAndDayRelation.put("8", 31);
        monthAndDayRelation.put("08", 31);
        monthAndDayRelation.put("10", 31);
        monthAndDayRelation.put("12", 31);
        monthAndDayRelation.put("4", 30);
        monthAndDayRelation.put("04", 30);
        monthAndDayRelation.put("6", 30);
        monthAndDayRelation.put("06", 30);
        monthAndDayRelation.put("8", 30);
        monthAndDayRelation.put("08", 30);
        monthAndDayRelation.put("10", 30);
        monthAndDayRelation.put("11", 30);
        monthAndDayRelation.put("2", 28);
        monthAndDayRelation.put("02", 28);


    }

}
