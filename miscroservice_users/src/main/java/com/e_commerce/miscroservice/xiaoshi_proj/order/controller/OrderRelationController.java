package com.e_commerce.miscroservice.xiaoshi_proj.order.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.xiaoshi_proj.order.service.OrderRelationService;
import com.e_commerce.miscroservice.xiaoshi_proj.order.vo.EnrollUserInfoView;
import com.e_commerce.miscroservice.xiaoshi_proj.order.vo.OrgEnrollUserView;
import com.e_commerce.miscroservice.xiaoshi_proj.order.vo.UserInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单关系模块
 * <p>
 * 订单关系controller
 */
@RestController
@RequestMapping("/api/v2/orderRelation")
@Log
public class OrderRelationController extends BaseController {

    @Autowired
    private OrderRelationService orderRelationService;

    /**
     * 报名
     *
     * @param orderId   订单id
     * @param token     token
     * @param date      日期
     * @param serviceId 商品id
     *
     *                  <p>
     *                  "success": true,
     *                  "msg": "报名成功"
     * @return
     */
    @RequestMapping("/enroll/" + TokenUtil.AUTH_SUFFIX)
    public Object enroll(Long orderId, String token, String date, Long serviceId) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();

        try {
            orderRelationService.enroll(orderId, user.getId(), date, serviceId);
            result.setSuccess(true);
            result.setMsg("报名成功");
        } catch (MessageException e) {
            if (e.getErrorCode().equals("401") && serviceId != null) {
                orderRelationService.removeCanEnrollDate(date, serviceId);
            }
            log.warn("报名失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("报名失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("报名失败");
        }
        return result;
    }

    /**
     * 批量报名
     *
     * @param userIds   用户id，逗号分割
     * @param date      日期
     * @param serviceId 商品id
     * @param orderId   订单id
     *                  <p>
     *                  "success": true,
     *                  "msg": "批量报名成功"
     * @return
     */
    @RequestMapping("orgEnroll/" + TokenUtil.AUTH_SUFFIX)
    public Object orgEnrollAuth(Long orderId, String userIds, String date, Long serviceId) {
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            orderRelationService.orgEnroll(orderId, userIdList, date, serviceId);
            result.setSuccess(true);
            result.setMsg("批量报名成功");
        } catch (MessageException e) {
            if (e.getErrorCode().equals("401") && serviceId != null) {
                orderRelationService.removeCanEnrollDate(date, serviceId);
            }
            log.warn("批量报名失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("批量报名失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("批量报名失败");
        }
        return result;
    }


    /**
     * 批量报名
     *
     * @param userIds   用户id，逗号分割
     * @param date      日期
     * @param serviceId 商品id
     * @param orderId   订单id
     *                  <p>
     *                  "success": true,
     *                  "msg": "批量报名成功"
     * @return
     */
    @RequestMapping("/orgEnroll")
    public Object orgEnroll(Long orderId, String userIds, String date, Long serviceId) {
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            orderRelationService.orgEnroll(orderId, userIdList, date, serviceId);
            result.setSuccess(true);
            result.setMsg("批量报名成功");
        } catch (MessageException e) {
            if (e.getErrorCode().equals("401") && serviceId != null) {
                orderRelationService.removeCanEnrollDate(date, serviceId);
            }
            log.warn("批量报名失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("批量报名失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("批量报名失败");
        }
        return result;
    }

    /**
     * 根据评分返回评价标签
     *
     * @param type     类型 1-求助 2-服务
     * @param credit   可信评分
     * @param major    态度评分
     * @param attitude 专业评分
     *                 <p>
     *                 "success": true,
     *                 "errorCode": "",
     *                 "msg": "报名成功",
     *                 "data": "与描述不符,态度差,不守时,不专业,服务差" 评价标签逗号分割
     * @return
     */
    @RequestMapping("/getRemarkLabels/" + TokenUtil.AUTH_SUFFIX)
    public Object getRemarkLabels(int type, int credit, int major, int attitude) {
        AjaxResult result = new AjaxResult();
        try {
            String msg = orderRelationService.getRemarkLabels(type, credit, major, attitude);
            result.setSuccess(true);
            result.setData(msg);
            result.setMsg("查看成功");
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
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
     * 取消报名
     *
     * @param orderId 订单id
     * @param token   token
     *                <p>
     *                "success": true,
     *                "msg": "取消报名成功"
     * @return
     */
    @RequestMapping("/removeEnroll/" + TokenUtil.AUTH_SUFFIX)
    public Object removeEnroll(Long orderId, String token) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.removeEnroll(orderId, user.getId());
            result.setSuccess(true);
            result.setMsg("取消报名成功");
        } catch (MessageException e) {
            log.warn("取消报名失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("取消报名失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("取消报名失败");
        }
        return result;
    }

    /**
     * 操作用户列表
     *
     * @param orderId 订单id
     * @param type    类型 1-选人 2-开始 7-支付 9-评价
     * @param token   token
     *
     *                <p>
     *                {
     *                "success": true,
     *                "msg": "查看成功",
     *                "data": [
     *                {
     *                "name": "刘维", 名字
     *                "userHeadPortraitPath": "https://timebank。。。.png",头像
     *                "status": 1,
     *                "toStringId": "68813258559062016" 用户id
     *                }
     *                ]
     *                }
     * @return
     */
    @RequestMapping("/userList/" + TokenUtil.AUTH_SUFFIX)
    public Object userListAuth(Long orderId, int type, String token) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            List<UserInfoView> userInfoViewList = orderRelationService.userListByPperation(orderId, type, user.getId());
            result.setSuccess(true);
            result.setData(userInfoViewList);
            result.setMsg("查看成功");
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
     * 操作用户列表
     *
     * @param orderId 订单id
     * @param type    类型 1-选人 2-开始 7-支付 9-评价
     * @param token   token
     *
     *                <p>
     *                {
     *                "success": true,
     *                "msg": "查看成功",
     *                "data": [
     *                {
     *                "name": "刘维", 名字
     *                "userHeadPortraitPath": "https://timebank。。。.png",头像
     *                "status": 1,
     *                "toStringId": "68813258559062016" 用户id
     *                }
     *                ]
     *                }
     * @return
     */
    @RequestMapping("/userList")
    public Object userList(Long orderId, int type, String token) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        try {
            List<UserInfoView> userInfoViewList = orderRelationService.userListByPperation(orderId, type, user.getId());
            result.setSuccess(true);
            result.setData(userInfoViewList);
            result.setMsg("查看成功");
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
     * 选择用户
     *
     * @param orderId 订单id
     * @param token   token
     * @param userIds 被操作者id
     *                <p>
     *                {
     *                "success": true,
     *                "msg": "选人成功",
     *                "data": [
     *                "用户刘维已被您操作"
     *                ]
     *                }
     * @return
     */
    @RequestMapping("chooseUser/" + TokenUtil.AUTH_SUFFIX)
    public Object chooseUserAuth(Long orderId, String token, String userIds) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.chooseUser(orderId, user.getId(), userIdList);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("选人成功");
        } catch (MessageException e) {
            log.warn("选人失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("选人失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("选人失败");
        }
        return result;
    }

    /**
     * 选择用户
     *
     * @param orderId 订单id
     * @param token   token
     * @param userIds 被操作者id
     *                <p>
     *                {
     *                "success": true,
     *                "msg": "选人成功",
     *                "data": [
     *                "用户刘维已被您操作"
     *                ]
     *                }
     * @return
     */
    @RequestMapping("/chooseUser")
    public Object chooseUser(Long orderId, String token, String userIds) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.chooseUser(orderId, user.getId(), userIdList);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("选人成功");
        } catch (MessageException e) {
            log.warn("选人失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("选人失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("选人失败");
        }
        return result;
    }

    /**
     * 拒绝用户
     *
     * @param orderId 订单ID
     * @param token   token
     * @param userIds 用户id（多人逗号分割）
     *                <p>
     *                "success": true,
     *                "msg": "拒绝成功",
     *                "data": []
     * @return
     */
    @RequestMapping("unChooseUser/" + TokenUtil.AUTH_SUFFIX)
    public Object unChooseUserAuth(Long orderId, String token, String userIds) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.unChooseUser(orderId, userIdList, user.getId(), 0);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("拒绝成功");
        } catch (MessageException e) {
            log.warn("拒绝失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("拒绝失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("拒绝失败");
        }
        return result;
    }


    /**
     * 拒绝用户
     *
     * @param orderId 订单ID
     * @param token   token
     * @param userIds 用户id（多人逗号分割）
     *                <p>
     *                "success": true,
     *                "msg": "拒绝成功",
     *                "data": []
     * @return
     */
    @RequestMapping("unChooseUser")
    public Object unChooseUser(Long orderId, String token, String userIds) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.unChooseUser(orderId, userIdList, user.getId(), 0);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("拒绝成功");
        } catch (MessageException e) {
            log.warn("拒绝失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("拒绝失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("拒绝失败");
        }
        return result;
    }


    /**
     * 支付
     *
     * @param orderId  订单编号
     * @param token    token
     * @param userIds  被支付用户id（多人逗号分割）
     * @param payments 支付钱数（多人逗号分割）
     *                 <p>
     *                 "success": true,
     *                 "msg": "支付成功",
     *                 "data": [
     *                 "用户刘维已被您支付"
     *                 ]
     * @return
     */
    @PostMapping("pay/" + TokenUtil.AUTH_SUFFIX)
    public Object payAuth(Long orderId, String token, String userIds, String payments) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        String[] payment = payments.split(",");
        List<Long> paymentList = new ArrayList<>();

        for (int i = 0; i < payment.length; i++) {
            paymentList.add(Long.parseLong(payment[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.payOrder(orderId, userIdList, paymentList, user.getId(), 1);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("支付成功");
        } catch (MessageException e) {
            log.warn("支付失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("支付失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("支付失败");
        }
        return result;
    }

    /**
     * 支付
     *
     * @param orderId  订单编号
     * @param token    token
     * @param userIds  被支付用户id（多人逗号分割）
     * @param payments 支付钱数（多人逗号分割）
     *                 <p>
     *                 "success": true,
     *                 "msg": "支付成功",
     *                 "data": [
     *                 "用户刘维已被您支付"
     *                 ]
     * @return
     */
    @PostMapping("/pay")
    public Object pay(Long orderId, String token, String userIds, String payments) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        String[] payment = payments.split(",");
        List<Long> paymentList = new ArrayList<>();

        for (int i = 0; i < payment.length; i++) {
            paymentList.add(Long.parseLong(payment[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.payOrder(orderId, userIdList, paymentList, user.getId(), 1);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("支付成功");
        } catch (MessageException e) {
            log.warn("支付失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("支付失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("支付失败");
        }
        return result;
    }


    /**
     * 开始订单
     *
     * @param orderId 订单编号
     * @param token   token
     * @param userIds 被开始用户，逗号分割
     *                <p>
     *                "success": true,
     *                "msg": "开始成功",
     *                "data": [
     *                "用户刘维已被您操作"
     *                ]
     * @return
     */
    @PostMapping("startOrder/" + TokenUtil.AUTH_SUFFIX)
    public Object startOrderAuth(Long orderId, String token, String userIds) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            orderRelationService.startOrder(orderId, user.getId(), userIdList);
            result.setSuccess(true);
            result.setMsg("开始成功");
        } catch (MessageException e) {
            log.warn("开始失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("开始失败," + e.getMessage());
        } catch (Exception e) {
            log.error("开始失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("开始失败");
        }
        return result;
    }


    /**
     * 开始订单
     *
     * @param orderId 订单编号
     * @param token   token
     * @param userIds 被开始用户，逗号分割
     *                <p>
     *                "success": true,
     *                "msg": "开始成功",
     *                "data": [
     *                "用户刘维已被您操作"
     *                ]
     * @return
     */
    @PostMapping("startOrder")
    public Object startOrder(Long orderId, String token, String userIds) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            orderRelationService.startOrder(orderId, user.getId(), userIdList);
            result.setSuccess(true);
            result.setMsg("开始成功");
        } catch (MessageException e) {
            log.warn("开始失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("开始失败," + e.getMessage());
        } catch (Exception e) {
            log.error("开始失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("开始失败");
        }
        return result;
    }


    /**
     * 批量投诉
     *
     * @param orderId    订单id
     * @param labelsId   标签id
     * @param message    内容
     * @param voucherUrl 图片，多个逗号分割
     * @param token      token
     * @param userIds    用户id 多个逗号分割
     *                   <p>
     *                   "success": true,
     *                   "msg": "投诉成功",
     *                   "data": [
     *                   "用户刘维已被您投诉"
     *                   ]
     * @return
     */
    @PostMapping("reports/" + TokenUtil.AUTH_SUFFIX)
    public Object reportsAuth(long orderId, long labelsId, String message, String voucherUrl, String token, String userIds) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.repors(orderId, labelsId, message, voucherUrl, user.getId(), userIdList);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("投诉成功");
        } catch (MessageException e) {
            log.warn("投诉失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("投诉失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("投诉失败");
        }
        return result;
    }


    /**
     * 批量投诉
     *
     * @param orderId    订单id
     * @param labelsId   标签id
     * @param message    内容
     * @param voucherUrl 图片，多个逗号分割
     * @param token      token
     * @param userIds    用户id 多个逗号分割
     *                   <p>
     *                   "success": true,
     *                   "msg": "投诉成功",
     *                   "data": [
     *                   "用户刘维已被您投诉"
     *                   ]
     * @return
     */
    @PostMapping("/reports")
    public Object reports(long orderId, long labelsId, String message, String voucherUrl, String token, String userIds) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.repors(orderId, labelsId, message, voucherUrl, user.getId(), userIdList);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("投诉成功");
        } catch (MessageException e) {
            log.warn("投诉失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("投诉失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("投诉失败");
        }
        return result;
    }


    /**
     * 批量评价
     *
     * @param token    token
     * @param userIds  用户id 多个逗号分割
     * @param orderId  订单id
     * @param credit   信用评分
     * @param major    专业评分
     * @param attitude 题阿杜评分
     * @param message  评价内容
     * @param labels   评价标签
     *                 <p>
     *                 "success": true,
     *                 "msg": "评价成功",
     *                 "data": [
     *                 "用户刘维已被您评价"
     *                 ]
     * @return
     */
    @PostMapping("/remark/" + TokenUtil.AUTH_SUFFIX)
    public Object remark(String token, String userIds, Long orderId, int credit, int major, int attitude, String message, String labels) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.remarkOrder(user.getId(), userIdList, orderId, credit, major, attitude, message, labels);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("评价成功");
        } catch (MessageException e) {
            log.warn("评价失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("评价失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("评价失败");
        }
        return result;
    }


    /**
     * 查看取消订单的应扣除时间币
     *
     * @param token   token
     * @param orderId 订单ID
     *                <p>
     *                "success": true,
     *                "errorCode": "",
     *                "msg": "查看成功",
     *                "data": 0 状态判断
     * @return
     */
    @PostMapping("removeOrderTips/" + TokenUtil.AUTH_SUFFIX)
    public Object removeOrderTipsAuth(String token, Long orderId) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            long coin = orderRelationService.removeOrderTips(orderId, user.getId());
            result.setSuccess(true);
            result.setData(coin);
            result.setMsg("查看成功");
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }


    /**
     * 查看取消订单的应扣除时间币
     *
     * @param token   token
     * @param orderId 订单ID
     *                <p>
     *                "success": true,
     *                "errorCode": "",
     *                "msg": "查看成功",
     *                "data": 0 状态判断
     * @return
     */
    @PostMapping("removeOrderTips")
    public Object removeOrderTips(String token, Long orderId) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        try {
            long coin = orderRelationService.removeOrderTips(orderId, user.getId());
            result.setSuccess(true);
            result.setData(coin);
            result.setMsg("查看成功");
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }


    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param userIds 被操作用户ID（多个逗号拼接）
     * @param token   token
     *                <p>
     *                "success": true,
     *                "errorCode": "",
     *                "msg": "取消成功",
     *                "data": []
     * @return
     */
    @PostMapping("removeOrder/" + TokenUtil.AUTH_SUFFIX)
    public Object removeOrderAuth(Long orderId, String userIds, String token) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.removeOrder(orderId, userIdList, user.getId());
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("取消成功");
        } catch (MessageException e) {
            log.warn("取消失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("取消失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("取消失败");
        }
        return result;
    }


    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param userIds 被操作用户ID（多个逗号拼接）
     * @param token   token
     *                <p>
     *                "success": true,
     *                "errorCode": "",
     *                "msg": "取消成功",
     *                "data": []
     * @return
     */
    @PostMapping("/removeOrder")
    public Object removeOrder(Long orderId, String userIds, String token) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.removeOrder(orderId, userIdList, user.getId());
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("取消成功");
        } catch (MessageException e) {
            log.warn("取消失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("取消失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("取消失败");
        }
        return result;
    }

    /**
     * 接受时间赠礼
     *
     * @param userTimeRecordId 流水ID
     * @param eventId          事件ID
     *                         <p>
     *                         "success": true,
     *                         "errorCode": "",
     *                         "msg": "接受赠礼成功", 成功消息
     *                         "data": ""
     * @return
     */
    @PostMapping("acceptGiftForRemove/" + TokenUtil.AUTH_SUFFIX)
    public Object acceptGiftForRemoveAuth(Long userTimeRecordId, Long eventId) {
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.acceptGiftForRemove(userTimeRecordId, eventId);
            result.setSuccess(true);
            result.setMsg("接受赠礼成功");
        } catch (MessageException e) {
            log.warn("接受赠礼失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("接受赠礼失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("接受赠礼失败");
        }
        return result;
    }

    /**
     * 接受时间赠礼
     *
     * @param userTimeRecordId 流水ID
     * @param eventId          事件ID
     *                         <p>
     *                         "success": true,
     *                         "errorCode": "",
     *                         "msg": "接受赠礼成功", 成功消息
     *                         "data": ""
     * @return
     */
    @PostMapping("/acceptGiftForRemove")
    public Object acceptGiftForRemove(Long userTimeRecordId, Long eventId) {
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.acceptGiftForRemove(userTimeRecordId, eventId);
            result.setSuccess(true);
            result.setMsg("接受赠礼成功");
        } catch (MessageException e) {
            log.warn("接受赠礼失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("接受赠礼失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("接受赠礼失败");
        }
        return result;
    }


    /**
     * 拒绝时间赠礼
     *
     * @param userTimeRecordId 支付流水id
     * @param eventId          事件ID
     *                         <p>
     *                         "success": true,
     *                         "errorCode": "",
     *                         "msg": "拒绝赠礼成功",
     *                         "data": ""
     * @return
     */
    @PostMapping("unAcceptGiftForRemove/" + TokenUtil.AUTH_SUFFIX)
    public Object unAcceptGiftForRemoveAuth(Long userTimeRecordId, Long eventId) {
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.unAcceptGiftForRemove(userTimeRecordId, eventId);
            result.setSuccess(true);
            result.setMsg("拒绝赠礼成功");
        } catch (MessageException e) {
            log.warn("拒绝赠礼失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("拒绝赠礼失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("拒绝赠礼失败");
        }
        return result;
    }


    /**
     * 拒绝时间赠礼
     *
     * @param userTimeRecordId 支付流水id
     * @param eventId          事件ID
     *                         <p>
     *                         "success": true,
     *                         "errorCode": "",
     *                         "msg": "拒绝赠礼成功",
     *                         "data": ""
     * @return
     */
    @PostMapping("/unAcceptGiftForRemove")
    public Object unAcceptGiftForRemove(Long userTimeRecordId, Long eventId) {
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.unAcceptGiftForRemove(userTimeRecordId, eventId);
            result.setSuccess(true);
            result.setMsg("拒绝赠礼成功");
        } catch (MessageException e) {
            log.warn("拒绝赠礼失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("拒绝赠礼失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("拒绝赠礼失败");
        }
        return result;
    }

    /**
     * 举报订单详情
     *
     * @param associationId 订单ID
     * @param token         token
     * @param labelsId      标签ID
     * @param message       举报文本
     * @param voucherUrl    图片链接
     *                      <p>
     *                      "success": true,
     *                      "errorCode": "",
     *                      "msg": "举报成功",
     *                      "data": ""
     * @return
     */
    @PostMapping("/reportOrder/" + TokenUtil.AUTH_SUFFIX)
    public Object reportOrder(Long associationId, String token, long labelsId, String message, String voucherUrl) {
        TUser user = UserUtil.getUser();
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.reoprtOrder(associationId, user, labelsId, message, voucherUrl);
            result.setSuccess(true);
            result.setMsg("举报成功");
        } catch (MessageException e) {
            log.warn("举报失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("举报失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("举报失败");
        }
        return result;
    }


    /**
     * 组织版的订单头部信息
     *
     * @param orderId order编号
     * @param token   token
     *                <p>
     *                "success": true,
     *                "errorCode": "",
     *                "msg": "查看成功",
     *                "data": {
     *                "orderId": 209, order编号
     *                "title": "定时支付7测试",标题
     *                "status": 0, 状态，同小程序订单管理列表
     *                "startTime": "2019-03-29 11:02",开始时间
     *                "endTime": "2019-03-29 11:03",结束时间
     *                "isRepeat": true,是否为重复的互助
     *                "enrollSum": 1, 报名人数
     *                "chooseUserSum": 0,选择人数
     *                "canChooseSum": 0  可选择人数
     *                }
     * @return
     */
    @PostMapping("/orgOrderInfo")
    public Object orgOrderInfo(Long orderId, String token) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        try {
            OrgEnrollUserView orgEnrollUserView = orderRelationService.orgOrderInfo(orderId, user.getId());
            result.setSuccess(true);
            result.setData(orgEnrollUserView);
            result.setMsg("查看成功");
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }

    /**
     * 组织选人列表
     *
     * @param orderId  订单id
     * @param status   1-可选择 2-已选择 3-被拒绝 4-已取消 0-全部
     * @param type     1-查找姓名 2-查找电话 3-查找组内姓名 0-不查询，搜索栏前面的选项
     * @param value    对应值，搜索栏里面的内容
     * @param pageSize 分页大小
     * @param pageNum  分页页码
     * @param token    token
     *                 <p>
     *                 "success": true,
     *                 "errorCode": "",
     *                 "msg": "查看成功",
     *                 "data": {
     *                 "resultList": [
     *                 {
     *                 "userIdToString": 6, 用户id
     *                 "userNameForTeam": "", 组内姓名（如果不是组织成员，会没有）
     *                 "userName": "姜修弘", 昵称（小程序名称）
     *                 "userUrl": "https://timebank-tes...4582129.png",头像
     *                 "sex": 0,年龄
     *                 "age": "未知",性别
     *                 "occupation": "无业游民",职业
     *                 "isGroup": false,是否组内成员
     *                 "skill": "未设定",技能
     *                 "serve_num": 10,发布服务数量
     *                 "total_eva": 55,总评分
     *                 "creatTime": "2019-03-27 16:37", 报名时间
     *                 "status": 10 状态（同小程序）
     *                 }
     *                 ],
     *                 "totalCount": 1 total数量
     *                 }
     * @return
     */
    @PostMapping("/enrollUserInfoList")
    public Object enrollUserInfoList(Long orderId, int status, int type, String value,
                                     int pageSize, int pageNum, String token) {
        TUser user = UserUtil.getUser(token);
        AjaxResult result = new AjaxResult();
        try {
            QueryResult<EnrollUserInfoView> enrollUserInfoViewList = orderRelationService.enrollUserInfoList(orderId, status, type, value, pageSize, pageNum, user);
            result.setSuccess(true);
            result.setData(enrollUserInfoViewList);
            result.setMsg("查看成功");
        } catch (MessageException e) {
            log.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("查看失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }
}
