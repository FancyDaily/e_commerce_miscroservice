package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.GZUserSubjectEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GZSubjectServiceImpl implements GZSubjectService {

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
    public List<TGzSubject> subjectList() {
        Integer availableStatus = 1;
        return gzSubjectDao.selectByAvailableStatus(availableStatus);
    }

    @Override
    public TGzSubject subjectDetail(Long subjectId) {
        return gzSubjectDao.selectByPrimaryKey(subjectId);
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
            throw new MessageException("该课程您已经评价过!");
        }

        TGzEvaluate gzEvaluate = new TGzEvaluate();
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
        List<TGzUserSubject> tGzUserSubjects = gzUserSubjectDao.selectByPrimaryKey(subjectId);
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


}
