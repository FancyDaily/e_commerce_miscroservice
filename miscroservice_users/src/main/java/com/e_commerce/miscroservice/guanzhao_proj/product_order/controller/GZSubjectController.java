package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzEvaluate;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZSubjectService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 观照律师训练营
 * 商品模块
 */
@RestController
@RequestMapping("gz/api/v1/subject")
@Data
public class GZSubjectController {

    @Autowired
    private GZSubjectService gzSubjectService;

    @Autowired
    private GZLessonService gzLessonService;

    /**
     * 在售课程列表
     * {
     * "id": 1,
     * "name": "Ivy老师的第一堂课",
     * "subjectHeadPortraitPath": "1111",
     * "price": "1288",
     * "forSalePrice": "998",
     * "forSaleStatus": 1,
     * "forSaleSurplusNum": 15,
     * "seriesIndex": 1,
     * "avaliableStatus": "1",
     * "availableDate": 2019509,
     * "availableTime": 1300,
     * "remarks": "这是Ivy老师的第一堂课",
     * "securityKey": "sc_daj@#$djak",
     * "extend": "",
     * "createUser": 42,
     * "createUserName": "三胖",
     * "createTime": 1557380201406,
     * "updateUser": 42,
     * "updateUserName": "三胖",
     * "updateTime": 1557380201406,
     * "isValid": "1"
     * }
     *
     * @return
     */
    @RequestMapping("subject/list")
    public Object subjectList() {
        AjaxResult result = new AjaxResult();
        try {
            List<TGzSubject> resultList = gzSubjectService.subjectList();
            result.setData(resultList);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "在售课程列表", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("在售课程列表", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 课程详情
     *
     * @param subjectId
     * @return
     */
    @RequestMapping("subject/detail")
    public Object subjectDetail(Long subjectId) {
        AjaxResult result = new AjaxResult();
        try {
            TGzSubject subject = gzSubjectService.subjectDetail(subjectId);
            result.setData(subject);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "课程详情", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("课程详情", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 章节列表
     *
     * @param subjectId
     * @return
     */
    @RequestMapping("lesson/list")
    public Object subjectLessonList(Long subjectId) {
        //TODO AUTH
        AjaxResult result = new AjaxResult();
        try {
            List<TGzLesson> resultList = gzSubjectService.lessonList(subjectId);
            result.setData(resultList);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "章节列表", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("章节列表", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 评价章节
     *
     * @param lessonId
     * @return
     */
    @Consume(TGzEvaluate.class)
    @RequestMapping("lesson/evaluate/send")
    public Object subjectLessonEvaluateSend(@RequestParam(required = true) Long subjectId, @RequestParam(required = true) Long lessonId, Integer level, String comment) {
        //TODO AUTH
        TGzEvaluate evaluate = (TGzEvaluate) ConsumeHelper.getObj();
        TUser user = new TUser();
        user.setId(42l);
        user.setName("三胖");
        AjaxResult result = new AjaxResult();
        try {
            gzSubjectService.evaluateLesson(user, evaluate);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "评价章节", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("评价章节", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 解锁课程
     *
     * @param subjectId
     * @return
     */
    @RequestMapping("subject/unlock")
    public Object subjectUnlock(Long subjectId) {
        AjaxResult result = new AjaxResult();
        try {
            gzSubjectService.unlockSubject(subjectId);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "解锁课程", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解锁课程", e);
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("lesson/unlock")
    public Object lessonUnlock(Long lessonId) {
        AjaxResult result = new AjaxResult();
        try {
            gzLessonService.unlockLesson(lessonId);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "解锁课程", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解锁课程", e);
            result.setSuccess(false);
        }
        return result;
    }

}
