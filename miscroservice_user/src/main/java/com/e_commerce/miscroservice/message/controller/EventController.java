package com.e_commerce.miscroservice.message.controller;


import com.e_commerce.miscroservice.commons.entity.application.TEvent;
import com.e_commerce.miscroservice.commons.entity.application.TPublish;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.message.service.EventService;
import com.e_commerce.miscroservice.message.service.PublishService;
import com.e_commerce.miscroservice.message.vo.BroadcastView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 键值模块
 *
 * 键值controller
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
     * @return
     */
    @PostMapping("/eventList")
    public Object eventList(Long nowUserId , Long tiggerId) {
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
