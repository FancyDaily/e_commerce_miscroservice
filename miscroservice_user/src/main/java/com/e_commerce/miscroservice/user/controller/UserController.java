package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserSkill;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.CompanyService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户Controller
 * 功能描述:用户Controller
 */
@RestController
@RequestMapping("api/v2/user")
public class UserController extends BaseController {

    Log logger = Log.getInstance(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    /**
     * 时间轨迹
     * @param token 登录凭证
     * @param ymString 年月字符串，eg.2019-01
     * @param option 操作，0全部，1收入，2支出
     *
     * {
     *     "success": true,
     *     "msg": "",
     *     "data": {
     *         "monthTotalOut": 0,  //月度总支出
     *         "total": 49, //总互助时
     *         "month": "01",   //当前月份
     *         "monthTotalIn": 37,  //月度总收入
     *         "frozen": 23,    //冻结金额
     *         "monthList": [
     *             {
     *                 "idString": "82438795718295552", //主键
     *                 "id": 82438795718295552,
     *                 "userId": 68813259007852544,
     *                 "fromUserId": 68813285541019648,
     *                 "type": 1,
     *                 "title": "提供一次服务",   //名目
     *                 "date": "2019-01-14",    //日期
     *                 "targetId": 80989437885939712,
     *                 "time": 0,   //金额
     *                 "createUser": 68813285541019648,
     *                 "createUserName": "Shane",
     *                 "createTime": 1547437340538,
     *                 "updateUser": 68813285541019648,
     *                 "updateUserName": "Shane",
     *                 "updateTime": 1547437340538,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "82435699785072640",
     *                 "id": 82435699785072640,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-14",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1547436602410,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547436602410,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "81365494354935808",
     *                 "id": 81365494354935808,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-11",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1547181445552,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547181445552,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "81073248518799360",
     *                 "id": 81073248518799360,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-10",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1547111768715,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547111768715,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "80740311462051840",
     *                 "id": 80740311462051840,
     *                 "userId": 68813259007852544,
     *                 "fromUserId": 68813260748488704,
     *                 "type": 1,
     *                 "title": "提供一次服务",
     *                 "date": "2019-01-09",
     *                 "targetId": 80740102669598720,
     *                 "time": 10,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1547032390335,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1547032390335,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "80739259920678912",
     *                 "id": 80739259920678912,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-09",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1547032139628,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547032139628,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "80282449333977088",
     *                 "id": 80282449333977088,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-08",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1546923227497,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546923227497,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "79899354319552512",
     *                 "id": 79899354319552512,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-07",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1546831890528,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546831890528,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "78878228382482432",
     *                 "id": 78878228382482432,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-04",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1546588435133,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546588435133,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "78552166431719424",
     *                 "id": 78552166431719424,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-03",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1546510695906,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546510695906,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "idString": "78100464930914304",
     *                 "id": 78100464930914304,
     *                 "userId": 68813259007852544,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-02",
     *                 "time": 3,
     *                 "createUser": 68813259007852544,
     *                 "createUserName": "?",
     *                 "createTime": 1546403001876,
     *                 "updateUser": 68813259007852544,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546403001876,
     *                 "isValid": "1"
     *             }
     *         ],
     *         "vacant": 26 //可用金额
     *     }
     * }
     *
     * @return
     */
    @PostMapping("payments")
    public Object payments(String token, String ymString, String option) {
        AjaxResult result = new AjaxResult();
        //TODO redis
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            Map<String, Object> payments = userService.payments(user, ymString, option);
            result.setData(payments);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("时间轨迹异常: " + e.getMessage());
        } catch (Exception e) {
            logger.info("时间轨迹异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 冻结明细
     * @param token
     * @param lastTime 分页参数(说明：返回数据的按时间倒序排列。以下时间指代创建时间create_time: 向上翻页时，把当前最大的时间给我；向下翻页时，把当前最小的时间给我)
     * @param pageSize 每页最大条数
     *
     * {
     *         "resultList": [
     *             {
     *                 "serviceIdString": "101430539319246848", //服务id
     *                 "orderIdString": "101430540338462999",   //订单id
     *                 "orderId": 101430540338462999,
     *                 "userId": 68813260748488704,
     *                 "serviceName": "幻想读Serializable999", //服务名
     *                 "type": 1,   //服务类型 1求助 2服务
     *                 "startTime": 1552022400000,  //开始时间戳
     *                 "endTime": 1552023600000,    //结束时间戳
     *                 "freezeTime": 9, //冻结金额
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1548046588516,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1548046588516,
     *                 "isValid": "1"
     *             }
     *         ],
     *         "totalCount": 1
     * }
     *
     * @return
     */
    @PostMapping("freezeList")
    public Object freezeList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813260748488704l);
        try {
            QueryResult<UserFreezeView> queryResult = userService.frozenList(user.getId(), lastTime, pageSize);
            result.setSuccess(true);
            result.setData(queryResult);
        } catch (MessageException e) {
            result.setMsg("冻结明细异常: " + e.getMessage());
            logger.error(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            logger.info("冻结明细异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 公益历程列表
     * @param token
     * @param lastTime 参考冻结明细说明
     * @param pageSize 参考冻结明细说明
     * @param year 年份
     *
     * {
     *     "success": true,
     *     "msg": "",
     *     "data": {
     *         "total": null,
     *         "detailList": {
     *             "resultList": [
     *                 {
     *                     "name": "zhangsan",  //项目名称
     *                     "time": "2019年4月3日", //时间
     *                     "coin": "3", //金额
     *                     "timeStamp": 1547445330978,
     *                     "in": true
     *                 },
     *                 {
     *                     "name": "zhangsan",
     *                     "time": "2019年4月3日",
     *                     "coin": "4",
     *                     "timeStamp": 1547445330978,
     *                     "in": true
     *                 }
     *             ],
     *             "totalCount": 0
     *         },
     *         "yearTotal": 7
     *     }
     * }
     *
     * @return
     */
    @PostMapping("publicWelfareList")
    public Object publicWelfareList(String token, Long lastTime, Integer pageSize, Integer year) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            Map<String, Object> map = userService.publicWelfareList(user, lastTime, pageSize, year);
            result.setData(map);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("公益历程列表异常: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("公益历程列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看技能（包含详细信息）
     * @param token
     *
     * {
     *                     "idString": "95167783989411840",
     *                     "id": 95167783989411840,
     *                     "userId": 68813260748488704,
     *                     "name": "歌剧二",
     *                     "description": "海豚音，大家好，才是真的好",
     *                     "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545625655755109.png",
     *                     "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png",
     *                     "detailUrlArray": [
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png",
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png"
     *                     ],
     *                     "createUser": 68813259007852544,
     *                     "createUserName": "盖伦😂",
     *                     "createTime": 1550472167835,
     *                     "updateUser": 68813259007852544,
     *                     "updateUserName": "盖伦😂",
     *                     "updateTime": 1550472167835,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "idString": "95160769636728832",
     *                     "id": 95160769636728832,
     *                     "userId": 68813260748488704,
     *                     "name": "书法",
     *                     "description": "精通国学书法，三百年，好品质",
     *                     "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154803914219080.png",
     *                     "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png",
     *                     "detailUrlArray": [
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png",
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png"
     *                     ],
     *                     "createUser": 68813259007852544,
     *                     "createUserName": "盖伦😂",
     *                     "createTime": 1550470495483,
     *                     "updateUser": 68813259007852544,
     *                     "updateUserName": "盖伦😂",
     *                     "updateTime": 1550472853539,
     *                     "isValid": "1"
     *                 }
     *
     * @return
     */
    @PostMapping("skill/list")
    public Object skillList(String token) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813260748488704l);
        try {
            UserSkillListView skillView = userService.skills(user);
            result.setData(skillView);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看技能异常: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 增加技能
     * @param token
     * @param name  技能名
     * @param description   描述
     * @param headUrl   封面图
     * @param detailUrls    内容图,多张图片使用逗号分隔。
     *
     * {
     *     "success": true, //成功
     *     "msg": ""
     * }
     *
     * @return
     */
    @PostMapping("skill/add")
    @Consume(TUserSkill.class)
    public Object skillAdd(String token, String name, String description, String headUrl, String detailUrls) {
        AjaxResult result = new AjaxResult();
        TUserSkill skill = (TUserSkill) ConsumeHelper.getObj();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            userService.skillAdd(user, skill);
            result.setSuccess(true);
        } catch (MessageException e) {
            result.setMsg(e.getMessage());
            logger.error("添加技能异常: " + e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("添加技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 修改技能
     * @param token
     * @param name 技能名
     * @param description 描述
     * @param headUrl 封面图
     * @param detailUrl 内容图，多张图使用逗号分隔
     *
     * {
     *     "success": true, //成功
     *     "msg": ""
     * }
     *
     * @return
     */
    @PostMapping("skill/modify")
    @Consume(TUserSkill.class)
    public Object skillModify(String token, Long id, String name, String description, String headUrl, String detailUrl) {
        AjaxResult result = new AjaxResult();
        TUserSkill skill = (TUserSkill) ConsumeHelper.getObj();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            userService.skillModify(user, skill);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("修改技能异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("修改技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除技能
     * @param token
     * @param id 技能id
     *
     * {
     *     "success": true, //成功
     *     "msg": ""
     * }
     *
     * @return
     */
    @PostMapping("skill/delete")
    public Object skillDelete(String token,Long id) {
        AjaxResult result = new AjaxResult();
        try {
            userService.skillDelete(id);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("删除技能异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("删除技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 收藏列表 还没写
     * @param token
     * @param lastTime 分页参数
     * @param pageSize 每页条数
     * @return
     */
    @PostMapping("collect/list")
    public Object collectList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            //TODO 调订单模块的收藏列表方法
//            QueryResult<Map<String, Object>> queryResult = userService.collectList(user,lastTime,pageSize);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("收藏列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("收藏列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 收藏/取消收藏 还没写
     * @param token
     * @param orderId 订单id
     * @return
     */
    @PostMapping("collect")
    public Object collect(String token, Long orderId) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            userService.collect(user,orderId);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("收藏列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("收藏列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看用户基本信息
     * @param token
     * @param userId 用户id
     * @return
     */
    @PostMapping("info")
    public Object info(String token, Long userId) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            DesensitizedUserView userView = userService.info(user,userId);
            result.setData(userView);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("收藏列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("收藏列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看个人主页(基本信息、技能列表、提供的服务、提供的求助)
     * @param token 登录凭证
     * @param userId 目标用户的id
     *
     * {
     *     "success": true,
     *     "msg": "",
     *     "data": {
     *         "desensitizedUserView": {
     *             "isAtten": 0,    //我与对方的关注状态，0未关注，1我已关注她，2我们俩互关
     *             "authStatus": 1,
     *             "vxId": "无",
     *             "id": 68813260748488704,
     *             "name": "马晓晨",   //昵称
     *             "jurisdiction": 0,
     *             "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/15446050826379.png",  //用户头像
     *             "userPicturePath": "http://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_background.png",   //个人主页背景图
     *             "vxOpenId": "oMgmu4vtWtuG_eFCMgvfJB8buPhI",  //openId
     *             "occupation": "",
     *             "birthday": 19940704,    //生日
     *             "sex": 1,    //性别，1男，2女
     *             "maxEducation": "本科",    //最大学历
     *             "followNum": 0,  //粉丝数量
     *             "receiptNum": 0,
     *             "remarks": "",
     *             "level": 4,
     *             "growthValue": 475,  //成长值
     *             "seekHelpNum": 18,
     *             "serveNum": 10,
     *             "surplusTime": 47,   //总金额
     *             "freezeTime": 210,   //冻结金额
     *             "creditLimit": 200,  //授信额度
     *             "publicWelfareTime": 0,  //公益时
     *             "authenticationStatus": 2, //实名认证状态,1未认证，2已认证
     *             "authenticationType": 1, //实名认证类型，1个人认证，2组织认证
     *             "totalEvaluate": 146, //三项评分总和
     *             "creditEvaluate": 48,    //信用评分
     *             "majorEvaluate": 49, //专业评分
     *             "attitudeEvaluate": 49,  //态度评分
     *             "skill": "",
     *             "integrity": 100,    //用户完整度
     *             "isCompanyAccount": 0,   //是否为组织账号 0个人账号 1组织账号(个人账号提交组织认证类型的请求得到处理通过之后，会诞生一个组织账号)
     *             "userType": "1", //用户类型 用于确定显示何种标签,1个人 2普通组织(除公益组织之外的组织) 3公益组织
     *             "createUser": 68813260748488704,
     *             "createUserName": "马晓晨",
     *             "createTime": 1537941095000,
     *             "updateUser": 68813262698840064,
     *             "updateUserName": "冰茬子",
     *             "updateTime": 1548135643427,
     *             "isValid": "1",
     *             "inviteCode": "EuciNL",
     *             "joinCompany": false
     *         },
     *         "services": {
     *             "resultList": [
     *                 {
     *                     "id": 101430540338462777,
     *                     "serviceId": 101430539319246848,
     *                     "mainId": 101430540338462720,
     *                     "serviceName": "可重复读Repeatable Read777", //名字
     *                     "servicePlace": 1,   //线上
     *                     "labels": "hehe,haha",   //标签
     *                     "type": 2,   //服务
     *                     "status": 1, //状态
     *                     "source": 1, //来源, 1个人，2组织
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,  //报名人数
     *                     "confirmNum": 0, //确认人数
     *                     "startTime": 1552022400000,  //开始时间
     *                     "endTime": 1552023600000,    //结束时间
     *                     "timeType": 1,   //时间类型,0指定时间，1可重复
     *                     "collectTime": 10,   //单价
     *                     "collectType": 1,    //收取类型,1互助时，2公益时
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1551965325000,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1551965325062,
     *                     "isValid": "1"
     *                 }
     *             ],
     *             "totalCount": 1
     *         },
     *         "helps": {
     *             "resultList": [
     *                 {
     *                     "id": 101675891532234752,
     *                     "serviceId": 101675890827591680,
     *                     "mainId": 101675891532234752,
     *                     "serviceName": "新版本重复9",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 1,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1552267200000,
     *                     "endTime": 1552268400000,
     *                     "timeType": 1,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1552023821420,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1552023821420,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "id": 101641267686932480,
     *                     "serviceId": 101641267305250816,
     *                     "mainId": 101641267686932480,
     *                     "serviceName": "新版本重复6",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 1,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1551921600000,
     *                     "endTime": 1551922800000,
     *                     "timeType": 1,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1552015566529,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1552015566529,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "id": 101640613451005952,
     *                     "serviceId": 101640612591173632,
     *                     "mainId": 101640613451005952,
     *                     "serviceName": "新版本重复4",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 1,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1552612800000,
     *                     "endTime": 1552614000000,
     *                     "timeType": 1,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1552015410433,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1552015410433,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "id": 101640871165820928,
     *                     "serviceId": 101640870670893056,
     *                     "mainId": 101640871165820928,
     *                     "serviceName": "新版本重复5",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 1,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1551921600000,
     *                     "endTime": 1551922800000,
     *                     "timeType": 1,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1552015471964,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1552015471964,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "id": 101675590041468928,
     *                     "serviceId": 101675589445877760,
     *                     "mainId": 101675590041468928,
     *                     "serviceName": "新版本重复7",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 1,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 3,
     *                     "confirmNum": 0,
     *                     "startTime": 1552526400000,
     *                     "endTime": 1552527600000,
     *                     "timeType": 1,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1552023749565,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1552023749565,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "id": 101637659306229760,
     *                     "serviceId": 101637658576420864,
     *                     "mainId": 101637659306229760,
     *                     "serviceName": "新版本重复3",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 1,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1552008000000,
     *                     "endTime": 1552009200000,
     *                     "timeType": 1,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1552014706141,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1552014706141,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "id": 101430540338462999,
     *                     "serviceId": 101430539319246848,
     *                     "mainId": 101430540338462720,
     *                     "serviceName": "幻想读Serializable999",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 1,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1552022400000,
     *                     "endTime": 1552023600000,
     *                     "timeType": 0,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1551965325000,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1551965325062,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "id": 101430540338462888,
     *                     "serviceId": 101430539319246848,
     *                     "mainId": 101430540338462720,
     *                     "serviceName": "读已提交Read Commited888",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 2,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1552022400000,
     *                     "endTime": 1552023600000,
     *                     "timeType": 0,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1551965325000,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1551965325062,
     *                     "isValid": "1"
     *                 }
     *             ],
     *             "totalCount": 8
     *         },
     *         "skills": {
     *             "skillCnt": 2,
     *             "userSkills": [
     *                 {
     *                     "idString": "95167783989411840", //技能id
     *                     "id": 95167783989411840,
     *                     "userId": 68813260748488704,
     *                     "name": "歌剧二",//技能名
     *                     "description": "海豚音，大家好，才是真的好",//技能描述
     *                     "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545625655755109.png",//技能封面
     *                     "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png",
     *                     "detailUrlArray": [  //详细内容图
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png",
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png"
     *                     ],
     *                     "createUser": 68813259007852544,
     *                     "createUserName": "盖伦😂",
     *                     "createTime": 1550472167835,
     *                     "updateUser": 68813259007852544,
     *                     "updateUserName": "盖伦😂",
     *                     "updateTime": 1550472167835,
     *                     "isValid": "1"
     *                 },
     *                 {
     *                     "idString": "95160769636728832",
     *                     "id": 95160769636728832,
     *                     "userId": 68813260748488704,
     *                     "name": "书法",
     *                     "description": "精通国学书法，三百年，好品质",
     *                     "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154803914219080.png",
     *                     "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png",
     *                     "detailUrlArray": [
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png",
     *                         "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png"
     *                     ],
     *                     "createUser": 68813259007852544,
     *                     "createUserName": "盖伦😂",
     *                     "createTime": 1550470495483,
     *                     "updateUser": 68813259007852544,
     *                     "updateUserName": "盖伦😂",
     *                     "updateTime": 1550472853539,
     *                     "isValid": "1"
     *                 }
     *             ]
     *         }
     *     }
     * }
     *
     * @return
     */
    @PostMapping("page")
    public Object page(String token,Long userId) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            UserPageView page = userService.page(user, userId);
            result.setData(page);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看个人主页异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看个人主页异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看发布的服务/求助列表
     * @param token
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param isService
     *
     * {
     *     "success": true,
     *     "msg": "",
     *     "data": {
     *         "resultList": [
     *             {
     *                 "id": 101430540338462777,
     *                 "serviceId": 101430539319246848,
     *                 "mainId": 101430540338462720,
     *                 "serviceName": "可重复读Repeatable Read777", //名字
     *                 "servicePlace": 1,   //线上
     *                 "labels": "hehe,haha",   //标签
     *                 "type": 2,   //服务
     *                 "status": 1, //状态
     *                 "source": 1, //来源, 1个人，2组织
     *                 "serviceTypeId": 15000,
     *                 "enrollNum": 0,  //报名人数
     *                 "confirmNum": 0, //确认人数
     *                 "startTime": 1552022400000,  //开始时间
     *                 "endTime": 1552023600000,    //结束时间
     *                 "timeType": 1,   //时间类型,0指定时间，1可重复
     *                 "collectTime": 10,   //单价
     *                 "collectType": 1,    //收取类型,1互助时，2公益时
     *                 "createUser": 68813260748488704, //创建人发单者
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1551965325000,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1551965325062,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "id": 101641267686932480,
     *                 "serviceId": 101641267305250816,
     *                 "mainId": 101641267686932480,
     *                 "serviceName": "新版本重复6",
     *                 "servicePlace": 1,
     *                 "labels": "hehe,haha",
     *                 "type": 1,
     *                 "status": 1,
     *                 "source": 1,
     *                 "serviceTypeId": 15000,
     *                 "enrollNum": 0,
     *                 "confirmNum": 0,
     *                 "startTime": 1551921600000,
     *                 "endTime": 1551922800000,
     *                 "timeType": 1,
     *                 "collectTime": 10,
     *                 "collectType": 1,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1552015566529,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1552015566529,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "id": 101640613451005952,
     *                 "serviceId": 101640612591173632,
     *                 "mainId": 101640613451005952,
     *                 "serviceName": "新版本重复4",
     *                 "servicePlace": 1,
     *                 "labels": "hehe,haha",
     *                 "type": 1,
     *                 "status": 1,
     *                 "source": 1,
     *                 "serviceTypeId": 15000,
     *                 "enrollNum": 0,
     *                 "confirmNum": 0,
     *                 "startTime": 1552612800000,
     *                 "endTime": 1552614000000,
     *                 "timeType": 1,
     *                 "collectTime": 10,
     *                 "collectType": 1,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1552015410433,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1552015410433,
     *                 "isValid": "1"
     *             },
     *             {
     *                 "id": 101640871165820928,
     *                 "serviceId": 101640870670893056,
     *                 "mainId": 101640871165820928,
     *                 "serviceName": "新版本重复5",
     *                 "servicePlace": 1,
     *                 "labels": "hehe,haha",
     *                 "type": 1,
     *                 "status": 1,
     *                 "source": 1,
     *                 "serviceTypeId": 15000,
     *                 "enrollNum": 0,
     *                 "confirmNum": 0,
     *                 "startTime": 1551921600000,
     *                 "endTime": 1551922800000,
     *                 "timeType": 1,
     *                 "collectTime": 10,
     *                 "collectType": 1,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1552015471964,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1552015471964,
     *                 "isValid": "1"
     *             }
     *         ],
     *         "totalCount": 8
     *     }
     * }
     *
     * @return
     */
    @PostMapping("page/service")
    public Object pageService(String token,Long userId,Integer pageNum,Integer pageSize,boolean isService) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            QueryResult queryResult = userService.pageService(userId, pageNum, pageSize, isService);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看发布的服务/求助列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看发布的服务/求助列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 历史互助记录
     * @param token
     * @param userId
     * @param pageNum
     * @param pageSize
     *
     * {
     *     "success": true,
     *     "msg": "",
     *     "data": {
     *         "resultList": [
     *             {
     *                 "order": {
     *                     "id": 101433003871305728,
     *                     "serviceId": 101433003401543680,
     *                     "mainId": 101433003871305728,
     *                     "serviceName": "新版本重复2",
     *                     "servicePlace": 1,
     *                     "labels": "hehe,haha",
     *                     "type": 1,
     *                     "status": 2,
     *                     "source": 1,
     *                     "serviceTypeId": 15000,
     *                     "enrollNum": 0,
     *                     "confirmNum": 0,
     *                     "startTime": 1551936000000,
     *                     "endTime": 1551937200000,
     *                     "collectTime": 10,
     *                     "collectType": 1,
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1551965912545,
     *                     "updateUser": 68813260748488704,
     *                     "updateUserName": "马晓晨",
     *                     "updateTime": 1551965912545,
     *                     "isValid": "1"
     *                 },
     *                 "evaluates": [
     *                     {
     *                         "id": 69864082542428160,
     *                         "evaluateUserId": 68813258559062016,
     *                         "userId": 68813260748488704,
     *                         "orderId": 101433003871305728,
     *                         "creditEvaluate": 5,
     *                         "majorEvaluate": 5,
     *                         "attitudeEvaluate": 5,
     *                         "message": "贴膜贴得不错",
     *                         "labels": "心灵手巧,行家里手",
     *                         "createUser": 68813258559062016,
     *                         "createUserName": "刘维",
     *                         "createTime": 1544439295290,
     *                         "updateUser": 68813258559062016,
     *                         "updateUserName": "刘维",
     *                         "updateTime": 1544439295290,
     *                         "isValid": "1"
     *                     },
     *                     {
     *                         "id": 69864392849620992,
     *                         "evaluateUserId": 68813259653775360,
     *                         "userId": 68813260748488704,
     *                         "orderId": 101433003871305728,
     *                         "attitudeEvaluate": 5,
     *                         "message": "总摔，给我很多挣取时间币的机会",
     *                         "labels": "诚心正意",
     *                         "createUser": 68813259653775360,
     *                         "createUserName": "左岸",
     *                         "createTime": 1544439369272,
     *                         "updateUser": 68813259653775360,
     *                         "updateUserName": "左岸",
     *                         "updateTime": 1544439369272,
     *                         "isValid": "1"
     *                     }
     *                 ],
     *                 "user": {
     *                     "id": 68813260748488704,
     *                     "name": "马晓晨",
     *                     "userTel": "15122843051",
     *                     "jurisdiction": 0,
     *                     "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/15446050826379.png",
     *                     "userPicturePath": "http://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_background.png",
     *                     "vxOpenId": "oMgmu4vtWtuG_eFCMgvfJB8buPhI",
     *                     "vxId": "无",
     *                     "occupation": "",
     *                     "birthday": 19940704,
     *                     "sex": 1,
     *                     "maxEducation": "本科",
     *                     "followNum": 0,
     *                     "receiptNum": 0,
     *                     "remarks": "",
     *                     "level": 4,
     *                     "growthValue": 475,
     *                     "seekHelpNum": 18,
     *                     "serveNum": 10,
     *                     "surplusTime": 47,
     *                     "freezeTime": 210,
     *                     "creditLimit": 200,
     *                     "publicWelfareTime": 0,
     *                     "authenticationStatus": 2,
     *                     "authenticationType": 1,
     *                     "totalEvaluate": 146,
     *                     "creditEvaluate": 48,
     *                     "majorEvaluate": 49,
     *                     "attitudeEvaluate": 49,
     *                     "skill": "",
     *                     "integrity": 100,
     *                     "accreditStatus": 0,
     *                     "masterStatus": 0,
     *                     "authStatus": 1,
     *                     "inviteCode": "EuciNL",
     *                     "avaliableStatus": "1",
     *                     "isCompanyAccount": 0,
     *                     "userType": "1",
     *                     "createUser": 68813260748488704,
     *                     "createUserName": "马晓晨",
     *                     "createTime": 1537941095000,
     *                     "updateUser": 68813262698840064,
     *                     "updateUserName": "冰茬子",
     *                     "updateTime": 1548135643427,
     *                     "isValid": "1",
     *                     "praise": 8
     *                 }
     *             }
     *         ],
     *         "totalCount": 5
     *     }
     * }
     *
     * @return
     */
    @PostMapping("historyService")
    public Object historyService(String token,Long userId,Integer pageNum,Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            QueryResult queryResult = userService.historyService(user, userId, pageNum, pageSize);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看历史互助记录列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看历史互助记录列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 加入的组织列表信息
     * @param token
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("company/list")
    public Object companyList(String token,Long userId,Integer pageNum,Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            QueryResult<StrUserCompanyView> companies = companyService.getCompanyList(user,userId,pageNum,pageSize);
            result.setData(companies);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("加入的组织列表信息异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("加入的组织列表信息异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }


}
