package com.e_commerce.miscroservice.xiaoshi_proj.message.controller;


import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.MessageEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.xiaoshi_proj.message.service.MessageService;
import com.e_commerce.miscroservice.xiaoshi_proj.message.vo.MessageDetailView;
import com.e_commerce.miscroservice.xiaoshi_proj.message.vo.MessageShowLIstView;
import com.e_commerce.miscroservice.xiaoshi_proj.message.vo.NoticesFirstView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 消息模块
 * <p>
 * 消息controller
 **/
@RestController
@RequestMapping("/api/v2/message")
@Log
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;


    /**
     * 收集formid
     *
     * @param formId formid
     * @param token  token
     *               <p>
     *               "success": true,
     *               "errorCode": "",
     *               "msg": "收集formId成功",
     *               "data": ""
     * @return
     */
    @RequestMapping("/collectFormId/" + TokenUtil.AUTH_SUFFIX)
    public Object collectFormId(String formId, String token) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser();
        try {
            messageService.insertFormId(formId, user.getId());
            result.setSuccess(true);
            result.setMsg("收集formId成功");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("收集formId失败");
            log.error("收集formId错误：", e);
            return result;
        }
    }

    /**
     * 查看系统消息
     *
     * @param pageSize 分页大小
     * @param lastTime 上一条的时间
     * @param token    token
     *                 <p>
     *                 <p>
     *                 "id": 81732352618790912, //系统消息编号
     *                 "noticeUserId": 68813259653775360, //接受者编号
     *                 "relevanceId": 81731245683245056, //关联id编号
     *                 "type": 1,//通知类型 1-普通文本通知
     *                 "noticeMessage": "在“借手机”的...",//通知内容
     *                 "createUser": 68813259435671552,//创建用户编号
     *                 "createUserName": "晴愔",
     *                 "createTime": 1547268911378,//创建时间
     *                 "updateUser": 68813259435671552,
     *                 "updateUserName": "晴愔",
     *                 "updateTime": 1547268911378,
     *                 "isValid": "1",
     *                 "noticeTitle": "恭喜您已被求助者选定"//通知标题
     * @return
     */
    @PostMapping("/notices/" + TokenUtil.AUTH_SUFFIX)
    public Object notices(int pageSize, long lastTime, String token) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            QueryResult<TMessageNotice> messageNotices = messageService.notices(lastTime, user.getId(), pageSize);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(messageNotices);
            ;
            return result;
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 发送消息
     *
     * @param token         token
     * @param messageUserId 发送给用户id
     * @param specialId     系统特殊操作id
     * @param type          类型 0-图片 1-文本
     * @param message       消息内容
     * @param url           图片链接
     *                      <p>
     *                      "success": true,
     *                      "errorCode": "",
     *                      "msg": "发送成功",
     *                      "data": ""
     * @return
     */
    @PostMapping("/sendMessage/" + TokenUtil.AUTH_SUFFIX)
    public Object sendMessage(String token, Long messageUserId, Long specialId, int type, String message, String url) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        if (type == MessageEnum.TYPE_TEXT.getType()) {
            //如果是文本，要看一下是否是空消息
            if (message.trim().isEmpty()) {
                result.setSuccess(false);
                result.setMsg("您还没有输入内容");
                return result;
            }
        }
        try {
            messageService.send(user.getId(), messageUserId, specialId, type, message, url);
            result.setSuccess(true);
            result.setMsg("发送成功");
            return result;
        } catch (MessageException e) {
            log.warn("发送失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("发送失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("发送失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("发送失败");
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 查看消息详情
     *
     * @param toUserId 对方用户编号
     * @param lastTime 上一条读取时间
     * @param pageSize 分页编号
     * @param token    token
     *                 {
     *                 "id": 102098442918035456,  消息id
     *                 "userName": "马晓晨", //发送者名字
     *                 "userUrl": "https://timebank-p...9.png",//发送者头像
     *                 "userId": 68813260748488704, //用户编号
     *                 "time": 1552124565645, //发送时间
     *                 "status": 1, //发送者是不是别人 0-自己发送的 1-别人发送的
     *                 "message": "测试2", //文本内容
     *                 "type": 1, //消息是文本还是图片 0-图片 1-文本
     *                 "url": "" //图片链接
     * @return
     */
    @PostMapping("/detail/" + TokenUtil.AUTH_SUFFIX)
    public Object detail(Long toUserId, Long lastTime, int pageSize, String token) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            QueryResult<MessageDetailView> messageDetailViewQueryResult = messageService.detail(toUserId, lastTime, pageSize, user.getId());
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(messageDetailViewQueryResult);
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }


    /**
     * 消息展示list
     *
     * @param lastTime 上次读取时间
     * @param token    token
     * @param pageSize 分页大小
     *                 <p>
     *                 "parent": 72400059177631744, 分组id
     *                 "userName": "马晓晨", 发送者姓名
     *                 "userUrl": "https://time。。。.png",发送者头像
     *                 "content": "测试2",发送内容
     *                 "time": 1552284647626,发送时间
     *                 "parentToString": "72400059177631744",string型的分组ID
     *                 "toUserIdToString": "68813260748488704",string型的对方id
     *                 "unReadSum": 18 未读消息数量
     * @return
     */
    @PostMapping("/showList/" + TokenUtil.AUTH_SUFFIX)
    public Object showList(Long lastTime, String token, Integer pageSize) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            QueryResult<MessageShowLIstView> list = messageService.list(user.getId(), lastTime, pageSize);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(list);
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }

    /**
     * 查看第一条的系统消息时间和未读消息数量
     *
     * @param token token
     *              <p>
     *              "sysTime": 1547437340571,//最新一条的时间
     *              "sysUnReadSum": 8 未读消息数量
     * @return
     */
    @PostMapping("/noticesFirstInfo/" + TokenUtil.AUTH_SUFFIX)
    public Object noticesFirstInfo(String token) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            NoticesFirstView noticesFirstView = messageService.noticesFirstInfo(user.getId());
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(noticesFirstView);
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }

    /**
     * 是否有未读消息
     *
     * @param token token
     *              <p>
     *              "success": true,
     *              "errorCode": "",
     *              "msg": "查看成功",
     *              "data": 1 1-有未读消息 0-没有未读消息
     * @return
     */
    @PostMapping({"/unReadMsg/" + TokenUtil.AUTH_SUFFIX,"unReadMsg"})
    public Object unReadMsg(String token) {
        AjaxResult result = new AjaxResult();
        /*if (token == null) {
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(0);
            return result;
        }*/
        TUser user = UserUtil.getUser();
        if (user == null) {
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(0);
            return result;
        }
        try {
            int unReadMsg = messageService.unReadMsg(user.getId());
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(unReadMsg);
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }
}
