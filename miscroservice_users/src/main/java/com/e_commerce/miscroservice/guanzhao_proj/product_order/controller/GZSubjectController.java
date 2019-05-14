package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzEvaluate;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZSubjectService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.SubjectInfosVO;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;
import lombok.Data;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 观照商品模块
 *
 */
@RestController
@RequestMapping("gz/api/v1/subject")
@Log
public class GZSubjectController {

    @Autowired
    private GZSubjectService gzSubjectService;

    @Autowired
    private GZLessonService gzLessonService;

    /**
     * 在售课程列表
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     *                 {
     *                 "resultList": [
     *                 {
     *                 "period": 15,
     *                 "avaliableStatus": 1,
     *                 "forSalePrice": 998,
     *                 "isValid": "1",
     *                 "forSaleSurplusNum": 15,
     *                 "updateUser": 42,
     *                 "availableDate": "20190510",
     *                 "updateTime": 1557472345000,
     *                 "seriesIndex": 1,
     *                 "subjectHeadPortraitPath": "http://www.baidu.com",
     *                 "availableTime": "1350",
     *                 "descPic": "http://1.baidu.com,http://2.baidu.com,http://3.baidu.com,http://4.baidu.com,http://5.baidu.com,http://6.baidu.com,http://last.baidu.com",
     *                 "forSaleStatus": 1,
     *                 "createTime": 1557472194000,
     *                 "price": 1288,
     *                 "name": "Ivy的第一堂课",
     *                 "createUser": 42,
     *                 "id": 1,
     *                 "remarks": "这是一段描述"
     *                 }
     *                 ],
     *                 "totalCount": 2
     *                 }
     * @return
     */
    @RequestMapping("subject/list")
    public Object subjectList(Integer pageNum, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        try {
            QueryResult queryResult = gzSubjectService.subjectList(pageNum, pageSize);
            result.setData(queryResult);
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
     * @param subjectId 课程编号
     *                  {
     *                  "resultList": [
     *                  {
     *                  "period": 15,
     *                  "avaliableStatus": 1,
     *                  "forSalePrice": 998,
     *                  "isValid": "1",
     *                  "forSaleSurplusNum": 15,
     *                  "updateUser": 42,
     *                  "availableDate": "20190510",
     *                  "updateTime": 1557472345000,
     *                  "seriesIndex": 1,
     *                  "subjectHeadPortraitPath": "http://www.baidu.com",
     *                  "availableTime": "1350",
     *                  "descPic": "http://1.baidu.com,http://2.baidu.com,http://3.baidu.com,http://4.baidu.com,http://5.baidu.com,http://6.baidu.com,http://last.baidu.com",
     *                  "forSaleStatus": 1,
     *                  "createTime": 1557472194000,
     *                  "price": 1288,
     *                  "name": "Ivy的第一堂课",
     *                  "createUser": 42,
     *                  "id": 1,
     *                  "remarks": "这是一段描述"
     *                  }
     *                  ],
     *                  "totalCount": 2
     *                  }
     * @return
     */
    @RequestMapping("subject/detail")
    public Object subjectDetail(Long subjectId) {
        AjaxResult result = new AjaxResult();
        try {
            SubjectInfosVO subjectVO = gzSubjectService.subjectDetail(subjectId);
            result.setData(subjectVO);
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
     * 课程详情
     *
     * @param subjectId 课程编号
     *                  {
     *                  "resultList": [
     *                  {
     *                  "period": 15,
     *                  "avaliableStatus": 1,
     *                  "forSalePrice": 998,
     *                  "isValid": "1",
     *                  "forSaleSurplusNum": 15,
     *                  "updateUser": 42,
     *                  "availableDate": "20190510",
     *                  "updateTime": 1557472345000,
     *                  "seriesIndex": 1,
     *                  "subjectHeadPortraitPath": "http://www.baidu.com",
     *                  "availableTime": "1350",
     *                  "descPic": "http://1.baidu.com,http://2.baidu.com,http://3.baidu.com,http://4.baidu.com,http://5.baidu.com,http://6.baidu.com,http://last.baidu.com",
     *                  "forSaleStatus": 1,
     *                  "createTime": 1557472194000,
     *                  "price": 1288,
     *                  "name": "Ivy的第一堂课",
     *                  "createUser": 42,
     *                  "id": 1,
     *                  "remarks": "这是一段描述"
     *                  }
     *                  ],
     *                  "totalCount": 2
     *                  }
     * @return
     */
    @RequestMapping("subject/detail/" + TokenUtil.AUTH_SUFFIX)
    public Object subjectDetailAuth(Long subjectId) {
        AjaxResult result = new AjaxResult();
        try {
            SubjectInfosVO subjectVO = gzSubjectService.subjectDetail(subjectId);
            result.setData(subjectVO);
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
     * @param subjectId 课程编号
     *                  [
     *                  {
     *                  "id": 1,
     *                  "subjectId": 1,
     *                  "lessonIndex": 1,
     *                  "name": "Ivy的第一堂课01",
     *                  "avaliableStatus": 0,
     *                  "availableDate": "20190510",
     *                  "availableTime": "1400",
     *                  "extend": "",
     *                  "createUser": 42,
     *                  "createTime": 1557561829000,
     *                  "updateUser": 42,
     *                  "updateTime": 1557562011000,
     *                  "isValid": "1"
     *                  },
     *                  {
     *                  "id": 2,
     *                  "subjectId": 1,
     *                  "lessonIndex": 2,
     *                  "name": "Ivy的第一堂课02",
     *                  "avaliableStatus": 0,
     *                  "availableDate": "20190517",
     *                  "availableTime": "1400",
     *                  "extend": "",
     *                  "createUser": 42,
     *                  "createTime": 1557561829000,
     *                  "updateUser": 42,
     *                  "updateTime": 1557562012000,
     *                  "isValid": "1"
     *                  },
     *                  {
     *                  "id": 3,
     *                  "subjectId": 1,
     *                  "lessonIndex": 3,
     *                  "name": "Ivy的第一堂课03",
     *                  "avaliableStatus": 0,
     *                  "availableDate": "20190524",
     *                  "availableTime": "1400",
     *                  "extend": "",
     *                  "createUser": 42,
     *                  "createTime": 1557561829000,
     *                  "updateUser": 42,
     *                  "updateTime": 1557562012000,
     *                  "isValid": "1"
     *                  }
     *                  ]
     * @return
     */
    @RequestMapping("lesson/list")
    public Object subjectLessonList(@RequestParam(required = true) Long subjectId) {
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
     * 该课程下我的章节列表(正在播放页)
     *
     * @param subjectId 课程编号
     *                  {
     *                  "resultList": [
     *                  {
     *                  "userId": 42,
     *                  "subjectId": 1,
     *                  "lessonId": 1,
     *                  "lessonIndex": 1,
     *                  "availableDate": "20190510",
     *                  "availableTime": "1400",
     *                  "avaliableStatus": 0,
     *                  "name": "Ivy的第一堂课01",
     *                  "sign": "",
     *                  "status": 1,
     *                  "videoCompletion": 0,
     *                  "lessonCompletionStatus": 0,
     *                  "extend": "",
     *                  "createUser": 42,
     *                  "createTime": 1557561829000,
     *                  "updateUser": 42,
     *                  "updateTime": 1557562011000,
     *                  "isValid": "1"
     *                  },
     *                  {
     *                  "userId": 42,
     *                  "subjectId": 1,
     *                  "lessonId": 2,
     *                  "lessonIndex": 2,
     *                  "availableDate": "20190517",
     *                  "availableTime": "1400",
     *                  "avaliableStatus": 0,
     *                  "name": "Ivy的第一堂课02",
     *                  "sign": "",
     *                  "status": 1,
     *                  "videoCompletion": 0,
     *                  "lessonCompletionStatus": 0,
     *                  "extend": "",
     *                  "createUser": 42,
     *                  "createTime": 1557561829000,
     *                  "updateUser": 42,
     *                  "updateTime": 1557562012000,
     *                  "isValid": "1"
     *                  },
     *                  {
     *                  "userId": 42,
     *                  "subjectId": 1,
     *                  "lessonId": 3,
     *                  "lessonIndex": 3,
     *                  "availableDate": "20190524",
     *                  "availableTime": "1400",
     *                  "avaliableStatus": 0,
     *                  "name": "Ivy的第一堂课03",
     *                  "sign": "",
     *                  "status": 1,
     *                  "videoCompletion": 0,
     *                  "lessonCompletionStatus": 0,
     *                  "extend": "",
     *                  "createUser": 42,
     *                  "createTime": 1557561829000,
     *                  "updateUser": 42,
     *                  "updateTime": 1557562012000,
     *                  "isValid": "1"
     *                  }
     *                  ],
     *                  "totalCount": 3
     *                  }
     * @return
     */
    @RequestMapping("lesson/list/mine/" + TokenUtil.AUTH_SUFFIX)
    public Object mySubjectLessonList(@RequestParam(required = true) Long subjectId, Integer pageNum, Integer pageSize) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        if (user == null) {
            user = new TUser();
            user.setId(42l);
        }
        try {
            QueryResult myLessonVOS = gzLessonService.mySubjectLessonList(user.getId(), subjectId, pageNum, pageSize);
            result.setData(myLessonVOS);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "该课程下我的章节列表(正在播放页)", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("该课程下我的章节列表(正在播放页)", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 评价章节
     *
     * @param lessonId 章节编号
     *                 {
     *                 "success": false,
     *                 "errorCode": "",
     *                 "msg": "该课程您已经评价过!",
     *                 "data": ""
     *                 }
     * @return
     */
    @Consume(TGzEvaluate.class)
    @RequestMapping("lesson/evaluate/send/" + TokenUtil.AUTH_SUFFIX)
    public Object subjectLessonEvaluateSend(@RequestParam(required = true) Long subjectId, @RequestParam(required = true) Long lessonId, Integer level, String comment) {
        //TODO AUTH
        TGzEvaluate evaluate = (TGzEvaluate) ConsumeHelper.getObj();
        TUser user = UserUtil.getUser();
        if (user == null) {
            user = new TUser();
            user.setId(42l);
        }
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
     * 课程评价列表
     * @param subjectId 课程编号
     * @param lessonId 章节编号
     * {
     *         "resultList": [
     *             {
     *                 "id": 2,
     *                 "userId": 42,
     *                 "lessonId": 1,
     *                 "subjectId": 1,
     *                 "level": 0,
     *                 "comment": "张三这个人真的无解",
     *                 "extend": "",
     *                 "createUser": 42,
     *                 "createTime": 1557566044000,
     *                 "updateUser": 42,
     *                 "updateTime": 1557566044000,
     *                 "isValid": "1"
     *             }
     *         ],
     *         "totalCount": 1
     *     }
     *
     * @return
     */
    @RequestMapping("lesson/evaluate/list/" + TokenUtil.AUTH_SUFFIX)
    public Object subjectLessonEvaluateList(@RequestParam(required = true) Long subjectId, @RequestParam(required = true) Long lessonId, Integer pageNum, Integer pageSize) {
        TUser user = UserUtil.getUser();
        if (user == null) {
            user = new TUser();
            user.setId(42l);
        }
        AjaxResult result = new AjaxResult();
        try {
            QueryResult<TGzEvaluate> queryResult = gzLessonService.lessonEvaluateList(subjectId, lessonId, pageNum, pageSize);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "课程评价列表", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("课程评价列表", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 视频进度更新
     * @param currentSeconds
     * @param totalSeconds
     * @param lessonId
     * @return
     */
    @RequestMapping("lesson/completion/update/" + TokenUtil.AUTH_SUFFIX)
    public Object lessonCompletionUpdate(@RequestParam(required = true) Integer currentSeconds, @RequestParam(required = true)  Integer totalSeconds, @RequestParam(required = true) Long lessonId) {
        TUser user = UserUtil.getUser();
        if (user == null) {
            user = new TUser();
            user.setId(42l);
        }
        AjaxResult result = new AjaxResult();
        try {
            gzLessonService.updateVideoCompletion(user.getId(), lessonId, currentSeconds, totalSeconds);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "视频进度更新", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("视频进度更新", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 发布新课程
     *
     * @return
     */
    @RequestMapping("subject/publish")
    @Consume(TGzSubject.class)
    public Object subjectPublish(Integer totalSeriesSize, @RequestParam(required = true) String name, Integer period,@RequestParam(required = true) String subjectHeadPortraitPath,@RequestParam(required = true) Double price, Double forSalePrice, Integer forSaleSurplusNum, Integer seriesIndex, String availableDate, String availableTime, String endDate, String endTime, String descPic, String remarks) {
        AjaxResult result = new AjaxResult();
        TGzSubject gzSubject = (TGzSubject) ConsumeHelper.getObj();
        try {
            gzSubjectService.publish(gzSubject, totalSeriesSize);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "发布新课程", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发布新课程", e);
            result.setSuccess(false);
        }
        return result;
    }


    /**
     * 正在学习课程列表
     * @param pageNum
     * @param pageSize
     *
     * {
     * 	"success": true,
     * 	"errorCode": "",
     * 	"msg": "",
     * 	"data": {
     * 		"resultList": [{
     * 			"id": 3,
     * 			"userId": 1,
     * 			"subjectId": 1,
     * 			"status": 1,
     * 			"notesNum": 0,
     * 			"homeworkExpectedNum": 15,
     * 			"homeworkDoneNum": 0,
     * 			"completion": 0,
     * 			"expireTime": 1560058601000,
     * 			"afterTime": 26,
     * 			"extend": "",
     * 			"isValid": "1",
     * 			"subjectName": "Ivy的第一堂课"
     *                }, {
     * 			"id": 4,
     * 			"userId": 1,
     * 			"subjectId": 1,
     * 			"status": 1,
     * 			"notesNum": 0,
     * 			"homeworkExpectedNum": 15,
     * 			"homeworkDoneNum": 0,
     * 			"completion": 0,
     * 			"expireTime": 1560058601000,
     * 			"afterTime": 26,
     * 			"extend": "",
     * 			"isValid": "1",
     * 			"subjectName": "Ivy的第一堂课"
     *        }, {
     * 			"id": 5,
     * 			"userId": 1,
     * 			"subjectId": 1,
     * 			"status": 1,
     * 			"notesNum": 0,
     * 			"homeworkExpectedNum": 15,
     * 			"homeworkDoneNum": 0,
     * 			"completion": 0,
     * 			"expireTime": 1560058601000,
     * 			"afterTime": 26,
     * 			"extend": "",
     * 			"isValid": "1",
     * 			"subjectName": "Ivy的第一堂课"
     *        }],
     * 		"totalCount": 14  	}
     * }
     * @return
     */
    @RequestMapping("subject/learning/list/" + TokenUtil.AUTH_SUFFIX)
    public Object findMyLearningSubject(Integer pageNum,Integer pageSize){
        AjaxResult result = new AjaxResult();
        if (pageNum==null||pageSize==null){
            result.setSuccess(false);
            result.setMsg("参数为空");
            return result;
        }
        try {
            Integer id = 1;
//            Integer id = IdUtil.getId();
            QueryResult<MyLearningSubjectVO> list = gzSubjectService.findMyLearningSubject(id,pageNum,pageSize);
            result.setSuccess(true);
            result.setData(list);
        }catch (MessageException e){
            log.warn("正在学习课程列表 e={}",e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            log.error("正在学习课程列表={}",e);
            result.setSuccess(false);
        }
        return result;


    }

    /**
     * 已结束的课程
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("subject/ending/list/" + TokenUtil.AUTH_SUFFIX)
    public Object findEndingSubject(Integer pageNum,Integer pageSize){
        AjaxResult result = new AjaxResult();
        if (pageNum==null||pageSize==null){
            result.setSuccess(false);
            result.setMsg("参数为空");
            return result;
        }
        try {
            Integer id = IdUtil.getId();
            QueryResult<MyLearningSubjectVO> list = gzSubjectService.findEndingSubject(id,pageNum,pageSize);
            result.setSuccess(true);
            result.setData(list);
        }catch (MessageException e){
            log.warn("已结束的课程列表 e={}",e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            log.error("已结束的课程列表={}",e);
            result.setSuccess(false);
        }
        return result;
    }

}
