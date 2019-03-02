package com.e_commerce.miscroservice.user.entity.support;

import com.beust.jcommander.internal.Lists;
import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/17 6:32
 * @Copyright 玖远网络
 */
@Data
public class ValidTimeQueueHelper{

    static Logger logger = LoggerFactory.getLogger(ValidTimeQueueHelper.class);

    /**
     * 创建对象
     * @return com.store.service.MemberService.MemberValidTimeQueue
     * @author Charlie
     * @date 2018/8/16 11:11
     */
    public static ValidTimeQueueHelper instance(Member member) {
        return new ValidTimeQueueHelper (member.getId (), member.getValidTimeQueue (), member.getType (), member.getEndTime ());
    }

    private ValidTimeQueueHelper(Long memberId, String validTimeQueue, Integer memberPackageType, Long endTime) {
        this.memberId = memberId;
        this.validTimeQueue = validTimeQueue;
        this.memberPackageType = memberPackageType;
        this.endTime = endTime;
    }

    private Long memberId;
    /**
     * 有效期
     */
    private String validTimeQueue;
    /**
     * 套餐类型
     */
    private Integer memberPackageType;
    /**
     * 结束时间
     */
    private Long endTime;

    /**
     *
     * @param memberPackageType 套餐类型
     * @param timeUnit 时间单位 1年2月3日4时5分
     * @param value 时间单位
     * @param higherType
     * @return com.yujj.entity.product.ValidTimeQueueHelper
     * @author Charlie
     * @date 2018/9/26 11:29
     */
    public ValidTimeQueueHelper add(Integer memberPackageType, int timeUnit, Integer value, Integer higherType) {
        //加之前先过滤一下有历史数据
        clearExpirationTime ();
        if (endTime == 0) {
            //过期会员
            Calendar calendar = addTime (Calendar.getInstance (), timeUnit, value);
            this.endTime = calendar.getTimeInMillis ();
            validTimeQueue = buildValidTimeQueue (endTime, memberPackageType);
            this.memberPackageType = memberPackageType;
        }
        else {
            //会员续费
            if (this.memberPackageType == 4) {
                //如果是0元续费, 直接覆盖, 不记录以前的状态
                Calendar calendar = addTime (Calendar.getInstance (), timeUnit, value);
                this.endTime = calendar.getTimeInMillis ();
                validTimeQueue = buildValidTimeQueue (endTime, memberPackageType);
                this.memberPackageType = memberPackageType;
                return this;
            }
            //非0元续费

//            int start = validTimeQueue.lastIndexOf ("[") + 1;
//            int end = validTimeQueue.lastIndexOf ("]");
//            String lastEndTimeInfo = validTimeQueue.substring (start, end);
//            String[] timeSplit = lastEndTimeInfo.split (":");
//            long lastEndTime = Long.parseLong (timeSplit[0]);
//            int memberType = Integer.parseInt (timeSplit[1]);
            //@_@ 我当初怎么就写出了这么个存储格式....
//            if (memberPackageType.equals(memberType)) {
//                logger.info("相同套餐续费,结束时间累加");
//                Calendar calendar = Calendar.getInstance ();
//                calendar.setTimeInMillis (lastEndTime);
//                calendar = addTime (calendar, timeUnit, value);
//                long finalTime = calendar.getTimeInMillis();
//
//                this.endTime = finalTime;
//                validTimeQueue = buildValidTimeQueue (finalTime, memberPackageType);
//            }
//            else {
//                if (memberType-higherType == 0) {
//                    logger.info("续费低级套餐,不改变当前使用套餐类型");
//                    Calendar calendar = Calendar.getInstance ();
//                    calendar.setTimeInMillis (lastEndTime);
//                    calendar = addTime (calendar, timeUnit, value);
//                    long finalTime = calendar.getTimeInMillis();
//
//                    validTimeQueue += buildValidTimeQueue (finalTime, memberPackageType);
//                }
//                else {
//                    logger.info("续费高级套餐,当前使用套餐换成高级套餐");
//                    long lowTypeRemainTime = lastEndTime - System.currentTimeMillis();
//                    Calendar calendar = Calendar.getInstance ();
//                    calendar = addTime (calendar, timeUnit, value);
//                    long newTypeEndTime = calendar.getTimeInMillis();
//                    validTimeQueue = buildValidTimeQueue(newTypeEndTime, memberPackageType);
//
//                    validTimeQueue += buildValidTimeQueue(newTypeEndTime + lowTypeRemainTime, memberType);
//
//                    this.endTime = newTypeEndTime;
//                    this.memberPackageType = memberPackageType;
//                }
//            }
            List<MemberStatus> memberStatusList = parseValidQueue(validTimeQueue);
            if (! memberStatusList.isEmpty()) {
                MemberStatus currentMemberStatus = memberStatusList.get(0);
                //当前用户的会员状态
                Long currentEndTime = currentMemberStatus.getEndTime();
                Integer currentType = currentMemberStatus.getType();

                if (memberPackageType.equals(currentType)) {
                    logger.info("相同套餐续费,每个结束时间都往后延迟");
                    memberStatusList.stream().forEach(status->{
                        Calendar calendar = Calendar.getInstance ();
                        calendar.setTimeInMillis (status.getEndTime());
                        calendar = addTime (calendar, timeUnit, value);
                        status.setEndTime(calendar.getTimeInMillis());
                    });
                }
                else {
                    if (currentType-higherType == 0) {
                        logger.info("续费低级套餐,不改变当前使用套餐类型");
                        //先排序到第二位,暂时处理
                        for (int i = 1; i < memberStatusList.size(); i++) {
                            MemberStatus status = memberStatusList.get(i);
                            Calendar calendar = Calendar.getInstance ();
                            calendar.setTimeInMillis (status.getEndTime());
                            calendar = addTime (calendar, timeUnit, value);
                            status.setEndTime(calendar.getTimeInMillis());
                        }
                        Calendar calendar = Calendar.getInstance ();
                        calendar.setTimeInMillis (currentEndTime);
                        calendar = addTime (calendar, timeUnit, value);
                        MemberStatus second = new MemberStatus(memberPackageType, calendar.getTimeInMillis());
                        memberStatusList.add(1, second);
                    }
                    else {
                        logger.info("续费高级套餐,当前使用套餐换成高级套餐");
                        memberStatusList.stream().forEach(status->{
                            Calendar calendar = Calendar.getInstance ();
                            calendar.setTimeInMillis (status.getEndTime());
                            calendar = addTime (calendar, timeUnit, value);
                            status.setEndTime(calendar.getTimeInMillis());
                        });

                        Calendar calendar = Calendar.getInstance ();
                        calendar = addTime (calendar, timeUnit, value);
                        MemberStatus first = new MemberStatus(memberPackageType, calendar.getTimeInMillis());
                        memberStatusList.add(0, first);
                    }
                }
                validTimeQueue = memberStatusToString(memberStatusList);
                this.endTime = memberStatusList.get(0).getEndTime();
                this.memberPackageType = memberStatusList.get(0).getType();
            }
        }
        return this;
    }

    public static String memberStatusToString(List<MemberStatus> memberStatusList) {
        StringBuilder builder = new StringBuilder();
        for (MemberStatus memberStatus : memberStatusList) {
            builder.append(memberStatus.str());
        }
        return builder.toString();
    }

    /**
     * 追加时间
     *
     * @param timeUnit 时间单位
     * @param value 时间长度
     * @return java.util.Calendar
     * @author Charlie
     * @date 2018/9/26 11:39
     */
    public static Calendar addTime(Calendar calendar, int timeUnit, Integer value) {
        int calendarTimeUnit ;
        switch (timeUnit) {
            case 1 :
                calendarTimeUnit = Calendar.YEAR;break;
            case 2 :
                calendarTimeUnit = Calendar.MONTH;break;
            case 3 :
                calendarTimeUnit = Calendar.DATE;break;
            case 4 :
                calendarTimeUnit = Calendar.HOUR_OF_DAY;break;
            case 5 :
                calendarTimeUnit = Calendar.MINUTE;break;
                default:
                    calendarTimeUnit = Calendar.YEAR;
                    logger.error ("未知的日期配置 timeUnit:{}",timeUnit);
        }
        calendar.add (calendarTimeUnit, value);
        return calendar;
    }


    /**
     * 清除过期时间, 没有则不处理
     *
     * @author Charlie
     * @date 2018/8/16 11:32
     */
    public void clearExpirationTime() {
        if (! isHasDirtyEndTime ()) {
            //没有脏数据
            return;
        }

        //有脏数据待处理
        String expireData = buildValidTimeQueue(this.endTime, this.memberPackageType);
        if (! validTimeQueue.contains (expireData)) {
            logger.error ("理用户会员账号过期时间异常,数据不匹配 memberId[{}].validTimeQueue[{}].endTime[{}].memberPackageType[{}]", memberId, validTimeQueue, endTime, memberId);
            throw new RuntimeException ("处理用户会员账号过期时间异常,数据不匹配 endTime:"+endTime+",validTimeQueue"+validTimeQueue+",memberId:"+memberId);
        }
        validTimeQueue = validTimeQueue.substring (expireData.length (), validTimeQueue.length ());
        if (validTimeQueue.length () > 0) {
            //新的时间
            String next =validTimeQueue .substring (validTimeQueue.indexOf ("[")+1, validTimeQueue.indexOf ("]"));
            String[] val = next.split (":");
            this.endTime = Long.parseLong (val[0]);
            this.memberPackageType = Integer.parseInt (val[1]);
        }
        else {
            //没有续费的服务
            this.endTime = 0L;
        }
    }



    /**
     * 是否有脏数据
     *
     * @return true 是
     * @author Charlie
     * @date 2018/8/16 11:38
     */
    public boolean isHasDirtyEndTime() {
        boolean hasDirtyEndTime = true;
        if (this.endTime > System.currentTimeMillis ()) {
            //没有过期数据
            hasDirtyEndTime = false;
        }
        if (this.endTime == 0) {
            //过期数据已被处理
            hasDirtyEndTime = false;
        }
        return hasDirtyEndTime;
    }


    public static String buildValidTimeQueue(Long endTime, Integer memberPackageType) {
        return new StringBuilder().append("[").append(String.valueOf(endTime)).append(":").append(String.valueOf(memberPackageType)).append("]").toString();
    }

    @Data
    static class MemberStatus {
        private Integer type;
        private Long endTime;

        public MemberStatus(Integer type, Long endTime) {
            this.type = type;
            this.endTime = endTime;
        }

        public String str() {
            String res;
            if (type != null && endTime != null) {
                res = buildValidTimeQueue(endTime, type);
            }
            else {
                res = "";
            }
            return res;
        }
    }




    public static List<MemberStatus> parseValidQueue(String str) {
        List<MemberStatus> vDateList = Lists.newArrayList(2);
        if (StringUtils.isNotBlank(str)) {
            int offset = str.indexOf("[") + 1;
            int end = str.indexOf("]", 1);
            for ( ;offset >0 && end > 0; offset = str.indexOf("[", offset) + 1, end = str.indexOf("]", end+1)) {
                String typeWithTime = str.substring(offset, end);
                if (StringUtils.isNotBlank(typeWithTime)) {
                    String[] typeWithTimeArr = typeWithTime.split(":");
                    if (typeWithTimeArr.length != 2) {
                        throw ErrorHelper.me("数据格式错误 " + str);
                    }
                    long time = Long.parseLong(typeWithTimeArr[0]);
                    int type = Integer.parseInt(typeWithTimeArr[1]);
                    MemberStatus memberStatus = new MemberStatus(type, time);
                    vDateList.add(memberStatus);
                }
            }
        }
        return vDateList;
    }
}

