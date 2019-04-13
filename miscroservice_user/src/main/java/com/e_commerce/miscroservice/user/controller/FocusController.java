package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.FocusService;
import com.e_commerce.miscroservice.user.vo.DesensitizedUserView;
import lombok.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关注模块
 * 关注Controller层
 */
@RestController
@RequestMapping("api/v2/focus")
@Log
public class FocusController extends BaseController {

    @Autowired
    private FocusService focusService;

    /**
     * 关注/取消关注
     *
     * @param token
     * @param userFollowId 将要被关注的人id
     *
     * {
     *     "success": true,
     *     "errorCode": "",
     *     "msg": "",
     *     "data": ""
     * }
     *
     * @return
     */
    @RequestMapping("submit")
    public Object submit(String token, Long userFollowId) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser(token);
        try {
            focusService.submit(user, userFollowId);
//            log.info("dddd{}hhdhd{}","forexample","aa");
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("关注异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("关注异常",e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 关注列表
     *
     * @param token
     * @param lastTime
     * @param pageSize
     *
     * {
     *     "success": true,
     *     "errorCode": "",
     *     "msg": "",
     *     "data": {
     *         "resultList": [  //用户信息
     *             {
     *                 "idString": "",
     *                 "age": "",
     *                 "isAtten": 0,    //是否互关 0不是互关 1互关
     *                 "authStatus": 1,
     *                 "vxId": "无",
     *                 "masterStatus": "",
     *                 "maxLevelMin": "",
     *                 "levelName": "",
     *                 "needGrowthNum": "",
     *                 "nextLevelName": "",
     *                 "timeStamp": "1545979786284",
     *                 "id": 73714097408966656,
     *                 "userAccount": "57210364",
     *                 "name": "Lilian",    //姓名
     *                 "jurisdiction": 0,
     *                 "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/1545633406569112.png",    //头像
     *                 "userPicturePath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_background.png",
     *                 "vxOpenId": "",
     *                 "occupation": "无业游民",
     *                 "workPlace": "",
     *                 "college": "",
     *                 "birthday": 19960705,
     *                 "sex": 0,
     *                 "maxEducation": "未设置",
     *                 "followNum": 0,
     *                 "receiptNum": 0,
     *                 "remarks": "",
     *                 "level": 3,
     *                 "growthValue": 225,
     *                 "seekHelpNum": 4,
     *                 "serveNum": 7,
     *                 "seekHelpDoneNum": "",
     *                 "serveDoneNum": "",
     *                 "surplusTime": 71,
     *                 "freezeTime": 0,
     *                 "creditLimit": "",
     *                 "publicWelfareTime": 0,
     *                 "authenticationStatus": 2,
     *                 "authenticationType": 1,
     *                 "totalEvaluate": 105,
     *                 "creditEvaluate": 35,
     *                 "majorEvaluate": 35,
     *                 "attitudeEvaluate": 35,
     *                 "skill": "",
     *                 "integrity": 5,
     *                 "isCompanyAccount": "",
     *                 "userType": "1",
     *                 "extend": "",
     *                 "createUser": 73714097408966656,
     *                 "createUserName": "晓主57210364",
     *                 "createTime": 1545357210364,
     *                 "updateUser": 68813262698840064,
     *                 "updateUserName": "冰茬子",
     *                 "updateTime": 1546490924154,
     *                 "isValid": "1",
     *                 "inviteCode": "QcQ6AC",
     *                 "idStr": "73714097408966656",
     *                 "joinCompany": false
     *             }
     *         ],
     *         "totalCount": 1
     *     }
     * }
     *
     * @return
     */
    @RequestMapping("focusList")
    public Object focusList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser(token);
        try {
            QueryResult<DesensitizedUserView> queryResult = focusService.myList(user, lastTime, pageSize, true);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("关注列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("关注列表异常", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 粉丝列表
     *
     * @param token
     * @param lastTime
     * @param pageSize
     * @return
     *
     * {
     *     "success": true,
     *     "errorCode": "",
     *     "msg": "",
     *     "data": {
     *         "resultList": [
     *             {
     *                 "idString": "",
     *                 "age": 25,
     *                 "isAtten": 0,   //是否互关， 0否 1互关
     *                 "authStatus": 1,
     *                 "vxId": "无",
     *                 "masterStatus": "",
     *                 "maxLevelMin": "",
     *                 "levelName": "",
     *                 "needGrowthNum": "",
     *                 "nextLevelName": "",
     *                 "timeStamp": "1544328132064",
     *                 "id": 68813258559062016,
     *                 "userAccount": "",
     *                 "name": "刘维",    //姓名
     *                 "jurisdiction": 0,
     *                 "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/1544189745461122.png",    //头像
     *                 "userPicturePath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/1544938083123131.png",
     *                 "vxOpenId": "oMgmu4qydMEoA_2s0X5txM_HeZcc",
     *                 "occupation": "",
     *                 "workPlace": "",
     *                 "college": "",
     *                 "birthday": 19910704,
     *                 "sex": 2,
     *                 "maxEducation": "硕士",
     *                 "followNum": 0,
     *                 "receiptNum": 0,
     *                 "remarks": "圣诞节快乐~~",
     *                 "level": 4,
     *                 "growthValue": 495,
     *                 "seekHelpNum": 14,
     *                 "serveNum": 3,
     *                 "seekHelpDoneNum": "",
     *                 "serveDoneNum": "",
     *                 "surplusTime": 737,
     *                 "freezeTime": 30,
     *                 "creditLimit": "",
     *                 "publicWelfareTime": 0,
     *                 "authenticationStatus": 2,
     *                 "authenticationType": 1,
     *                 "totalEvaluate": 45,
     *                 "creditEvaluate": 15,
     *                 "majorEvaluate": 15,
     *                 "attitudeEvaluate": 15,
     *                 "skill": "跑步",
     *                 "integrity": 100,
     *                 "isCompanyAccount": "",
     *                 "userType": "1",
     *                 "extend": "",
     *                 "createUser": 68813258559062016,
     *                 "createUserName": "刘维",
     *                 "createTime": 1537544885000,
     *                 "updateUser": 68813285541019648,
     *                 "updateUserName": "Shane",
     *                 "updateTime": 1547437236150,
     *                 "isValid": "1",
     *                 "inviteCode": "AVPFVc",
     *                 "idStr": "68813258559062016",
     *                 "joinCompany": false
     *             }
     *         ],
     *         "totalCount": 1
     *     }
     * }
     *
     */
    @RequestMapping("fanList")
    public Object fanList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser(token);
        try {
            QueryResult<DesensitizedUserView> queryResult = focusService.myList(user, lastTime, pageSize, false);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("粉丝列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("关注列表异常", e);
            result.setSuccess(false);
        }
        return result;
    }

}
