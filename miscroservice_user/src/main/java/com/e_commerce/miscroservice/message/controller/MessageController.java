package com.e_commerce.miscroservice.message.controller;


import com.e_commerce.miscroservice.commons.entity.application.TMessage;
import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.message.service.MessageService;
import com.e_commerce.miscroservice.message.vo.MessageDetailView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 消息
 *
 * 消息controller
 **/
@RestController
@RequestMapping("/api/v2/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    /**
     * 查看系统消息
     *
     * @param pageSize
     * @param lastTime
     * @param nowUserId
     *
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
     *
     * @return
     */
    @PostMapping("/notices")
    public Object notices(int pageSize , long lastTime , Long nowUserId) {
        AjaxResult result = new AjaxResult();
        try {
            QueryResult<TMessageNotice> messageNotices = messageService.notices(lastTime, nowUserId, pageSize);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(messageNotices);;
            return result;
        } catch (MessageException e) {
            logger.error("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("查看失败" + errInfo(e));
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
     * @param nowUserId
     * @param messageUserId
     * @param specialId
     * @param type
     * @param message
     * @param url
     *
     * @return
     */
    @PostMapping("/sendMessage")
    public Object sendMessage(Long nowUserId , Long messageUserId ,  Long specialId , int type , String message , String url) {
        AjaxResult result = new AjaxResult();
        if (url.isEmpty()) {
            if (message.trim().isEmpty()) {
                result.setSuccess(false);
                result.setMsg("您还没有输入内容");
                return result;
            }
        }
        try {
            messageService.send(nowUserId , messageUserId , specialId , type , message , url);
            result.setSuccess(true);
            result.setMsg("发送成功");
            return result;
        } catch (MessageException e) {
            logger.error("发送失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("发送失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("发送失败" + errInfo(e));
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
     * @param toUserId
     * @param lastTime
     * @param pageSize
     * @param nowUserId
     *
     *                 "id": 102098442918035456,  消息id
     *                 "userName": "马晓晨", //发送者名字
     *                 "userUrl": "https://timebank-p...9.png",//发送者头像
     *                 "userId": 68813260748488704, //用户编号
     *                 "time": 1552124565645, //发送时间
     *                 "status": 1, //发送者是不是别人 0-自己发送的 1-别人发送的
     *                 "message": "测试2", //文本内容
     *                 "type": 1, //消息是文本还是图片 0-图片 1-文本
     *                 "url": "" //图片链接
     *
     * @return
     */
    @PostMapping("/detail")
    public Object detail(Long toUserId , Long lastTime , int pageSize , Long nowUserId) {
        AjaxResult result = new AjaxResult();
        try {
            QueryResult<MessageDetailView> messageDetailViewQueryResult = messageService.detail(toUserId , lastTime , pageSize , nowUserId);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(messageDetailViewQueryResult);
        } catch (MessageException e) {
            logger.error("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("查看失败" + errInfo(e));
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
            e.printStackTrace();
        }
            return result;
    }

    /**
     * 收集formid
     * @param formId
     * @param userId
     * @return
     */
    @RequestMapping("/collectFormId")
    public Object collectFormId(String formId, Long userId) {
        AjaxResult result = new AjaxResult();
        try {
            messageService.insertFormId(formId, userId);
            result.setSuccess(true);
            result.setMsg("收集formId成功");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("收集formId失败");
            logger.error("收集formId错误：" + errInfo(e));
            e.printStackTrace();
            return result;
        }
    }
    @PostMapping("/showList")
    public Object showList(Long lastTime , Long nowUserId) {
        AjaxResult result = new AjaxResult();
        try {
            List<TMessage> messageList = messageService.list(nowUserId , lastTime);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(messageList);
        } catch (MessageException e) {
            logger.error("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("查看失败" + errInfo(e));
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
            e.printStackTrace();
        }
        return result;
    }

}
