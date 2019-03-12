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
 * 订单关系
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
     *                  "name": "刘维",
     *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/1544189745461122.png",
     *                  "status": 1,
     *                  "toStringId": "68813258559062016"
     *                  },
     *                  {
     *                  "name": "左岸",
     *                  "userHeadPortraitPath": "https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI0PmHoNhhs1zvJdDYGT8ddTLkCXzLk6fO1mWiaYwFrBRaoh6wqicWVXoECbOC00khkXwzWhIjoGflg/132",
     *                  "status": 1,
     *                  "toStringId": "68813259653775360"
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
     * @param userIds 被操作用户ID
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
     * 测试
     *
     * @param orderId sa
     * @param userIds  ss
     *
     * "success": true,
     *                "msg": "报名成功"
     *
     */
    @RequestMapping("/test")
    public Object notices(Long orderId, String userIds) {
        AjaxResult result = new AjaxResult();
        String[] userId = userIds.split(",");
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < userId.length; i++) {
            userIdList.add(Long.parseLong(userId[i]));
        }
        try {
            String msg = orderRelationService.test(orderId, userIdList);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(msg);
            ;
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("失败");
            e.printStackTrace();
            return result;
        }
    }
}
