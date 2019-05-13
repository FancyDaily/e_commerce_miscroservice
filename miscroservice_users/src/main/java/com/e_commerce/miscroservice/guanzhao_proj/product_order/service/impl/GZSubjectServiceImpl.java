package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.GZUserSubjectEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZSubjectService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.SubjectInfosVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GZSubjectServiceImpl implements GZSubjectService {

    Log log = Log.getInstance(GZSubjectServiceImpl.class);

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

    @Override
    public QueryResult subjectList(Integer pageNum, Integer pageSize) {
        Integer availableStatus = 1;
        Page<Object> startPage = PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 0 : pageSize);
        gzSubjectDao.selectByAvailableStatus(availableStatus);

        QueryResult result = new QueryResult();
        result.setTotalCount(startPage.getTotal());
        result.setResultList(startPage.getResult());

        return result;
    }

    @Override
    public SubjectInfosVO subjectDetail(Long subjectId) {
        TGzSubject subject = gzSubjectDao.selectByPrimaryKey(subjectId);
        if(subject==null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该课程不存在");
        }
        SubjectInfosVO subjectInfosVO = subject.copySubjectInfosVO();
        String descPic = subjectInfosVO.getDescPic();
        subjectInfosVO.setDescPicArray(descPic!=null && descPic.contains(",")? descPic.split(","):new String[1]);
        List<TGzLesson> tGzLessons = gzLessonDao.selectBySubjectId(subjectId);
        subjectInfosVO.setLessonList(tGzLessons);
        return subjectInfosVO;
    }

    @Override
    public List<TGzLesson> lessonList(Long subjectId) {
        return gzLessonDao.selectBySubjectId(subjectId);
    }

    @Override
    public void evaluateLesson(TUser user, TGzEvaluate evaluate) {
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
        List<MyLearningSubjectVO> list = gzSubjectDao.findMyLearningSubject(id);
        QueryResult<MyLearningSubjectVO> result = new QueryResult<>();
        result.setResultList(list);
        result.setTotalCount(page.getTotal());

        return result;
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


}
