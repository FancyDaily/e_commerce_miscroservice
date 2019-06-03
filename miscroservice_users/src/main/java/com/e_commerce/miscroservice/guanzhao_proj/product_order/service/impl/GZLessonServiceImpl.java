package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.config.colligate.MqTemplate;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLessonVO;
import com.e_commerce.miscroservice.push_controller.FileUrlManagers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 09:22
 */
@Service
public class GZLessonServiceImpl implements GZLessonService {

    private Log log = Log.getInstance(GZOrderServiceImpl.class);

	@Autowired
	private GzUserVideoDao gzUserVideoDao;

    @Autowired
    private GZLessonDao gzLessonDao;

    @Autowired
    private GZSubjectDao gzSubjectDao;

    @Autowired
    private GZUserSubjectDao gzUserSubjectDao;

    @Autowired
    private GZUserLessonDao gzUserLessonDao;

    @Autowired
    private GZKeyValueDao keyValueDao;

    @Autowired
    private GZEvaluateDao gzEvaluateDao;

    @Autowired
	private GZVideoDao gzVideoDao;

    @Autowired
    @Lazy
    MqTemplate mqTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FileUrlManagers fileUrlManagers;

	@Autowired
	private RedisUtil redisUtil;

    @Override
    public void updateLearningCompletion(Long lessonId) {
        TGzLesson lesson = gzLessonDao.selectByPrimaryKey(lessonId);
    }

    @Override
    public void unlockMyLesson(Long userId, Long subjectId) {
        //找到所有购买此课程的用户 -> 写入sign
        List<TGzUserLesson> toUpdater = new ArrayList<>();
        List<Long> toUpdaterIds = new ArrayList<>();
        List<TGzKeyValue> keyValueToInserter = new ArrayList<>();
        //user-lesson关系
//        List<TGzUserLesson> userLessons = gzUserLessonDao.selectBySubjectId(subjectId);
        List<TGzUserLesson> userLessons = gzUserLessonDao.selectByUserIdAndSubjectId(userId, subjectId);
        for (TGzUserLesson userLesson : userLessons) {
            if(!StringUtil.isEmpty(userLesson.getSign())) { //若已有签名
                continue;
            }
            Long lessonId = userLesson.getLessonId();
            TGzLesson tGzLesson = gzLessonDao.selectByPrimaryKey(lessonId);
            if(!Objects.equals(tGzLesson.getVideoOnLoadStatus(), GZLessonEnum.VIDEO_STATUS_AVAILABLE_YES.getCode())) {  //若未装载
                continue;
            }
            //生成sign，插入key-gzvalue
//            String sourceStr = userId.toString() + lessonId.toString() + subjectId.toString();
            String sourceStr = userId + lessonId.toString() + subjectId.toString();
            String sign = Md5Util.md5(sourceStr);
            TGzKeyValue keyValue = new TGzKeyValue();
            keyValue.setType(KeyValueEnum.TYPE_SIGN.toCode());
            keyValue.setGzkey(userId.toString());
            keyValue.setGzvalue(sign);
            keyValueToInserter.add(keyValue);
            userLesson.setStatus(GZUserLessonEnum.STATUS_AVAILABLE.getCode());
            userLesson.setSign(sign);
            toUpdaterIds.add(userLesson.getId());
            toUpdater.add(userLesson);
        }
        gzUserLessonDao.batchUpdate(toUpdater, toUpdaterIds);
        keyValueDao.batchInsert(keyValueToInserter);
    }

	/**
	 * 更新视频加载状态
	 * @param subjectId
	 * @param lessonId
	 * @param fileName
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Override
    public void unlockLesson(Long subjectId, Long lessonId, String fileName) {
		//校验课程是否存在
		TGzSubject subject = gzSubjectDao.selectByPrimaryKey(subjectId);
		if (Objects.isNull(subject)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "找不到该课程！请确认课程编号");
		}

		//检测章节是否存在
        TGzLesson tGzLesson = gzLessonDao.selectByPrimaryKey(lessonId);
        if (Objects.isNull(tGzLesson)) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "找不到该章节！请确认章节编号");
        }

		//检测lessonId是否为subjectId下
		TGzLesson tGzLesson1 = gzLessonDao.selectBySubjectIdAndLessonId(subjectId, lessonId);
		if(tGzLesson1==null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该课程下没有该章节!");
		}

        //创建视频实体
		String displayName = fileName;
		if (fileName.contains(".")) {
			displayName = fileName.substring(0, fileName.lastIndexOf("."));	//去掉扩展名
		}
		List<TGzVideo> videoList = gzVideoDao.selectBySubjectIdAndLessonIdAndFileName(subjectId, lessonId, fileName);
		Long videoId = null;
		if(videoList.isEmpty()) {
			//确定当前序号
			TGzVideo gzVideo1 = gzVideoDao.selectOneBySubjectIdAndLessonIdIndexDesc(subjectId, lessonId);
			gzVideo1 = gzVideo1==null? new TGzVideo():gzVideo1;
			Integer index = gzVideo1.getVideoIndex();
			index = index==null? -1:index;
			Integer indexNow = ++index;
			TGzVideo gzVideo = TGzVideo.builder().subjectId(subjectId).lessonId(lessonId).fileName(fileName).name(displayName).videoIndex(indexNow).build();
			gzVideoDao.insert(gzVideo);
			videoId = gzVideo.getId();
		}

      /*  //解锁开始立即解锁
        Long lessonId = tGzLesson.getId();*/
/*
        //
        TimerScheduler scheduler = new TimerScheduler();
        scheduler.setType(TimerSchedulerTypeEnum.ORDER_OVERTIME_END.toNum());
        scheduler.setName("lower_order" + UUID.randomUUID().toString());
        scheduler.setCron(DateUtil.genCron(tGzLesson.getAvailableDate() + "" + tGzLesson.getAvailableTime()));
        Map<String, String> map = new HashMap<>();
        map.put("subjectId", String.valueOf(subjectId));
        map.put("lessonId", String.valueOf(lessonId));
        scheduler.setParams(JSON.toJSONString(map));

        mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_SEND.toName(), JSONObject.toJSONString(scheduler));
*/
		//只要上传了一个视频，便更新这章节的视频加载状态为已加载
		if(GZLessonEnum.VIDEO_STATUS_AVAILABLE_YES.getCode() != tGzLesson.getVideoOnLoadStatus()) {
        	tGzLesson.setVideoOnLoadStatus(GZLessonEnum.VIDEO_STATUS_AVAILABLE_YES.getCode());
		}
        gzLessonDao.updateByPrimaryKey(tGzLesson);

		//找到所有购买此课程的用户 -> 写入sign
        List<TGzUserSubject> tGzUserSubjects = gzUserSubjectDao.selectBySubjectId(subjectId);
		List<TGzUserLesson> toUpdater = new ArrayList<>();
        List<Long> toUpdaterIds = new ArrayList<>();
		for (TGzUserSubject tGzUserSubject : tGzUserSubjects) {
			Long userId = tGzUserSubject.getUserId();
			//生成sign，插入key-gzvalue
			String sourceStr = userId.toString() + lessonId.toString() + subjectId.toString();
			String sign = Md5Util.md5(sourceStr);
            TGzKeyValue keyValue = new TGzKeyValue();
            keyValue.setType(KeyValueEnum.TYPE_SIGN.toCode());
            keyValue.setGzkey(userId.toString());
            keyValue.setGzvalue(sign);
            keyValueDao.insert(keyValue);

            //user-lesson关系
            TGzUserLesson userLesson = gzUserLessonDao.selectByUserIdAndLessonId(userId, lessonId);
            if (userLesson != null) {
                userLesson.setStatus(GZUserLessonEnum.STATUS_AVAILABLE.getCode());
                userLesson.setSign(sign);
                toUpdaterIds.add(userLesson.getId());
                toUpdater.add(userLesson);
            }

			//插入userVideo记录
			TGzUserVideo userVideo = TGzUserVideo.builder().subjectId(subjectId).lessonId(lessonId).userId(userId).videoId(videoId).build();
			gzUserVideoDao.insert(userVideo);
        }

        try {
            if (!toUpdater.isEmpty()) {
                gzUserLessonDao.batchUpdate(toUpdater, toUpdaterIds);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void authCheck(String sign, Long userId, Long subjectId, Long lessonId) {
        //章节开放
        TGzLesson tGzLesson = gzLessonDao.selectByPrimaryKey(lessonId);
        MessageException messageException = new MessageException("当前章节未开放!");
        boolean flag;
        if (Objects.isNull(tGzLesson) || !Objects.equals(tGzLesson.getVideoOnLoadStatus(), GZLessonEnum.VIDEO_STATUS_AVAILABLE_YES.getCode())) {
            throw messageException;
        }
        String availableDate = tGzLesson.getAvailableDate();
        String availableTime = tGzLesson.getAvailableTime();
        String wholeDateTime = availableDate + availableTime;
        Long availableStamp = Long.valueOf(DateUtil.dateTimeToStamp(wholeDateTime));
        flag = System.currentTimeMillis() < availableStamp; //未到章节开放时间

        if (flag) {
            throw messageException;
        }

        if(StringUtil.isEmpty(sign)) {
            throw messageException;
        }

        String sourceStr = userId + "" + lessonId + "" + subjectId;
        String expectedSign = Md5Util.md5(sourceStr);
        messageException = new MessageException("对不起，你没有权限观看!");
        //校验sign
        if (!Objects.equals(expectedSign, sign)) {
            throw messageException;
        }
        //校验user
        TGzKeyValue keyValue = keyValueDao.selectByTypeAndValue(KeyValueEnum.TYPE_SIGN.toCode(), sign);
        if (!Objects.equals(userId + "", keyValue.getGzkey())) {
            throw messageException;
        }
        //校验课程有效权限
        TGzUserSubject userSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, subjectId);
        if (Objects.isNull(userSubject) || System.currentTimeMillis() > userSubject.getExpireTime()) {
            throw messageException;
        }


    }

    @Override
    public void sendUnlockTask(Long subjectId, Long lessonId, String fileName) {
        unlockLesson(subjectId, lessonId, fileName);
    }

    @Override
    public QueryResult mySubjectLessonList(Long userId, Long subjectId, Integer pageNum, Integer pageSize) {
        log.info("我的课程章节学习(正在播放)userId={}, subjectId={}, pageNum={}, pageSize={}", userId, subjectId, pageNum, pageSize);
        //校验开课
        TGzSubject gzSubject = gzSubjectDao.selectByPrimaryKey(subjectId);

        Integer avaliableStatus = gzSubject.getAvaliableStatus();
        String dateTime = gzSubject.getAvailableDate() + gzSubject.getAvailableTime();
        Long expectedStamp = Long.valueOf(DateUtil.dateTimeToStamp(dateTime));
        long currentTimeMillis = System.currentTimeMillis();
        boolean statusAvailableFlag = !Objects.equals(avaliableStatus, GZSubjectEnum.AVAILABLE_STATUS_TRUE.getCode());
        boolean timeAvailableFlag = currentTimeMillis > expectedStamp;
        if (timeAvailableFlag && !statusAvailableFlag) {    //如果到时间、状态错误，修正（仅修正课程状态）
            gzSubject.setAvaliableStatus(GZSubjectEnum.AVAILABLE_STATUS_TRUE.getCode());
            gzSubjectDao.updateByPrimaryKey(gzSubject);
        }
        //我的课程记录
        TGzUserSubject tGzUserSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, subjectId);
        if (Objects.isNull(tGzUserSubject) || System.currentTimeMillis() > tGzUserSubject.getExpireTime()) {    //TODO 失效是否可以看
            throw new MessageException("对不起, 您没有权限查看该课程");
        }

        //匹配查找我的章节状态
        List<MyLessonVO> myLessonVOS = new ArrayList<>();
        Page<Object> startPage = PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 0 : pageSize);
        List<TGzLesson> tGzLessons = gzLessonDao.selectBySubjectId(subjectId);

        List<TGzUserLesson> gzUserLessonList = gzUserLessonDao.selectByUserIdAndSubjectId(userId, subjectId);
        Map<Long, Object> lessonIdUserLessonMap = new HashMap<>();
        for (TGzUserLesson gzUserLesson : gzUserLessonList) {
            lessonIdUserLessonMap.put(gzUserLesson.getLessonId(), gzUserLesson);
        }

        //构建一个章节-视频集Map
		Map<Long, List<TGzVideo>> lessonVideoMap = new HashMap<>();
		List<TGzVideo> tGzVideos = gzVideoDao.selectBySubjectId(subjectId);
		tGzVideos.stream().forEach(a -> {
			Long lessonId = a.getLessonId();
			List<TGzVideo> videos = lessonVideoMap.get(lessonId);
			if(videos==null) {
				videos = new ArrayList<>();
			}
			videos.add(a);
			lessonVideoMap.put(lessonId, videos);
		});

		List<TGzUserVideo> gzUserVideos = gzUserVideoDao.selectByUserIdAndSubjectId(userId, subjectId);
		Map<Long, TGzUserVideo> userVideoMap = new HashMap<>();
		gzUserVideos.forEach(
			a -> userVideoMap.put(a.getVideoId(), a)
		);
		for (TGzLesson tGzLesson : tGzLessons) {
			Long lessonId = tGzLesson.getId();
			MyLessonVO myLessonVO = tGzLesson.copyMyLessonVO();
            Object existObject = lessonIdUserLessonMap.get(lessonId);
            Integer videoOnLoadStatus = tGzLesson.getVideoOnLoadStatus();
            Integer videoCompletion = 0;
            Integer lessonCompletionStatus = GZUserLessonEnum.LESSON_COMPLETION_STATUS_NO.getCode();
            String sign = null;
            if (existObject != null) {
                TGzUserLesson userLesson = (TGzUserLesson) existObject;
                videoCompletion = userLesson.getVideoCompletion();
                lessonCompletionStatus = userLesson.getLessonCompletionStatus();
                sign = userLesson.getSign();
                if(!StringUtil.isEmpty(sign)) { //有权限播放 TODO 测试
                    String suffix = ".mp4";
                    String fileName = tGzLesson.getName();
                    fileName += suffix;
					List<TGzVideo> videoList = gzVideoDao.selectByLessonId(lessonId);
					for(TGzVideo video:videoList) {
						fileName = video.getFileName();
						String url = fileUrlManagers.getUrl(fileName);
					}

					/*Object o = redisUtil.get(key);
					if (o==null || System.currentTimeMillis() > (long)o) {
						redisUtil.set(key, System.currentTimeMillis() + 1000 * 60 * 8, 1000 * 60 * 8);
					}*/
//                    myLessonVO.setUrl(url);
                }
            }
            final String signValue = sign;
            myLessonVO.setSign(sign);
            myLessonVO.setLessonIndex(tGzLesson.getLessonIndex());
            String availableDate = tGzLesson.getAvailableDate();
            String availableTime = tGzLesson.getAvailableTime();
            String wholeDateTime = availableDate + availableTime;
            Long dateTimeMills = com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil.parse(wholeDateTime);
            Integer lessonAvailableStatus = System.currentTimeMillis() > dateTimeMills ? GZLessonEnum.AVAILABLE_STATUS_YES.getCode() : GZLessonEnum.AVAILABLE_STATUS_NO.getCode();  //只计算值，不维护available_status字段
            myLessonVO.setAvaliableStatus(lessonAvailableStatus);
            myLessonVO.setAvailableDate(availableDate);
            myLessonVO.setAvailableTime(availableTime);
            myLessonVO.setVideoCompletion(videoCompletion);
            myLessonVO.setLessonCompletionStatus(lessonCompletionStatus);
            myLessonVO.setLessonId(lessonId);
            myLessonVO.setVideoOnloadStatus(videoOnLoadStatus);   //视频可播放状态
            myLessonVO.setUserId(userId);
            //获取视频集合
			List<TGzVideo> videoList = lessonVideoMap.get(lessonId);
			List<TGzVideo> resultVideoList = new ArrayList<>();
			if(videoList==null) {
				videoList = new ArrayList<>();
			}
			videoList.stream().forEach(a -> {
				TGzUserVideo tGzUserVideo = userVideoMap.get(a.getId());
				int completeStatus = GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_NO.getCode();
				int completion = 0;
				if(tGzUserVideo!=null) {
					completion = tGzUserVideo.getVideoCompletion();
				}
				completeStatus = completion==100?GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode():completeStatus;
				a.setVideoCompletion(completion);
				a.setVideoCompletionStatus(completeStatus);
				a.setSign(signValue);
				resultVideoList.add(a);
			});
			myLessonVO.setGzVideoList(resultVideoList);
			myLessonVOS.add(myLessonVO);
        }

        QueryResult result = new QueryResult();
        result.setTotalCount(startPage.getTotal());
        result.setResultList(myLessonVOS);

        return result;
    }




    @Override
    public void updateVideoCompletion(Long userId, Long lessonId, Long videoId, Integer currentSeconds, Integer totalSeconds) {
        log.info("视频进度更新userId={}, lessonId={}, currentSeconds={}, totalSeconds={}", userId, lessonId, currentSeconds, totalSeconds);
		TGzUserLesson tGzUserLesson = gzUserLessonDao.selectByUserIdAndLessonId(userId, lessonId);
		if (tGzUserLesson == null) {
			return;
		}
		TGzUserVideo gzUserVideo = gzUserVideoDao.selectByVideoIdAndUserId(videoId, userId);
		if(gzUserVideo == null || gzUserVideo.getVideoCompletion() == 100) {
			return;
		}
		currentSeconds = currentSeconds == null ? 0 : currentSeconds;
        totalSeconds = totalSeconds == null ? 0 : totalSeconds;
        double currentDouble = currentSeconds;
        double totalDouble = totalSeconds;
        Integer completion = (int) (currentDouble / totalDouble * 100);

        String key = userId.toString() + lessonId;
        //校验该进度是否已经更新
        Integer attribute = (Integer) request.getSession().getAttribute(key);
        if (attribute != null && !attribute.equals(completion)) {
            return;
        }

        request.getSession().setAttribute(key, completion);

        //校验
        completion = completion > 100 ? 100 : completion;
        //视频完成进度更新
        Integer videoCompletionStatus = gzUserVideo.getVideoCompletionStatus();
        int videoExpectedCompletionStatus = completion > 90 ? GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode() : GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_NO.getCode();
		videoCompletionStatus = Objects.equals(videoCompletionStatus, GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode()) ? videoCompletionStatus : videoExpectedCompletionStatus;

		gzUserVideo.setVideoCompletion(completion);
		gzUserVideo.setVideoCompletionStatus(videoCompletionStatus);
		gzUserVideoDao.update(gzUserVideo);
		//统计视频进度，更新章节学习进度
		completion = tGzUserLesson.getVideoCompletion();
		int cnt = 0;
		if(completion != 100) {
			List<TGzVideo> gzVideos = gzVideoDao.selectByLessonId(lessonId);
			int total = gzVideos.size();
			List<TGzUserVideo> gzUserVideos = gzUserVideoDao.selectByUserIdAndLessonId(userId, lessonId);
			Long count = gzUserVideos.stream().filter(a -> a.getVideoCompletion() == 100)
				.count();
			cnt = count.intValue();
			/*for (TGzVideo video : gzVideos) {
				Integer videoCompletion = video.getVideoCompletion();
				if(videoCompletion == 100) {
					cnt++;
				}
			}
*/
			double i = (double)cnt / total;
			int result = (int)(i * 100);

			Integer comletionStatus = tGzUserLesson.getLessonCompletionStatus();
			int expectedCompletionStatus = completion == 90 ? GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode() : GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_NO.getCode();
			comletionStatus = Objects.equals(comletionStatus, GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode()) ? comletionStatus : expectedCompletionStatus;
			tGzUserLesson.setVideoCompletion(result);
			tGzUserLesson.setVideoCompletionStatus(comletionStatus);
			tGzUserLesson.setLessonCompletionStatus(comletionStatus);
			gzUserLessonDao.update(tGzUserLesson);
		}
        //统计章节进度，更新课程学习进度
        Long subjectId = tGzUserLesson.getSubjectId();
        Integer completeCnt = 0;
        Integer totalCnt = 0;
        List<TGzLesson> tGzLessons = gzLessonDao.selectBySubjectId(subjectId);
        totalCnt = tGzLessons.size();
        List<TGzUserLesson> gzUserLessonList = gzUserLessonDao.selectByUserIdAndSubjectId(userId, subjectId);
        for (TGzUserLesson gzUserLesson : gzUserLessonList) {
            if (Objects.equals(gzUserLesson.getLessonCompletionStatus(), GZUserLessonEnum.LESSON_COMPLETION_STATUS_DONE_YES.getCode())
				|| gzUserLesson.getVideoCompletion() == 100) {
                completeCnt++;
            }
        }
        double i = (double)completeCnt / totalCnt;
        int currentComp = (int) (i * 100);
        Integer subjectCompletion = totalCnt == 0 ? 0 : currentComp;

        TGzUserSubject tGzUserSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, subjectId);
        tGzUserSubject.setCompletion(subjectCompletion);
        gzUserSubjectDao.updateByPrimaryKey(tGzUserSubject);
    }


    @Override
    public QueryResult<TGzEvaluate> lessonEvaluateList(Long subjectId, Long lessonId, Integer pageNum, Integer
            pageSize) {
        log.info("课程评价列表subjectId={}, lessonId={}, pageNum={}, pageSize={}", subjectId, lessonId, pageNum, pageSize);
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 0 : pageSize;

        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TGzEvaluate> gzEvaluates = gzEvaluateDao.selectByLessonId(lessonId);
        QueryResult queryResult = new QueryResult();
        queryResult.setTotalCount(startPage.getTotal());
        queryResult.setResultList(gzEvaluates);
        return queryResult;
    }

}
