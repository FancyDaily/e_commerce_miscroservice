package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.CompanyService;
import com.e_commerce.miscroservice.user.service.GrowthValueService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户模块
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

    @Autowired
    private GrowthValueService growthValueService;

    /**
     * 手机号验证码登录
     *
     * @param telephone 手机号
     * @param validCode 短信验证码
     * @return
     */
    @PostMapping("loginBySMS")
    public Object loginUserBySMS(String telephone, String validCode) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> resultMap = userService.loginUserBySMS(telephone, validCode);
            result.setSuccess(true);
            result.setData(resultMap);
        } catch (MessageException e) {
            result.setMsg("手机号验证码登录异常: " + e.getMessage());
            logger.error(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("手机号验证码登录异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 用户登出
     * @param token
     * @return
     */
    @PostMapping("logOut")
    public Object logOut(String token) {
        AjaxResult result = new AjaxResult();
        try {
            userService.logOut(token);
            result.setSuccess(true);
        } catch (MessageException e) {
            result.setMsg("用户登出 (旧版迁移)异常: " + e.getMessage());
            logger.error(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            logger.error("手用户登出 (旧版迁移)异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 时间轨迹
     *
     * @param token    登录凭证
     * @param ymString 年月字符串，eg.2019-01
     * @param option   操作，0全部，1收入，2支出
     *                 <p>
     *                 {
     *                 "success": true,
     *                 "msg": "",
     *                 "data": {
     *                 "monthTotalOut": 0,  //月度总支出
     *                 "total": 49, //总互助时
     *                 "month": "01",   //当前月份
     *                 "monthTotalIn": 37,  //月度总收入
     *                 "frozen": 23,    //冻结金额
     *                 "monthList": [
     *                 {
     *                 "idString": "82438795718295552", //主键
     *                 "id": 82438795718295552,
     *                 "userId": 68813260748488704,
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
     *                 },
     *                 {
     *                 "idString": "82435699785072640",
     *                 "id": 82435699785072640,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-14",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1547436602410,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547436602410,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "81365494354935808",
     *                 "id": 81365494354935808,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-11",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1547181445552,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547181445552,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "81073248518799360",
     *                 "id": 81073248518799360,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-10",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1547111768715,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547111768715,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "80740311462051840",
     *                 "id": 80740311462051840,
     *                 "userId": 68813260748488704,
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
     *                 },
     *                 {
     *                 "idString": "80739259920678912",
     *                 "id": 80739259920678912,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-09",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1547032139628,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1547032139628,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "80282449333977088",
     *                 "id": 80282449333977088,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-08",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1546923227497,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546923227497,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "79899354319552512",
     *                 "id": 79899354319552512,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-07",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1546831890528,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546831890528,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "78878228382482432",
     *                 "id": 78878228382482432,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-04",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1546588435133,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546588435133,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "78552166431719424",
     *                 "id": 78552166431719424,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-03",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1546510695906,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546510695906,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "idString": "78100464930914304",
     *                 "id": 78100464930914304,
     *                 "userId": 68813260748488704,
     *                 "type": 2,
     *                 "title": "奖励-签到",
     *                 "date": "2019-01-02",
     *                 "time": 3,
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "?",
     *                 "createTime": 1546403001876,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "?",
     *                 "updateTime": 1546403001876,
     *                 "isValid": "1"
     *                 }
     *                 ],
     *                 "vacant": 26 //可用金额
     *                 }
     *                 }
     * @return
     */
    @RequestMapping("payments")
    public Object payments(String token, String ymString, String option) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            Map<String, Object> payments = userService.payments(user, ymString, option);
            result.setData(payments);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("时间轨迹异常: " + e.getMessage());
        } catch (Exception e) {
            logger.error("时间轨迹异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 冻结明细
     *
     * @param token    登录凭证
     * @param lastTime 分页参数(说明：返回数据的按时间倒序排列。以下时间指代创建时间create_time: 向上翻页时，把当前最大的时间给我；向下翻页时，把当前最小的时间给我)
     * @param pageSize 每页最大条数
     *                 <p>
     *                 {
     *                 "resultList": [
     *                 {
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
     *                 }
     *                 ],
     *                 "totalCount": 1
     *                 }
     * @return
     */
    @RequestMapping("freezeList")
    public Object freezeList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            QueryResult<UserFreezeView> queryResult = userService.frozenList(user.getId(), lastTime, pageSize);
            result.setSuccess(true);
            result.setData(queryResult);
        } catch (MessageException e) {
            result.setMsg("冻结明细异常: " + e.getMessage());
            logger.error(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            logger.error("冻结明细异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 公益历程列表
     *
     * @param token    登录凭证
     * @param lastTime 参考冻结明细说明
     * @param pageSize 参考冻结明细说明
     * @param year     年份
     *                 <p>
     *                 {
     *                 "success": true,
     *                 "msg": "",
     *                 "data": {
     *                 "total": null,
     *                 "detailList": {
     *                 "resultList": [
     *                 {
     *                 "name": "zhangsan",  //项目名称
     *                 "time": "2019年4月3日", //时间
     *                 "coin": "3", //金额
     *                 "timeStamp": 1547445330978,
     *                 "in": true
     *                 },
     *                 {
     *                 "name": "zhangsan",
     *                 "time": "2019年4月3日",
     *                 "coin": "4",
     *                 "timeStamp": 1547445330978,
     *                 "in": true
     *                 }
     *                 ],
     *                 "totalCount": 0
     *                 },
     *                 "yearTotal": 7
     *                 }
     *                 }
     * @return
     */
    @RequestMapping("publicWelfareList")
    public Object publicWelfareList(String token, Long lastTime, Integer pageSize, Integer year) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            Map<String, Object> map = userService.publicWelfareList(user, lastTime, pageSize, year);
            result.setData(map);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("公益历程列表异常: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("公益历程列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看技能（包含详细信息）
     *
     * @param token 登录凭证
     *              {
     *              {
     *              "idString": "95167783989411840",
     *              "id": 95167783989411840,
     *              "userId": 68813260748488704,
     *              "name": "歌剧二",
     *              "description": "海豚音，大家好，才是真的好",
     *              "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545625655755109.png",
     *              "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png",
     *              "detailUrlArray": [
     *              "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png",
     *              "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png"
     *              ],
     *              "createUser": 68813260748488704,
     *              "createUserName": "盖伦😂",
     *              "createTime": 1550472167835,
     *              "updateUser": 68813260748488704,
     *              "updateUserName": "盖伦😂",
     *              "updateTime": 1550472167835,
     *              "isValid": "1"
     *              },
     *              {
     *              "idString": "95160769636728832",
     *              "id": 95160769636728832,
     *              "userId": 68813260748488704,
     *              "name": "书法",
     *              "description": "精通国学书法，三百年，好品质",
     *              "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154803914219080.png",
     *              "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png",
     *              "detailUrlArray": [
     *              "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png",
     *              "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png"
     *              ],
     *              "createUser": 68813260748488704,
     *              "createUserName": "盖伦😂",
     *              "createTime": 1550470495483,
     *              "updateUser": 68813260748488704,
     *              "updateUserName": "盖伦😂",
     *              "updateTime": 1550472853539,
     *              "isValid": "1"
     *              }
     *              }
     * @return
     */
    @RequestMapping("skill/list")
    public Object skillList(String token) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            UserSkillListView skillView = userService.skills(user);
            result.setData(skillView);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看技能异常: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 增加技能
     *
     * @param token       登录凭证
     * @param name        技能名
     * @param description 描述
     * @param headUrl     封面图
     * @param detailUrls  内容图,多张图片使用逗号分隔。
     *                    <p>
     *                    {
     *                    "success": true, //成功
     *                    "msg": ""
     *                    }
     * @return
     */
    @RequestMapping("skill/add")
    @Consume(TUserSkill.class)
    public Object skillAdd(String token, String name, String description, String headUrl, String detailUrls) {
        AjaxResult result = new AjaxResult();
        TUserSkill skill = (TUserSkill) ConsumeHelper.getObj();
        TUser user = (TUser) redisUtil.get(token);
        try {
            userService.skillAdd(user, skill);
            result.setSuccess(true);
        } catch (MessageException e) {
            result.setMsg(e.getMessage());
            logger.error("添加技能异常: " + e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 修改技能
     *
     * @param token       登录凭证
     * @param name        技能名
     * @param description 描述
     * @param headUrl     封面图
     * @param detailUrl   内容图，多张图使用逗号分隔
     *                    <p>
     *                    {
     *                    "success": true, //成功
     *                    "msg": ""
     *                    }
     * @return
     */
    @RequestMapping("skill/modify")
    @Consume(TUserSkill.class)
    public Object skillModify(String token, Long id, String name, String description, String headUrl, String detailUrl) {
        AjaxResult result = new AjaxResult();
        TUserSkill skill = (TUserSkill) ConsumeHelper.getObj();
        TUser user = (TUser) redisUtil.get(token);
        try {
            userService.skillModify(user, skill);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("修改技能异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除技能
     *
     * @param token 登录凭证
     * @param id    技能id
     *              <p>
     *              {
     *              "success": true, //成功
     *              "msg": ""
     *              }
     * @return
     */
    @RequestMapping("skill/delete")
    public Object skillDelete(String token, Long id) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            userService.skillDelete(id);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("删除技能异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 收藏列表
     *
     * @param token    登录凭证
     * @param pageNum  分页参数
     * @param pageSize 每页条数
     *                 <p>
     *                 {
     *                 "success": true,
     *                 "errorCode": "",
     *                 "msg": "",
     *                 "data": {
     *                 "resultList": [
     *                 {
     *                 "id": 101675590041468928,
     *                 "serviceId": 101675589445877760, //服务id
     *                 "mainId": 101675590041468928,
     *                 "nameAudioUrl": "",
     *                 "serviceName": "新版本重复7", //服务名
     *                 "servicePersonnel": 3,   //服务人数
     *                 "servicePlace": 1,   //服务场景 1线上 2线下
     *                 "labels": "hehe,haha",   //标签
     *                 "type": 1,   //服务类型 1服务 2求助
     *                 "status": 1, //服务状态
     *                 "source": 1, //服务来源 1个人 2组织（来自于组织时，该次为活动）
     *                 "serviceTypeId": 15000,
     *                 "addressName": "",
     *                 "longitude": "",
     *                 "latitude": "",
     *                 "totalEvaluate": "",
     *                 "enrollNum": 4,  //报名人数
     *                 "confirmNum": 2, //确认人数
     *                 "startTime": 1552526400000,  //开始时间
     *                 "endTime": 1552527600000,    //结束时间
     *                 "serviceStatus": "",
     *                 "openAuth": "",
     *                 "timeType": 1,   //服务重复属性 1单次 2重复
     *                 "collectTime": 10,   //收取金额
     *                 "collectType": 1,    //收取类型 1互助时 2公益时
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1552023749565,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1552023749565,
     *                 "companyId": "",
     *                 "isValid": "1"
     *                 }
     *                 ],
     *                 "totalCount": 1
     *                 }
     *                 }
     * @return
     */
    @RequestMapping("collect/list")
    public Object collectList(String token, Integer pageNum, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            QueryResult<List<TOrder>> queryResult = userService.collectList(user, pageNum, pageSize);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("收藏列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("收藏列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 收藏/取消收藏
     *
     * @param token   登录凭证
     * @param orderId 订单id
     *                <p>
     *                {
     *                "success": true,
     *                "errorCode": "",
     *                "msg": "",
     *                "data": ""
     *                }
     * @return
     */
    @RequestMapping("collect")
    public Object collect(String token, Long orderId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            userService.collect(user, orderId);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("收藏/取消收藏异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("收藏/取消收藏异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看用户基本信息
     *
     * @param token  登录凭证
     * @param userId 用户id
     *               <p>
     *               {
     *               "idString": "",
     *               "age": 88,   //年龄
     *               "isAtten": "",
     *               "authStatus": 1,//基本信息授权状态
     *               "vxId": "无",
     *               "masterStatus": "",//达人标识
     *               "maxLevelMin": "",
     *               "levelName": "",
     *               "needGrowthNum": "",
     *               "nextLevelName": "",
     *               "timeStamp": "",
     *               "id": 68813260748488704,
     *               "userAccount": "",
     *               "name": "马晓晨",   //昵称
     *               "jurisdiction": 0,
     *               "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/15446050826379.png",  //头像
     *               "userPicturePath": "http://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_background.png",   //个人主页背景
     *               "vxOpenId": "oMgmu4vtWtuG_eFCMgvfJB8buPhI",
     *               "occupation": "泡咖啡的",    //职业
     *               "workPlace": "消失信息", //公司(区分于组织)
     *               "college": "蓝翔挖掘机学院",    //学校
     *               "birthday": 19940704,    //生日
     *               "sex": 1,    //性别1男，2女
     *               "maxEducation": "本科",
     *               "followNum": 0,  //粉丝数
     *               "receiptNum": 0,
     *               "remarks": "",
     *               "level": 4,
     *               "growthValue": 475,
     *               "seekHelpNum": 18,
     *               "serveNum": 10,
     *               "seekHelpPublishNum": "",
     *               "servePublishNum": "",
     *               "surplusTime": 53,   //账户总额
     *               "freezeTime": 210,   //冻结金额
     *               "creditLimit": 200,  //授信
     *               "publicWelfareTime": 0,  //公益时
     *               "authenticationStatus": 2,   //实名认证状态 1未实名 2已实名
     *               "authenticationType": 1, //实名认证类型 1个人 2组织
     *               "servTotalEvaluate": 146,  //服务总分
     *               "servCreditEvaluate": 48,  //服务信用评分
     *               "servMajorEvaluate": 49,   //服务专业评分
     *               "servAttitudeEvaluate": 49,    //服务态度评分
     *               "helpTotalEvaluate": "",   //求助总分
     *               "helpCreditEvaluate": "",  //求助信用评分
     *               "helpMajorEvaluate": "",   //求助专业评分
     *               "helpAttitudeEvaluate": "",    //求助态度评分
     *               "companyNames": "晓时信息技术有限公司,长三角划水工程联盟,无偿献血公益",     //加入的组织名称
     *               "limitedCompanyNames": "晓时信息技术有限公司,长三角划水工程联盟", //用于展示的组织标签
     *               "skill": "",
     *               "integrity": 100,    //用户信息完整度
     *               "isCompanyAccount": 0,
     *               "userType": "1", //用户类型(区分个人、普通组织、公益组织的标签展示)
     *               "extend": "",
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1537941095000,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1552132162162,
     *               "isValid": "1",
     *               "inviteCode": "EuciNL",
     *               "idStr": "",
     *               "joinCompany": false
     *               }
     * @return
     */
    @RequestMapping("infos")
    public Object info(String token, Long userId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            DesensitizedUserView userView = userService.info(user, userId);
            result.setData(userView);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看个人基本信息异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看个人基本信息异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看个人主页(基本信息、技能列表、提供的服务、提供的求助)
     *
     * @param token  登录凭证
     * @param userId 目标用户的id
     *               <p>
     *               {
     *               "success": true,
     *               "msg": "",
     *               "data": {
     *               "desensitizedUserView": {
     *               "isAtten": 0,    //我与对方的关注状态，0未关注，1我已关注她，2我们俩互关
     *               "authStatus": 1,
     *               "vxId": "无",
     *               "id": 68813260748488704,
     *               "name": "马晓晨",   //昵称
     *               "jurisdiction": 0,
     *               "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/15446050826379.png",  //用户头像
     *               "userPicturePath": "http://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_background.png",   //个人主页背景图
     *               "vxOpenId": "oMgmu4vtWtuG_eFCMgvfJB8buPhI",  //openId
     *               "occupation": "",
     *               "birthday": 19940704,    //生日
     *               "sex": 1,    //性别，1男，2女
     *               "maxEducation": "本科",    //最大学历
     *               "followNum": 0,  //粉丝数量
     *               "receiptNum": 0,
     *               "remarks": "",
     *               "level": 4,
     *               "growthValue": 475,  //成长值
     *               "seekHelpNum": 18,
     *               "serveNum": 10,
     *               "surplusTime": 47,   //总金额
     *               "freezeTime": 210,   //冻结金额
     *               "creditLimit": 200,  //授信额度
     *               "publicWelfareTime": 0,  //公益时
     *               "authenticationStatus": 2, //实名认证状态,1未认证，2已认证
     *               "authenticationType": 1, //实名认证类型，1个人认证，2组织认证
     *               "totalEvaluate": 146, //三项评分总和
     *               "creditEvaluate": 48,    //信用评分
     *               "majorEvaluate": 49, //专业评分
     *               "attitudeEvaluate": 49,  //态度评分
     *               "skill": "",
     *               "integrity": 100,    //用户完整度
     *               "isCompanyAccount": 0,   //是否为组织账号 0个人账号 1组织账号(个人账号提交组织认证类型的请求得到处理通过之后，会诞生一个组织账号)
     *               "userType": "1", //用户类型 用于确定显示何种标签,1个人 2普通组织(除公益组织之外的组织) 3公益组织
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1537941095000,
     *               "updateUser": 68813262698840064,
     *               "updateUserName": "冰茬子",
     *               "updateTime": 1548135643427,
     *               "isValid": "1",
     *               "inviteCode": "EuciNL",
     *               "joinCompany": false
     *               },
     *               "services": {
     *               "resultList": [
     *               {
     *               "id": 101430540338462777,
     *               "serviceId": 101430539319246848,
     *               "mainId": 101430540338462720,
     *               "serviceName": "可重复读Repeatable Read777", //名字
     *               "servicePlace": 1,   //线上
     *               "labels": "hehe,haha",   //标签
     *               "type": 2,   //服务
     *               "status": 1, //状态
     *               "source": 1, //来源, 1个人，2组织
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,  //报名人数
     *               "confirmNum": 0, //确认人数
     *               "startTime": 1552022400000,  //开始时间
     *               "endTime": 1552023600000,    //结束时间
     *               "timeType": 1,   //时间类型,0指定时间，1可重复
     *               "collectTime": 10,   //单价
     *               "collectType": 1,    //收取类型,1互助时，2公益时
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1551965325000,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1551965325062,
     *               "isValid": "1"
     *               }
     *               ],
     *               "totalCount": 1
     *               },
     *               "helps": {
     *               "resultList": [
     *               {
     *               "id": 101675891532234752,
     *               "serviceId": 101675890827591680,
     *               "mainId": 101675891532234752,
     *               "serviceName": "新版本重复9",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 1,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,
     *               "confirmNum": 0,
     *               "startTime": 1552267200000,
     *               "endTime": 1552268400000,
     *               "timeType": 1,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1552023821420,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1552023821420,
     *               "isValid": "1"
     *               },
     *               {
     *               "id": 101641267686932480,
     *               "serviceId": 101641267305250816,
     *               "mainId": 101641267686932480,
     *               "serviceName": "新版本重复6",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 1,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,
     *               "confirmNum": 0,
     *               "startTime": 1551921600000,
     *               "endTime": 1551922800000,
     *               "timeType": 1,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1552015566529,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1552015566529,
     *               "isValid": "1"
     *               },
     *               {
     *               "id": 101640613451005952,
     *               "serviceId": 101640612591173632,
     *               "mainId": 101640613451005952,
     *               "serviceName": "新版本重复4",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 1,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,
     *               "confirmNum": 0,
     *               "startTime": 1552612800000,
     *               "endTime": 1552614000000,
     *               "timeType": 1,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1552015410433,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1552015410433,
     *               "isValid": "1"
     *               },
     *               {
     *               "id": 101640871165820928,
     *               "serviceId": 101640870670893056,
     *               "mainId": 101640871165820928,
     *               "serviceName": "新版本重复5",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 1,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,
     *               "confirmNum": 0,
     *               "startTime": 1551921600000,
     *               "endTime": 1551922800000,
     *               "timeType": 1,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1552015471964,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1552015471964,
     *               "isValid": "1"
     *               },
     *               {
     *               "id": 101675590041468928,
     *               "serviceId": 101675589445877760,
     *               "mainId": 101675590041468928,
     *               "serviceName": "新版本重复7",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 1,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 3,
     *               "confirmNum": 0,
     *               "startTime": 1552526400000,
     *               "endTime": 1552527600000,
     *               "timeType": 1,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1552023749565,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1552023749565,
     *               "isValid": "1"
     *               },
     *               {
     *               "id": 101637659306229760,
     *               "serviceId": 101637658576420864,
     *               "mainId": 101637659306229760,
     *               "serviceName": "新版本重复3",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 1,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,
     *               "confirmNum": 0,
     *               "startTime": 1552008000000,
     *               "endTime": 1552009200000,
     *               "timeType": 1,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1552014706141,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1552014706141,
     *               "isValid": "1"
     *               },
     *               {
     *               "id": 101430540338462999,
     *               "serviceId": 101430539319246848,
     *               "mainId": 101430540338462720,
     *               "serviceName": "幻想读Serializable999",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 1,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,
     *               "confirmNum": 0,
     *               "startTime": 1552022400000,
     *               "endTime": 1552023600000,
     *               "timeType": 0,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1551965325000,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1551965325062,
     *               "isValid": "1"
     *               },
     *               {
     *               "id": 101430540338462888,
     *               "serviceId": 101430539319246848,
     *               "mainId": 101430540338462720,
     *               "serviceName": "读已提交Read Commited888",
     *               "servicePlace": 1,
     *               "labels": "hehe,haha",
     *               "type": 1,
     *               "status": 2,
     *               "source": 1,
     *               "serviceTypeId": 15000,
     *               "enrollNum": 0,
     *               "confirmNum": 0,
     *               "startTime": 1552022400000,
     *               "endTime": 1552023600000,
     *               "timeType": 0,
     *               "collectTime": 10,
     *               "collectType": 1,
     *               "createUser": 68813260748488704,
     *               "createUserName": "马晓晨",
     *               "createTime": 1551965325000,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "马晓晨",
     *               "updateTime": 1551965325062,
     *               "isValid": "1"
     *               }
     *               ],
     *               "totalCount": 8
     *               },
     *               "skills": {
     *               "skillCnt": 2,
     *               "userSkills": [
     *               {
     *               "idString": "95167783989411840", //技能id
     *               "id": 95167783989411840,
     *               "userId": 68813260748488704,
     *               "name": "歌剧二",//技能名
     *               "description": "海豚音，大家好，才是真的好",//技能描述
     *               "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545625655755109.png",//技能封面
     *               "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png",
     *               "detailUrlArray": [  //详细内容图
     *               "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154562668004844.png",
     *               "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/1545702131965137.png"
     *               ],
     *               "createUser": 68813260748488704,
     *               "createUserName": "盖伦😂",
     *               "createTime": 1550472167835,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "盖伦😂",
     *               "updateTime": 1550472167835,
     *               "isValid": "1"
     *               },
     *               {
     *               "idString": "95160769636728832",
     *               "id": 95160769636728832,
     *               "userId": 68813260748488704,
     *               "name": "书法",
     *               "description": "精通国学书法，三百年，好品质",
     *               "headUrl": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154803914219080.png",
     *               "detailUrls": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png",
     *               "detailUrlArray": [
     *               "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154502975982719.png",
     *               "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/release/154443075139314.png"
     *               ],
     *               "createUser": 68813260748488704,
     *               "createUserName": "盖伦😂",
     *               "createTime": 1550470495483,
     *               "updateUser": 68813260748488704,
     *               "updateUserName": "盖伦😂",
     *               "updateTime": 1550472853539,
     *               "isValid": "1"
     *               }
     *               ]
     *               }
     *               }
     *               }
     * @return
     */
    @RequestMapping("page")
    public Object page(String token, Long userId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
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
            logger.error("查看个人主页异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看发布的服务/求助列表(个人主页)
     *
     * @param token     登录凭证
     * @param userId    用户编号
     * @param pageNum   分页页码
     * @param pageSize  分页大小
     * @param isService 是否为服务
     *                  {
     *                  "success": true,
     *                  "msg": "",
     *                  "data": {
     *                  "resultList": [
     *                  {
     *                  "id": 101430540338462777,
     *                  "serviceId": 101430539319246848,
     *                  "mainId": 101430540338462720,
     *                  "serviceName": "可重复读Repeatable Read777", //名字
     *                  "servicePlace": 1,   //线上
     *                  "labels": "hehe,haha",   //标签
     *                  "type": 2,   //服务
     *                  "status": 1, //状态
     *                  "source": 1, //来源, 1个人，2组织
     *                  "serviceTypeId": 15000,
     *                  "enrollNum": 0,  //报名人数
     *                  "confirmNum": 0, //确认人数
     *                  "startTime": 1552022400000,  //开始时间
     *                  "endTime": 1552023600000,    //结束时间
     *                  "timeType": 1,   //时间类型,0指定时间，1可重复
     *                  "collectTime": 10,   //单价
     *                  "collectType": 1,    //收取类型,1互助时，2公益时
     *                  "createUser": 68813260748488704, //创建人发单者
     *                  "createUserName": "马晓晨",
     *                  "createTime": 1551965325000,
     *                  "updateUser": 68813260748488704,
     *                  "updateUserName": "马晓晨",
     *                  "updateTime": 1551965325062,
     *                  "isValid": "1"
     *                  },
     *                  {
     *                  "id": 101641267686932480,
     *                  "serviceId": 101641267305250816,
     *                  "mainId": 101641267686932480,
     *                  "serviceName": "新版本重复6",
     *                  "servicePlace": 1,
     *                  "labels": "hehe,haha",
     *                  "type": 1,
     *                  "status": 1,
     *                  "source": 1,
     *                  "serviceTypeId": 15000,
     *                  "enrollNum": 0,
     *                  "confirmNum": 0,
     *                  "startTime": 1551921600000,
     *                  "endTime": 1551922800000,
     *                  "timeType": 1,
     *                  "collectTime": 10,
     *                  "collectType": 1,
     *                  "createUser": 68813260748488704,
     *                  "createUserName": "马晓晨",
     *                  "createTime": 1552015566529,
     *                  "updateUser": 68813260748488704,
     *                  "updateUserName": "马晓晨",
     *                  "updateTime": 1552015566529,
     *                  "isValid": "1"
     *                  },
     *                  {
     *                  "id": 101640613451005952,
     *                  "serviceId": 101640612591173632,
     *                  "mainId": 101640613451005952,
     *                  "serviceName": "新版本重复4",
     *                  "servicePlace": 1,
     *                  "labels": "hehe,haha",
     *                  "type": 1,
     *                  "status": 1,
     *                  "source": 1,
     *                  "serviceTypeId": 15000,
     *                  "enrollNum": 0,
     *                  "confirmNum": 0,
     *                  "startTime": 1552612800000,
     *                  "endTime": 1552614000000,
     *                  "timeType": 1,
     *                  "collectTime": 10,
     *                  "collectType": 1,
     *                  "createUser": 68813260748488704,
     *                  "createUserName": "马晓晨",
     *                  "createTime": 1552015410433,
     *                  "updateUser": 68813260748488704,
     *                  "updateUserName": "马晓晨",
     *                  "updateTime": 1552015410433,
     *                  "isValid": "1"
     *                  },
     *                  {
     *                  "id": 101640871165820928,
     *                  "serviceId": 101640870670893056,
     *                  "mainId": 101640871165820928,
     *                  "serviceName": "新版本重复5",
     *                  "servicePlace": 1,
     *                  "labels": "hehe,haha",
     *                  "type": 1,
     *                  "status": 1,
     *                  "source": 1,
     *                  "serviceTypeId": 15000,
     *                  "enrollNum": 0,
     *                  "confirmNum": 0,
     *                  "startTime": 1551921600000,
     *                  "endTime": 1551922800000,
     *                  "timeType": 1,
     *                  "collectTime": 10,
     *                  "collectType": 1,
     *                  "createUser": 68813260748488704,
     *                  "createUserName": "马晓晨",
     *                  "createTime": 1552015471964,
     *                  "updateUser": 68813260748488704,
     *                  "updateUserName": "马晓晨",
     *                  "updateTime": 1552015471964,
     *                  "isValid": "1"
     *                  }
     *                  ],
     *                  "totalCount": 8
     *                  }
     *                  }
     * @return
     */
    @RequestMapping("page/service")
    public Object pageService(String token, Long userId, Integer pageNum, Integer pageSize, boolean isService) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            QueryResult queryResult = userService.pageService(userId, pageNum, pageSize, isService,user);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看发布的服务/求助列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看发布的服务/求助列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 历史互助记录
     *
     * @param token    登录凭证
     * @param userId   用户编号
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     *                 <p>
     *                 {
     *                 "success": true,
     *                 "msg": "",
     *                 "data": {
     *                 "resultList": [
     *                 {
     *                 "order": {   //主体（1）
     *                 "id": 101433003871305728,    //订单id
     *                 "serviceId": 101433003401543680, //商品(服务/求助)的id
     *                 "mainId": 101433003871305728,    //主订单id
     *                 "serviceName": "新版本重复2", //名称
     *                 "servicePlace": 1,   //类型，1线上2线下
     *                 "labels": "hehe,haha",   //标签
     *                 "type": 1,   //类型 1服务2求助
     *                 "status": 2, //状态
     *                 "source": 1, //来源 1个人2组织
     *                 "serviceTypeId": 15000,  //服务类型代号
     *                 "enrollNum": 0,  //报名人数
     *                 "confirmNum": 0, //确认人数
     *                 "startTime": 1551936000000,  //开始时间
     *                 "endTime": 1551937200000,    //结束时间
     *                 "collectTime": 10,   //收取价格
     *                 "collectType": 1,    //收取类型 1互助时2公益时
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1551965912545,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1551965912545,
     *                 "isValid": "1"
     *                 },
     *                 "evaluates": [   //附带的评价(多)
     *                 {
     *                 "id": 69864082542428160,
     *                 "evaluateUserId": 68813258559062016, //评价者id
     *                 "userId": 68813260748488704, //被评价者id
     *                 "orderId": 101433003871305728,   //订单id
     *                 "creditEvaluate": 5, //信用评分
     *                 "majorEvaluate": 5,  //专业评分
     *                 "attitudeEvaluate": 5,   //态度评分
     *                 "message": "贴膜贴得不错", //评价的内容
     *                 "labels": "心灵手巧,行家里手",   //评价标签
     *                 "createUser": 68813258559062016,
     *                 "createUserName": "刘维",
     *                 "createTime": 1544439295290,
     *                 "updateUser": 68813258559062016,
     *                 "updateUserName": "刘维",
     *                 "updateTime": 1544439295290,
     *                 "isValid": "1"
     *                 },
     *                 {
     *                 "id": 69864392849620992,
     *                 "evaluateUserId": 68813259653775360,
     *                 "userId": 68813260748488704,
     *                 "orderId": 101433003871305728,
     *                 "attitudeEvaluate": 5,
     *                 "message": "总摔，给我很多挣取时间币的机会",
     *                 "labels": "诚心正意",
     *                 "createUser": 68813259653775360,
     *                 "createUserName": "左岸",
     *                 "createTime": 1544439369272,
     *                 "updateUser": 68813259653775360,
     *                 "updateUserName": "左岸",
     *                 "updateTime": 1544439369272,
     *                 "isValid": "1"
     *                 }
     *                 ],
     *                 "user": {    //配套的用户信息
     *                 "id": 68813260748488704,
     *                 "name": "马晓晨",
     *                 "userTel": "15122843051",
     *                 "jurisdiction": 0,
     *                 "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/15446050826379.png",
     *                 "userPicturePath": "http://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_background.png",
     *                 "vxOpenId": "oMgmu4vtWtuG_eFCMgvfJB8buPhI",
     *                 "vxId": "无",
     *                 "occupation": "",
     *                 "birthday": 19940704,
     *                 "sex": 1,
     *                 "maxEducation": "本科",
     *                 "followNum": 0,
     *                 "receiptNum": 0,
     *                 "remarks": "",
     *                 "level": 4,
     *                 "growthValue": 475,
     *                 "seekHelpNum": 18,
     *                 "serveNum": 10,  //作为服务者的被评分次数,各项平均分=单项服务总评分/作为服务者的被评分次数
     *                 "surplusTime": 47,
     *                 "freezeTime": 210,
     *                 "creditLimit": 200,
     *                 "publicWelfareTime": 0,  //公益时
     *                 "authenticationStatus": 2,
     *                 "authenticationType": 1,
     *                 "serv_total_evaluate": 146,
     *                 "serv_credit_evaluate": 48,  //服务总信用评分
     *                 "serv_major_evaluate": 49,   //服务总专业评分
     *                 "serv_attitude_evaluate": 49,    //服务总态度评分
     *                 "skill": "",
     *                 "integrity": 100,    //用户信息完整度
     *                 "accreditStatus": 0,
     *                 "masterStatus": 0,   //达人标识
     *                 "authStatus": 1, //基本信息授权状态
     *                 "inviteCode": "EuciNL",
     *                 "avaliableStatus": "1",  //可用状态
     *                 "isCompanyAccount": 0,   //是否为组织账号
     *                 "userType": "1", //用户类型 1个人2公益组织3一般组织
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1537941095000,
     *                 "updateUser": 68813262698840064,
     *                 "updateUserName": "冰茬子",
     *                 "updateTime": 1548135643427,
     *                 "isValid": "1",
     *                 "praise": 8
     *                 }
     *                 }
     *                 ],
     *                 "totalCount": 1
     *                 }
     *                 }
     * @return
     */
    @RequestMapping("historyService")
    public Object historyService(String token, Long userId, Integer pageNum, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
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
            logger.error("查看历史互助记录列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 加入的组织列表信息
     *
     * @param token    登录凭证
     * @param userId   用户编号
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     *                 <p>
     *                 {
     *                 "success": true,
     *                 "errorCode": "",
     *                 "msg": "",
     *                 "data": {
     *                 "resultList": [
     *                 {
     *                 "num": 1,    //组织人数
     *                 "companyIdString": "80363494481854464",  //组织id
     *                 "id": 1111,
     *                 "companyId": 80363494481854464,
     *                 "userId": 68813260748488704,
     *                 "companyName": "黑龙江现观商业管理有限公司",  //组织名
     *                 "companyJob": "",    //组内角色 0创建者 1成员
     *                 "teamName": "",  //组织内昵称
     *                 "teamJob": "",
     *                 "teamUserCode": "",
     *                 "extend": "",
     *                 "createUser": "",
     *                 "createUserName": "",
     *                 "createTime": "",
     *                 "updateUser": "",
     *                 "updateUserName": "",
     *                 "updateTime": 1231231231,
     *                 "isValid": "1"
     *                 }
     *                 ],
     *                 "totalCount": 1
     *                 }
     *                 }
     * @return
     */
    @RequestMapping("company/list")
    public Object companyList(String token, Long userId, Integer pageNum, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            QueryResult<StrUserCompanyView> companies = companyService.getCompanyList(user, userId, pageNum, pageSize);
            result.setData(companies);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("加入的组织列表信息异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加入的组织列表信息异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 组织发布的活动列表
     *
     * @param token     登录凭证
     * @param companyId 组织id
     * @param pageNum   页码
     * @param pageSize  每页条数
     *                  <p>
     *                  {
     *                  "success": true,
     *                  "errorCode": "",
     *                  "msg": "",
     *                  "data": {
     *                  "resultList": [
     *                  {
     *                  "idString": "101430540338461080",
     *                  "serviceIdString": "101430539319246848", //商品id
     *                  "id": 101430540338461080,
     *                  "serviceId": 101430539319246848,
     *                  "mainId": 101430540338462720,
     *                  "nameAudioUrl": "",
     *                  "serviceName": "脏读READ UNCOMMITTED101010",   //名称
     *                  "servicePersonnel": 3,   //预设人数
     *                  "servicePlace": 1,   //线上线下  1线上2线下
     *                  "labels": "hehe,haha",
     *                  "type": 1,   //服务类型 1服务2求助
     *                  "status": 1, //状态
     *                  "source": 2, //来源 1个人 2组织
     *                  "serviceTypeId": 15000,
     *                  "addressName": "",   //地址
     *                  "longitude": "",
     *                  "latitude": "",
     *                  "totalEvaluate": "",
     *                  "enrollNum": 0,
     *                  "confirmNum": 0,
     *                  "startTime": 1552022400000,  //开始时间
     *                  "endTime": 1552023600000,    //结束时间
     *                  "serviceStatus": "",
     *                  "openAuth": "",
     *                  "timeType": 0,   //订单对应商品的重复属性 0指定时间1可重复
     *                  "collectTime": 10,   //单价
     *                  "collectType": 1,    //货币类型 1互助式2公益时
     *                  "createUser": 68813260970786816,
     *                  "createUserName": "马晓晨",
     *                  "createTime": 1551965325000,
     *                  "updateUser": 68813260970786816,
     *                  "updateUserName": "马晓晨",
     *                  "updateTime": 1551965325062,
     *                  "companyId": "",
     *                  "isValid": "1"
     *                  }
     *                  ],
     *                  "totalCount": 1
     *                  }
     *                  }
     * @return
     */
    @RequestMapping("company/social/list")
    public Object companySocialList(String token, Long companyId, Integer pageNum, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            QueryResult<StrServiceView> activityList = companyService.getActivityList(companyId, pageNum, pageSize);
            result.setData(activityList);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组织发布的活动列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("组织发布的活动列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 组织发布的我参与的活动列表
     *
     * @param token     登录凭证
     * @param companyId 组织编号
     * @param pageNum   分页页码
     * @param pageSize  分页大小
     *                  <p>
     *                  {
     *                  "success": true,
     *                  "errorCode": "",
     *                  "msg": "",
     *                  "data": {
     *                  "resultList": [  //参考组织发布的活动接口
     *                  {
     *                  "idString": "101430540338461080",
     *                  "serviceIdString": "101430539319246848",
     *                  "id": 101430540338461080,
     *                  "serviceId": 101430539319246848,
     *                  "mainId": 101430540338462720,
     *                  "nameAudioUrl": "",
     *                  "serviceName": "脏读READ UNCOMMITTED101010",
     *                  "servicePersonnel": 3,
     *                  "servicePlace": 1,
     *                  "labels": "hehe,haha",
     *                  "type": 1,
     *                  "status": 1,
     *                  "source": 2,
     *                  "serviceTypeId": 15000,
     *                  "addressName": "",
     *                  "longitude": "",
     *                  "latitude": "",
     *                  "totalEvaluate": "",
     *                  "enrollNum": 0,
     *                  "confirmNum": 0,
     *                  "startTime": 1552022400000,
     *                  "endTime": 1552023600000,
     *                  "serviceStatus": "",
     *                  "openAuth": "",
     *                  "timeType": 0,
     *                  "collectTime": 10,
     *                  "collectType": 1,
     *                  "createUser": 68813260970786816,
     *                  "createUserName": "马晓晨",
     *                  "createTime": 1551965325000,
     *                  "updateUser": 68813260970786816,
     *                  "updateUserName": "马晓晨",
     *                  "updateTime": 1551965325062,
     *                  "companyId": "",
     *                  "isValid": "1"
     *                  }
     *                  ],
     *                  "totalCount": 1
     *                  }
     *                  }
     * @return
     */
    @RequestMapping("company/social/list/mine")
    public Object companySocialListMine(String token, Long companyId, Integer pageNum, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            QueryResult<StrServiceView> myActivityList = companyService.getMyActivityList(user.getId(), companyId, pageNum, pageSize);
            result.setData(myActivityList);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组织发布的活动列表异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("组织发布的活动列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }


    /**
     * 用户信息修改(包括修改手机号码)
     *
     * @param token                登录凭证
     * @param name                 昵称
     * @param userTel              手机号
     * @param userHeadPortraitPath 头像
     * @param userPicturePath      背景
     * @param occupation           职业
     * @param workPlace            公司
     * @param college              学校
     * @param age                  年龄
     * @param sex                  性别 1男 2女
     * @param vxId                 微信号
     * @param remarks              个人宣言
     *                             {
     *                             "success": true,
     *                             "errorCode": "",
     *                             "msg": "",
     *                             "data": ""
     *                             }
     * @return
     */
    @RequestMapping("modify")
    @Consume(TUser.class)
    public Object modify(String token, String name, String userTel, String userHeadPortraitPath, String userPicturePath, String occupation, String workPlace, String college, Integer age, Integer sex, String vxId, String remarks) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) ConsumeHelper.getObj();
        try {
            token = userService.modify(token, user);
            result.setData(token);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("用户信息修改异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户信息修改异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 预创建一个红包
     *
     * @param token       登录凭证
     * @param description 描述
     * @param time        金额
     *                    {
     *                    "success": true,
     *                    "errorCode": "",
     *                    "msg": "",
     *                    "data": {
     *                    "id": 102130274443198464,    //红包id
     *                    "userId": 68813260748488704, //用户id
     *                    "description": "张三牛逼",   //红包描述
     *                    "time": 7,   //红包金额
     *                    "createUser": 68813260748488704,
     *                    "createUserName": "",
     *                    "createTime": 1552132154916,
     *                    "updateUser": 68813260748488704,
     *                    "updateUserName": "",
     *                    "updateTime": 1552132154916,
     *                    "isValid": "0"
     *                    }
     *                    }
     * @return
     */
    @RequestMapping("bonusPackage/preGenerate")
    @Consume(TBonusPackage.class)
    public Object bonusPackagePreGenerate(String token, String description, Long time) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            TBonusPackage bonusPackage = (TBonusPackage) ConsumeHelper.getObj();
            TBonusPackage bonus = userService.preGenerateBonusPackage(user, bonusPackage);
            result.setData(bonus);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("预创建红包异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("预创建红包异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 创建一个红包
     *
     * @param token          登录凭证
     * @param bonusPackageId    红包编号
     * {
     *                       "success": true,
     *                       "errorCode": "",
     *                       "msg": "",
     *                       "data": ""
     *                       }
     * @return
     */
    @RequestMapping("bonusPackage/generate")
    public Object bonusPackageGenerate(String token, Long bonusPackageId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            userService.generateBonusPackage(user, bonusPackageId);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("创建红包异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建红包异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看一个红包
     *
     * @param token          登录凭证
     * @param bonusPackageId 红包编号
     *                       {
     *                       "id": 102130274443198464,
     *                       "userId": 68813260748488704,
     *                       "description": "张三牛逼",
     *                       "time": 7,
     *                       "createUser": 68813260748488704,
     *                       "createUserName": "",
     *                       "createTime": 1552132154916,
     *                       "updateUser": 68813260748488704,
     *                       "updateUserName": "马晓晨",
     *                       "updateTime": 1552132162244,
     *                       "isValid": "1"
     *                       }
     * @return
     */
    @RequestMapping("bonusPackage/infos")
    public Object bonusPackageInfo(String token, Long bonusPackageId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            BonusPackageVIew bonusPackage = userService.bonusPackageInfo(user, bonusPackageId);
            result.setData(bonusPackage);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("查看红包异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看红包异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 打开红包
     *
     * @param token          登录凭证
     * @param bonusPackageId 红包编号
     *                       {
     *                       "success": false,
     *                       "errorCode": "",
     *                       "msg": "您不能领取自己的红包!",
     *                       "data": ""
     *                       }
     * @return
     */
    @RequestMapping("bonusPackage/open")
    public Object bonusPackageOpen(String token, String bonusPackageId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            Long bonusId = Long.valueOf(bonusPackageId);
            userService.openBonusPackage(user, bonusId);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("打开红包异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("打开红包异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 红包退回（超时）
     *
     * @param token          登录凭证
     * @param bonusPackageId 红包编号
     * @return
     */
    @RequestMapping("bonusPackage/sendBack")
    public Object bonusPackageSendBack(String token, Long bonusPackageId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            userService.sendBackBonusPackage(user, bonusPackageId);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("红包退回异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("红包退回异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看是否为我的红包
     *
     * @param token          登录凭证
     * @param bonusPackageId 红包编号
     * @return
     */
    @RequestMapping("bonusPackage/isMine")
    public Object isMyBonusPackage(String token, Long bonusPackageId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            Map<String, Object> resultMap = userService.isMyBonusPackage(user, bonusPackageId);
            result.setData(resultMap);
            result.setSuccess(true);
            if(resultMap==null) {
                result.setSuccess(false);
            }
        } catch (MessageException e) {
            logger.error("查看是否为我的红包异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查看是否为我的红包异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 用户认证信息更新(实名认证)
     *
     * @param token    登录凭证
     * @param cardId   身份证号
     * @param cardName 身份证名字
     *                 <p>
     *                 {
     *                 "success": false,
     *                 "errorCode": "",
     *                 "msg": "必要身份证参数不全！",
     *                 "data": ""
     *                 }
     * @return
     */
    @RequestMapping("cert")
    @Consume(TUserAuth.class)
    public Object cert(String token, String cardId, String cardName) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            userService.auth(token, user, cardId, cardName);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("个人认证信息提交异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("个人认证信息提交异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 单位认证信息更新
     *
     * @param token    登录凭证
     * @param type     组织类型
     * @param name     组织名字
     * @param province 省份
     * @param city     城市
     * @param county   区/县
     * @param depict   描述
     *                 <p>
     *                 {
     *                 "success": false,
     *                 "errorCode": "",
     *                 "msg": "单位信息不能为空!",
     *                 "data": ""
     *                 }
     * @return
     */
    @Consume(TCompany.class)
    @RequestMapping("company/cert")
    public Object companyAuth(String token, Integer type, String name, String province, String city, String county, String depict, String url, String contactsName, String contactsTel, String contactsCardId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        TCompany company = (TCompany) ConsumeHelper.getObj();
        try {
            userService.companyAuth(user, company);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组织审核信息提交异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("组织审核信息提交异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 认证信息查询
     *
     * @param token 登录凭证
     *              {
     *              "success": true,
     *              "errorCode": "",
     *              "msg": "",
     *              "data": {
     *              "id": 102845147649146880,
     *              "userId": 68813260748488704,
     *              "type": 1,   //组织类型(广义) 1民办非企业组织，2社会团体，3事业单位，4政府机关，5企业
     *              "status": 0, //认证进行中（待审核）
     *              "name": "张三角发达公司",   //组织名
     *              "depict": "张三角发达公司致力于发展发达文化，我们承诺绝不传销",   //组织描述
     *              "province": "浙江省",   //省份
     *              "city": "杭州市",   //城市
     *              "county": "上城区", //区/县
     *              "code": "",
     *              "legalPerson": "",
     *              "startTime": "",
     *              "endTime": "",
     *              "url": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/15446050826379.png",   //证明图片（多张图片请用分隔）
     *              "contactsName": "马晓晨",
     *              "contactsTel": "13546646541213",
     *              "contactsCardId": "33102119940231515616",
     *              "extend": "",
     *              "createUser": 68813260748488704,
     *              "createUserName": "",
     *              "createTime": 1552302593970,
     *              "updateUser": 68813260748488704,
     *              "updateUserName": "",
     *              "updateTime": 1552302593970,
     *              "isValid": "1"
     *              }
     *              }
     * @return
     */
    @PostMapping("company/infos")
    public Object companyInfo(String token) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            Map<String, Object> map = companyService.companyInfo(user);
            result.setData(map);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("认证信息查询异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("认证信息查询异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 每日签到信息查询
     *
     * @param token    登录凭证
     * @param ymString 年月字符串,eg.2019-03
     *
     *                 <p>
     *                 {
     *                 "bonus7": "",    //特殊奖励金额
     *                 "state": true,   //签到状态： true已签到，false未签到
     *                 "count": 1,      //累计签到天数
     *                 "cycleArray": [  //签到连续串示意数组
     *                 "今日",
     *                 "2019-03-12",
     *                 "2019-03-13",
     *                 "2019-03-14",
     *                 "2019-03-15",
     *                 "2019-03-16",
     *                 "2019-03-17"
     *                 ],
     *                 "signUpList": [  //签到日历数组
     *                 {
     *                 "updateDate": "2019-03-11",  //表示签到的日期
     *                 "createDate": "2019-03-11",
     *                 "idString": "102816831982534656",
     *                 "id": 102816831982534656,
     *                 "entityId": "",
     *                 "type": 3,
     *                 "subType": "",
     *                 "value": "",
     *                 "targetId": "",
     *                 "targetNum": "",
     *                 "createUser": 68813260748488704,
     *                 "createUserName": "马晓晨",
     *                 "createTime": 1552295842989,
     *                 "updateUser": 68813260748488704,
     *                 "updateUserName": "马晓晨",
     *                 "updateTime": 1552295842989,
     *                 "isValid": "1"
     *                 }
     *                 ]
     *                 }
     * @return
     */
    @PostMapping("signUpInfo")
    public Object signUpInfo(String token, String ymString) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            SignUpInfoView signUpInfo = userService.signUpInfo(user, ymString);
            result.setSuccess(true);
            result.setData(signUpInfo);
        } catch (MessageException e) {
            logger.error("每日签到信息查询异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("每日签到信息查询异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 每日签到
     *
     * @param token 登录凭证
     *              <p>
     *              {
     *              "success": true,
     *              "errorCode": "",
     *              "msg": "",
     *              "data": 3    //本次获得奖励金额
     *              }
     * @return
     */
    @PostMapping("/signUp")
    public Object signUp(String token) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            long reward = userService.signUp(token, user);
            result.setSuccess(true);
            result.setData(reward);
        } catch (MessageException e) {
            logger.error("每日签到异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("每日签到异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 用户反馈
     *
     * @param token      登录凭证
     * @param labelsId   key-value表中的对应id
     * @param message    回馈内容
     * @param voucherUrl 图片信息(多张使用逗号分隔)
     *                   <p>
     *                   {
     *                   "success": true,
     *                   "errorCode": "",
     *                   "msg": ""
     *                   }
     * @return
     */
    @RequestMapping("feedBack")
    @Consume(TReport.class)
    public Object feedBack(String token, long labelsId, String message, String voucherUrl) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        TReport report = (TReport) ConsumeHelper.getObj();
        try {
            userService.feedBack(user, report);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("用户反馈异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户反馈异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 任务信息查询
     *
     * @param token 登录凭证
     *              <p>
     *              {
     *              "success": true,
     *              "errorCode": "",
     *              "msg": "",
     *              "data": [    //任务类型列表
     *              3    //任务类型 0注册1实名2完善3签到4邀请好友5完成首次互助
     *              ]
     *              }
     * @return
     */
    @RequestMapping("taskList")
    public Object taskList(String token) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
//            Set<Integer> taskIds = userService.taskList(user);
            TaskHallView taskHallView = userService.taskHall(user);
            result.setData(taskHallView);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("任务信息查询异常: " + e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("任务信息查询异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 获取key—value值 样本
     *
     * @param key feedback
     * @return
     */
    @PostMapping("/get")
    public Object get(String key) {
        AjaxResult result = new AjaxResult();
        try {
            TPublish publish = userService.getPublishValue(key);
            result.setData(publish);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("获取key—value值失败," + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("获取key—value值失败" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 发送短信验证码
     *
     * @param telephone 手机号
     * @return
     */
    @PostMapping("generateSMS")
    public Object generateSMS(String telephone) {
        AjaxResult result = new AjaxResult();
        try {
            if (userService.genrateSMSCode(telephone).isSuccess()) { // 生成并发送成功
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setErrorCode(AppErrorConstant.ERROR_SENDING_SMS);
                result.setMsg(AppErrorConstant.SMS_NOT_SEND_MESSAGE);
            }
        } catch (MessageException e) {
            logger.error("发送短信验证码异常," + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("发送短信验证码异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 获取成长值明细
     *
     * @param token    登录凭证
     * @param ymString 年月字符串
     * @param option   操作
     * @return
     */
    @RequestMapping("scoreList")
    public Object scoreList(String token, String ymString, String option) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            Map<String, Object> queryResult = growthValueService.scoreList(user, ymString, option);
            result.setSuccess(true);
            result.setData(queryResult);
        } catch (MessageException e) {
            logger.error("发送短信验证码异常," + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("发送短信验证码异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 校验短信验证码
     *
     * @param telephone 手机号
     * @param validCode 短信验证码
     * @return
     */
    @PostMapping("checkSMS")
    public Object checkSMS(String telephone, String validCode) {
        AjaxResult result = new AjaxResult();
        try {
            userService.checkSMS(telephone, validCode);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("发送短信验证码异常," + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("发送短信验证码异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 回馈邀请人
     *
     * @param token     登录凭证
     * @param inviterId 邀请者编号
     * @return
     */
    @PostMapping("payInviter")
    public Object payInviter(String token, Long inviterId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        Long mineId = user.getId();
        try {
            userService.payInviter(inviterId, mineId);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("回馈邀请人异常," + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("回馈邀请人异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 分享（查看二维码)
     *
     * @param token  登录凭证
     * @param option 1个人分享、2服务分享、3求助分享 4加入组织
     * @return
     */
    @PostMapping("share")
    public Object share(String token, String serviceId, String option, String userId) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) redisUtil.get(token);
        try {
            ShareServiceView shareServiceView = userService.share(user, serviceId, option, token, userId);
            result.setSuccess(true);
            result.setData(shareServiceView);
        } catch (MessageException e) {
            logger.error("分享（查看二维码)异常," + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("分享（查看二维码)异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 微信授权基本信息更新
     *
     * @param token                登录凭证
     * @param userHeadPortraitPath 头像
     * @param name                 昵称
     * @param sex                  性别 1男2女
     * @return
     */
    @RequestMapping("wechat/infosAuth")
    @Consume(TUser.class)
    public Object wechatInfosAuth(String token, String userHeadPortraitPath, String name, Integer sex) {
        AjaxResult result = new AjaxResult();
        TUser user = (TUser) ConsumeHelper.getObj();
        try {
            userService.wechatInfoAuth(user, token);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("微信授权基本信息更新异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("微信授权基本信息更新异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 获取scene值
     *
     * @param scene 场景值
     * @return
     */
    @PostMapping("scene")
    public Object scene(Long scene) {
        AjaxResult result = new AjaxResult();
        try {
            SceneView sceneView = userService.scene(scene);
            result.setData(sceneView);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("获取scene值异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("获取scene值异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 激活（生成邀请码）
     *
     * @param token      登录凭证
     * @param inviteCode 邀请码(激活码)
     * @return
     */
    @PostMapping("invite/activate")
    public Object generateInviteCode(String token, String inviteCode) {
        AjaxResult result = new AjaxResult();
        try {
            userService.generateInviteCode(token, inviteCode);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("激活（生成邀请码）异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("激活（生成邀请码）异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 组织版登录（密码）
     *
     * @param telephone 手机号
     * @param password  密码
     * @return
     */
    @PostMapping("loginGroupByPwd")
    public Object loginGroupByPwd(String telephone, String password) {
        // TODO 在配置文件中加入拦截白名单
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> loginGroupByPwdMap = userService.loginGroupByPwd(telephone, password);
            result.setData(loginGroupByPwdMap);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组织版登录（密码）" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("组织版登录（密码）" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 重置密码（组织）
     *
     * @param telephone 手机号
     * @param validCode 验证码
     * @param password  密码
     * @return
     */
    @PostMapping("modifyPwd")
    public Object modifyPwd(String telephone, String validCode, String password) {
        AjaxResult result = new AjaxResult();
        try {
            userService.modifyPwd(telephone, validCode, password);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("重置密码异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("重置密码异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 申请加入组织
     *
     * @param token     登录凭证
     * @param companyId 组织编号
     * @return
     */
    @PostMapping("company/join")
    public Object joinCompany(String token, Long companyId) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            userService.joinCompany(user, companyId);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("重置密码异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("重置密码异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 组织时间轨迹查询
     *
     * @param token 登录凭证
     * @param year  年份
     * @param month 月份
     * @param type  类型 0全部1收入2支出
     * @return
     */
    @PostMapping("queryPayments")
    public Object queryPayments(String token, String year, String month, String type) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            CompanyPaymentView view = userService.queryPayment(user, year, month, type);
            result.setData(view);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.SysError.getErrorCode());
            result.setMsg(AppErrorConstant.AppError.SysError.getErrorMsg());
        }
        return result;
    }

}