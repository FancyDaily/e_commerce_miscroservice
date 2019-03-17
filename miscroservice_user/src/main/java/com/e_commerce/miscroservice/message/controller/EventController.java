package com.e_commerce.miscroservice.message.controller;


import com.e_commerce.miscroservice.commons.entity.application.TEvent;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.message.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 事件模块
 *
 * 事件controller
 **/
@RestController
@RequestMapping("/api/v2/event")
public class EventController extends BaseController {


    @Autowired
    private EventService eventService;


    /**
     * 根据触发id和用户查找触发事件
     *
     * @param nowUserId 当前用户id
     * @param tiggerId 触发id
     *
     *
     *             "id": 2, 编号
     *             "userId": 68813259653775360, 用户编号
     *             "templateId": 1, 模版id 1-赔付时间模版
     *             "tiggerId": "orderId103548905098051584", 触发id
     *             "parameter": "userTimeRecordId=104158218095165440", 参数
     *             "priority": 2, 优先级
     *             "text": "用户马晓晨已取消订单，并向你支付致歉礼：互助时10分钟", 模版填充文本
     *             "createTime": 1552615648891, 创建时间
     *             "createUser": 68813260748488704,
     *             "createUserName": "马晓晨",
     *             "isValid": "1"
     *
     * @return
     */
    @PostMapping("/eventList")
    public Object eventList(Long nowUserId , String tiggerId) {
        AjaxResult result = new AjaxResult();
        try {
            List<TEvent> eventList = eventService.selectTeventListByUserIdAndTiggetId(nowUserId , tiggerId);
            result.setSuccess(true);
            result.setMsg("查询成功！");
            result.setData(eventList);
            return result;
        } catch (MessageException e) {
            logger.warn("查询失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查询失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("查询失败" + errInfo(e));
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查询失败");
            e.printStackTrace();
            return result;
        }
    }
}
