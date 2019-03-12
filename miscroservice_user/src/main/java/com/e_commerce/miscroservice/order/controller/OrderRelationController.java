package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import com.e_commerce.miscroservice.order.vo.UserInfoView;
import org.redisson.api.annotation.REntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单关系模块
 *
 * 订单关系controller
 */
@RestController
@RequestMapping("/api/v2/orderRelation")
public class OrderRelationController extends BaseController {

    @Autowired
    private OrderRelationService orderRelationService;

    @Autowired
    private OrderRelationshipDao orderRelationshipDao;

    /**
     * 报名
     *
     * @param orderId   订单id
     * @param userId    用户id
     * @param date      日期
     * @param serviceId 商品id
     *
     *                  <p>
     *                  "success": true,
     *                  "msg": "报名成功"
     *
     * @return
     */
    @RequestMapping("/enroll")
    public Object enroll(Long orderId, long userId, String date, Long serviceId) {
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.enroll(orderId, userId, date, serviceId);
            result.setSuccess(true);
            result.setMsg("报名成功");
        } catch (MessageException e) {
            logger.warn("报名失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("报名失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("报名失败" + errInfo(e), e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("报名失败");
        }
        return result;
    }

    /**
     * 取消报名
     *
     * @param orderId   订单id
     * @param nowUserId 当前用户id
     *
     *                  "success": true,
     *                  "msg": "取消报名成功"
     *
     * @return
     */
    @RequestMapping("/removeEnroll")
    public Object removeEnroll(Long orderId, Long nowUserId) {
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.removeEnroll(orderId, nowUserId);
            result.setSuccess(true);
            result.setMsg("取消报名成功");
        } catch (MessageException e) {
            logger.warn("取消报名失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("取消报名失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("取消报名失败" + errInfo(e), e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("取消报名失败");
        }
        return result;
    }

    /**
     * 操作用户列表
     *
     * @param orderId   订单id
     * @param type      类型 1-选人 2-开始 7-支付 9-评价
     * @param nowUserId 操作用户id
     *
     *                  <p>
     *                  {
     *                  "success": true,
     *                  "msg": "查看成功",
     *                  "data": [
     *                  {
     *                  "name": "刘维", 名字
     *                  "userHeadPortraitPath": "https://timebank。。。.png",头像
     *                  "status": 1,
     *                  "toStringId": "68813258559062016" 用户id
     *                  }
     *                  ]
     *                  }
     *
     * @return
     */
    @RequestMapping("/userList")
    public Object userList(Long orderId, int type, Long nowUserId) {
        AjaxResult result = new AjaxResult();
        try {
            List<UserInfoView> userInfoViewList = orderRelationService.userListByPperation(orderId, type, nowUserId);
            result.setSuccess(true);
            result.setData(userInfoViewList);
            result.setMsg("查看成功");
        } catch (MessageException e) {
            logger.warn("查看失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查看失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("查看失败" + errInfo(e), e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
        }
        return result;
    }

    /**
     * 选择用户
     * @param orderId 订单id
     * @param nowUserId 当前用户id
     * @param userIds 被操作者id
     *
     * {
     *     "success": true,
     *     "msg": "选人成功",
     *     "data": [
     *         "用户刘维已被您操作"
     *     ]
     * }
     *
     * @return
     */
    @RequestMapping("/chooseUser")
    public Object chooseUser(Long orderId, Long nowUserId, String userIds) {
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.chooseUser(orderId , nowUserId , userIdList);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("选人成功");
        } catch (MessageException e) {
            logger.warn("选人失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("选人失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("选人失败" + errInfo(e), e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("选人失败");
        }
        return result;
    }

    /**
     * 拒绝用户
     * @param orderId 订单ID
     * @param nowUserId 当前用户ID
     * @param userIds 用户id（多人逗号分割）
     *
     *     "success": true,
     *     "msg": "拒绝成功",
     *     "data": []
     *
     * @return
     */
    @RequestMapping("/unChooseUser")
    public Object unChooseUser(Long orderId, Long nowUserId, String userIds) {
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            List<String> errorMsgList = orderRelationService.unChooseUser(orderId ,userIdList ,nowUserId);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("拒绝成功");
        } catch (MessageException e) {
            logger.warn("拒绝失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("拒绝失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("拒绝失败" + errInfo(e), e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("拒绝失败");
        }
        return result;
    }

    /**
     * 支付
     *
     * @param orderId 订单编号
     * @param nowUserId 当前用户id
     * @param userIds 被支付用户id（多人逗号分割）
     * @param payments 支付钱数（多人逗号分割）
     * @return
     */
    @PostMapping("/pay")
    public Object pay(Long orderId, Long nowUserId, String userIds , String payments) {
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
            List<String> errorMsgList = orderRelationService.payOrder(orderId , userIdList , paymentList , nowUserId);
            result.setSuccess(true);
            result.setData(errorMsgList);
            result.setMsg("支付成功");
        } catch (MessageException e) {
            logger.warn("支付失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("支付失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("支付失败" + errInfo(e), e);
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
     * @param nowUserId 当前用户
     *
     *     "success": false,
     *     "errorCode": "499",
     *     "msg": "开始失败,您已经签到过了～", 错误消息
     *
     * @return
     */
    @PostMapping("/startOrder")
    public Object startOrder(Long orderId, Long nowUserId) {
        AjaxResult result = new AjaxResult();
        try {
            orderRelationService.startOrder(orderId , nowUserId);
            result.setSuccess(true);
            result.setMsg("开始成功");
        } catch (MessageException e) {
            logger.warn("开始失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("开始失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("开始失败" + errInfo(e), e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("开始失败");
        }
        return result;
    }

}
