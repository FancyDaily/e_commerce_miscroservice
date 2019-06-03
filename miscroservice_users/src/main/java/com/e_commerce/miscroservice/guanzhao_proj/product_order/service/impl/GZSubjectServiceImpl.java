package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZSubjectService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLessonVO;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.SubjectInfosVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GZSubjectServiceImpl implements GZSubjectService {

    Log log = Log.getInstance(GZSubjectServiceImpl.class);

    @Autowired
	private GZVideoDao gzVideoDao;

    @Autowired
    private GZSubjectDao gzSubjectDao;

    @Autowired
    private GZLessonDao gzLessonDao;

    @Autowired
    private GZEvaluateDao gzEvaluateDao;

    @Autowired
    private GZUserSubjectDao gzUserSubjectDao;

    @Autowired
    private GZUserLessonDao gzUserLessonDao;

    @Autowired
    private GZOrderDao gzOrderDao;

    @Autowired
    private GZVoucherDao gzVoucherDao;

    @Override
    public QueryResult subjectList(Integer pageNum, Integer pageSize) {
        log.info("在售课程列表pageNum={}, pageSize={}", pageNum, pageSize);
//        Integer availableStatus = 1;
//        gzSubjectDao.selectByAvailableStatus(availableStatus);
        Page<Object> startPage = PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 0 : pageSize);
        gzSubjectDao.selectAll();

        QueryResult result = new QueryResult();
        result.setTotalCount(startPage.getTotal());
        result.setResultList(startPage.getResult());

        return result;
    }

    @Override
    public SubjectInfosVO subjectDetail(Long subjectId) {
        return subjectDetailAuth(null, subjectId);
    }

    @Override
    public SubjectInfosVO subjectDetailAuth(Long userId, Long subjectId) {
        log.info("课程详情userId={}, subjectId={}", userId, subjectId);
        TGzSubject subject = gzSubjectDao.selectByPrimaryKey(subjectId);
        if(subject==null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该课程不存在");
        }
        // 查找过期的&使用优惠价格的订单 -> 修正
        List<TGzOrder> gzOrders1 = gzOrderDao.selectBySubjectIdAndStatus(subject.getId(), GZOrderEnum.UN_PAY.getCode());
        for(TGzOrder gzOrder:gzOrders1) {
            long timePass = System.currentTimeMillis() - gzOrder.getCreateTime().getTime();
            boolean isSalPrice = GZOrderEnum.IS_SALE_PRICE_YES.getCode().equals(gzOrder.getIsSalePrice());
            Long paySurplusTimeOriginal = AppConstant.PAY_SURPLUS_TIME_ORIGINAL;
            boolean isExpired = paySurplusTimeOriginal - timePass < 0;  //过期
            if(Objects.equals(gzOrder.getStatus(), GZOrderEnum.UN_PAY.getCode()) && isExpired && isSalPrice) {   //如果状态为待支付，并且时间失效，并且使用优惠价格
                //状态修改为超时,商品的优惠数目增加
                gzOrder.setStatus(GZOrderEnum.TIMEOUT_PAY.getCode());
                gzOrderDao.updateByPrimaryKey(gzOrder);
                Integer forSaleSurplusNum = subject.getForSaleSurplusNum();
                forSaleSurplusNum = forSaleSurplusNum==null?0:forSaleSurplusNum;
                subject.setForSaleSurplusNum(forSaleSurplusNum + 1);    //优惠数目返还
                subject.setForSaleStatus(GZSubjectEnum.FORSALE_STATUS_YES.getCode());   //设置为优惠中
                gzSubjectDao.updateByPrimaryKey(subject);
                //使用的优惠券返还(校验有效性
                Long voucherId = gzOrder.getVoucherId();
                if(voucherId!=null) {
                    TGzVoucher voucher = gzVoucherDao.selectByPrimaryKey(voucherId);
                    boolean expired = voucher.getActivationTime()+voucher.getEffectiveTime() < System.currentTimeMillis();
                    voucher.setAvailableStatus(GZVoucherEnum.STATUS_AVAILABLE.toCode());    //恢复
                    if(expired) {
                        voucher.setAvailableStatus(GZVoucherEnum.STATUS_EXPIRED.toCode());  //可用
                    }
                    gzVoucherDao.update(voucher);
                }
            }
        }

        String availableDate = subject.getAvailableDate();
        String availableTime = subject.getAvailableTime();
        availableTime = StringUtil.isEmpty(availableTime)?"0000":availableTime;
        String availableWholeTime = availableDate + availableTime;
        Long availableMills = com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil.parse(availableWholeTime);
        long currentTimeMillis = System.currentTimeMillis();
        long toAvailableMills = availableMills > currentTimeMillis ? availableMills - currentTimeMillis: -1;
        SubjectInfosVO subjectInfosVO = subject.copySubjectInfosVO();
        subjectInfosVO.setIntroPic(subject.getIntroPic());
        subjectInfosVO.setOutLinePic(subject.getOutLinePic());
        String descPic = subjectInfosVO.getDescPic();
        subjectInfosVO.setDescPicArray(descPic!=null && descPic.contains(",")? descPic.split(","):new String[1]);
        List<TGzLesson> tGzLessons = gzLessonDao.selectBySubjectId(subjectId);
        subjectInfosVO.setLessonList(tGzLessons);

        //查找我的订单
        Long createTime = null;
        if(userId != null) {
            List<TGzOrder> gzOrders = gzOrderDao.selectByUserIdAndSubjectIdAndStatusCreateTimeDesc(userId, subjectId, GZOrderEnum.UN_PAY.getCode());
            if(!gzOrders.isEmpty()) {
                TGzOrder gzOrder = gzOrders.get(0);
                gzOrder.getPrice();
                String createTimeStr = gzOrder.getCreateTime().toString();
                createTime = Long.valueOf(DateUtil.dateTimeToStamp(createTimeStr));
                Long surplusMills = 30 * 60 * 60 * 1000 - (currentTimeMillis - createTime);
                boolean expired = surplusMills < 0;
                surplusMills = expired? -1l:surplusMills;
                subjectInfosVO.setSurplusPayMills(surplusMills);
                if(expired && Objects.equals(gzOrder.getStatus(), GZOrderEnum.UN_PAY.getCode())) {  //修正
                    gzOrder.setStatus(GZOrderEnum.TIMEOUT_PAY.getCode());
                    gzOrderDao.updateByPrimaryKey(gzOrder);
                } else {
                    String tgzOrderNo = gzOrder.getTgzOrderNo();
                    subjectInfosVO.setOrderNo(tgzOrderNo);
                }
            }
        }

        if(toAvailableMills!=-1) {
            Map<String, Object> map = DateUtil.mills2DHms(toAvailableMills);
            String day = (String) map.get("day");
            String dms = (String) map.get("hms");
            subjectInfosVO.setSurplusToAvailableDayCnt(StringUtil.isEmpty(day)?0:Integer.valueOf(day));
            subjectInfosVO.setSurplusToAvailableFormatStr(dms);
        } else {
            subjectInfosVO.setSurplusToAvailableDayCnt(-1);
        }
        subjectInfosVO.setSurplusToAvailableTime(toAvailableMills);
        subjectInfosVO.setPayCreateTimeMills(createTime);

        return subjectInfosVO;
    }

    @Override
    public List<MyLessonVO> lessonList(Long subjectId) {
        log.info("章节列表subjectId={}",subjectId);
		List<TGzLesson> tGzLessons = gzLessonDao.selectBySubjectId(subjectId);
		List<MyLessonVO> myLessonVOs = new ArrayList<>();
		List<Long> lessonIds = new ArrayList<>();
		tGzLessons.forEach(
			lesson -> lessonIds.add(lesson.getId())
		);
		List<TGzVideo> videoList = gzVideoDao.selectInLessonIds(lessonIds);
		Map<Long, List<TGzVideo>> videoMap = new HashMap<>();
		videoList.forEach(
			video -> {
				Long lessonId = video.getLessonId();
				List<TGzVideo> tVideoList = videoMap.get(lessonId);
				if(tVideoList==null) {
					tVideoList = new ArrayList<>();
				}
				tVideoList.add(video);
				videoMap.put(lessonId, tVideoList);
			}
		);
		tGzLessons.forEach(
			lesson ->  {
				Long lessonId = lesson.getId();
				MyLessonVO myLessonVO = lesson.copyMyLessonVO();
				List<TGzVideo> aVideoList = videoMap.get(lessonId);
				aVideoList = aVideoList==null?new ArrayList<>():aVideoList;
				myLessonVO.setGzVideoList(aVideoList);
				myLessonVOs.add(myLessonVO);
			}
		);
		return myLessonVOs;
	}

    @Override
    public void evaluateLesson(TUser user, TGzEvaluate evaluate) {
        log.info("评价章节userId={}, evaluate={}", user.getId(), evaluate);
        Long lessonId = evaluate.getLessonId();
        Long subjectId = evaluate.getSubjectId();
        Integer level = evaluate.getLevel();
        //校验是否已经评价过
        List<TGzEvaluate> gzEvaluateList = gzEvaluateDao.selectByUserIdAndLessonId(user.getId(), lessonId);
        if(!gzEvaluateList.isEmpty()) {
            throw new MessageException("该章节您已经评价过!");
        }

        TGzEvaluate gzEvaluate = new TGzEvaluate();
        gzEvaluate.setUserId(user.getId());
        gzEvaluate.setLessonId(lessonId);
        gzEvaluate.setSubjectId(subjectId);
        gzEvaluate.setLevel(level == null? 2:level);
        gzEvaluate.setComment(evaluate.getComment());
        gzEvaluate.setCreateUser(user.getId());
        gzEvaluate.setUpdateUser(user.getId());
        gzEvaluate.setIsValid(AppConstant.IS_VALID_YES);
        gzEvaluateDao.insert(gzEvaluate);
    }

    @Override
    public void unlockSubject(Long subjectId) {
        TGzSubject subject = gzSubjectDao.selectByPrimaryKey(subjectId);
        //从开课之日期计算有效期
        Integer period = subject.getPeriod();
        period = Objects.isNull(period)?0:period;
        List<TGzUserSubject> tGzUserSubjects = gzUserSubjectDao.selectBySubjectId(subjectId);
        List<Long> userSubjectIds = new ArrayList<>();
        List<TGzUserSubject> updaters = new ArrayList<>();
        long currentTimeMillis = System.currentTimeMillis();
        for(TGzUserSubject gzUserSubject:tGzUserSubjects) {
            userSubjectIds.add(gzUserSubject.getId());
            //课程生效
            gzUserSubject.setStatus(GZUserSubjectEnum.STATUS_LEARNING.toCode());
            gzUserSubject.setExpireTime(currentTimeMillis + DateUtil.interval * period);
            updaters.add(gzUserSubject);
        }
        gzUserSubjectDao.batchUpdate(updaters,userSubjectIds);
    }

    @Override
    public QueryResult<MyLearningSubjectVO> findMyLearningSubject(Integer id, Integer pageNum, Integer pageSize) {
        log.info("查看我的正在学习id={},pageNum={},pageSize={}",id,pageNum,pageSize);
        Page<MyLearningSubjectVO> page = PageHelper.startPage(pageNum,pageSize);
        List<MyLearningSubjectVO> list = gzSubjectDao.findMyLearningSubject(id,System.currentTimeMillis());
        QueryResult<MyLearningSubjectVO> result = new QueryResult<>();
        result.setResultList(list);
        result.setTotalCount(page.getTotal());

        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void publish(TGzSubject gzSubject, Integer totalSeriesSize) {
        String subjectHeadPortraitPath = gzSubject.getSubjectHeadPortraitPath();
        String availableTime = gzSubject.getAvailableTime();
        String availableDate = gzSubject.getAvailableDate();
        Integer forSaleStatus = gzSubject.getForSaleStatus();
        forSaleStatus = forSaleStatus==null?GZSubjectEnum.FORSALE_STATUS_NO.getCode():forSaleStatus;
        gzSubject.setForSaleStatus(forSaleStatus);
        String endTime = gzSubject.getEndTime();
        String endDate = gzSubject.getEndDate();
        Long endDateTime = com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil.parse(endDate + endTime);

        if(StringUtil.isAnyEmpty(subjectHeadPortraitPath,availableDate,availableTime,endDate,endTime)) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "必填参数为空");
        }

        String reg = "[0-9]{8}";
        String regTime = "[0-9]{4}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(availableDate);
        Matcher matcher1 = pattern.matcher(endDate);
        pattern = Pattern.compile(regTime);
        Matcher matcher2 = pattern.matcher(availableTime);
        Matcher matcher3 = pattern.matcher(endTime);
        if(!matcher.matches() || !matcher1.matches() || !matcher2.matches() || !matcher3.matches()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "日期、时间格式不正确!");
        }

        Integer period = gzSubject.getPeriod();
        period = period==null?60:period;
        gzSubject.setPeriod(period);
        Double forSalePrice = gzSubject.getForSalePrice();
        Integer forSaleSurplusNum = gzSubject.getForSaleSurplusNum();
        String descPic = gzSubject.getDescPic();

        if(forSalePrice != null) {
            gzSubject.setForSaleStatus(GZSubjectEnum.FORSALE_STATUS_YES.getCode());
            forSaleSurplusNum = forSaleSurplusNum==null? GZSubjectEnum.DEFAULTSURPLUSNUM:forSaleSurplusNum;
        }

        if(descPic == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "描述图片为空!");
        }

        String name = gzSubject.getName();
        Integer seriesIndex = gzSubject.getSeriesIndex();
        List<TGzSubject> tGzSubjects = gzSubjectDao.selectByNameAndSeriesIndex(name, seriesIndex);
        for(TGzSubject tGzSubject:tGzSubjects) {
            if(Objects.equals(gzSubject.getName(), name) && (Objects.equals(gzSubject.getSeriesIndex(), seriesIndex))) {    //课程与系列号
                throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前课程、系列已存在!");
            }
        }

        //inser subject
        gzSubject.setAvaliableStatus(GZSubjectEnum.AVAILABLE_STATUS_TRUE.getCode());
        gzSubject.setCreateUser(0l);
        gzSubject.setUpdateUser(0l);
        gzSubjectDao.insert(gzSubject);
        //create default lessons
        totalSeriesSize = totalSeriesSize==null? 8:totalSeriesSize; //预设章节数
        int intervalDayCnt = 7; //默认章节间隔
        int lessonSeriesIndex = 1;
        //按照每七日解锁方案创建章节
        String lessonAvailableDate = availableDate;
        String lessonAvailableTime = availableTime;
        String totalAvailableDateTime = availableDate + availableTime;
        Long lessonAvailableMills = com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil.parse(totalAvailableDateTime);
        String totalEndDateTime = endDate + endTime;
        Long endMills = com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil.parse(totalEndDateTime);
        Long periodLong = (endMills - lessonAvailableMills) / DateUtil.interval;
        period = periodLong.intValue();

        List<TGzLesson> toInsert = new ArrayList<>();
        for(int currentIndex = 0; currentIndex < totalSeriesSize; currentIndex++,lessonSeriesIndex++) {
            TGzLesson tGzLesson = new TGzLesson();
            String souceStr = DateUtil.timeStamp2Seconds(lessonAvailableMills);
            String[] s = souceStr.split(" ");
            String sourceDate = s[0];
            String sourceTime = s[1];
            String[] sourceYMD = sourceDate.split("-");
            String year = sourceYMD[0];
            String month = sourceYMD[1];
            String day = sourceYMD[2];
            String[] sourceHMS = sourceTime.split(":");
            String hour = sourceHMS[0];
            String minute = sourceHMS[1];
            String second = sourceHMS[2];
            StringBuilder builder = new StringBuilder();
            tGzLesson.setAvailableDate(builder.append(year).append(month).append(day).toString());
            builder = new StringBuilder();
            tGzLesson.setAvailableTime(builder.append(hour).append(minute).toString());
            if(currentIndex != totalSeriesSize -1) {
                lessonAvailableMills += intervalDayCnt * DateUtil.interval;
            }
            lessonAvailableMills = lessonAvailableMills>endDateTime?endDateTime:lessonAvailableMills;
            tGzLesson.setLessonIndex(lessonSeriesIndex);
            tGzLesson.setAvaliableStatus(GZLessonEnum.AVAILABLE_STATUS_NO.getCode());
            tGzLesson.setSubjectId(gzSubject.getId());
            tGzLesson.setCreateUser(gzSubject.getCreateUser());
            tGzLesson.setUpdateUser(gzSubject.getUpdateUser());
            tGzLesson.setIsValid(AppConstant.IS_VALID_YES);
            tGzLesson.setName(gzSubject.getName() + "0" + lessonSeriesIndex);
            toInsert.add(tGzLesson);
        }
        gzLessonDao.insert(toInsert);
    }

    @Override
    public QueryResult<MyLearningSubjectVO> findEndingSubject(Integer id, Integer pageNum, Integer pageSize) {
        log.info("查看我的已结束课程id={},pageNum={},pageSize={}",id,pageNum,pageSize);

        Page<MyLearningSubjectVO> page = PageHelper.startPage(pageNum,pageSize);
        List<MyLearningSubjectVO> list = gzSubjectDao.findEndingSubject(id);

        QueryResult<MyLearningSubjectVO> result = new QueryResult<>();
        result.setResultList(list);
        result.setTotalCount(page.getTotal());

        return result;
    }

	@Override
	public Map<String, Object> videoList(Long userId, Long lessonId) {
    	TGzUserLesson userLesson = gzUserLessonDao.selectByUserIdAndLessonId(userId, lessonId);
    	if(userLesson == null) {
    		throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "对不起,您没有权限查看该章节!");
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("userLesson", userLesson);
		List<TGzVideo> videos =  gzVideoDao.selectByLessonId(lessonId);
		resultMap.put("videos", videos);
		return resultMap;
	}


}
