package com.e_commerce.miscroservice.message.controller;


import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.entity.application.TPublish;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.MessageEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.message.service.MessageService;
import com.e_commerce.miscroservice.message.service.PublishService;
import com.e_commerce.miscroservice.message.vo.BroadcastView;
import com.e_commerce.miscroservice.message.vo.MessageDetailView;
import com.e_commerce.miscroservice.message.vo.MessageShowLIstView;
import com.e_commerce.miscroservice.message.vo.NoticesFirstView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.Log;
import java.util.List;


/**
 * 键值模块
 *
 * 键值controller
 **/
@RestController
@RequestMapping("/api/v2/publish")
@Log
public class PublishController extends BaseController {

    @Autowired
    private PublishService publishService;

    /**
     *
     * 存入key—value值
     *
     * @param id 编号
     * @param key key
     * @param value 值
     * @param extend
     *
     *     "success": true,
     *     "errorCode": "",
     *     "msg": "插入成功！"
     *
     * @return
     */
    @PostMapping("/set")
    public Object repors(Long id , String key , String value , String extend) {
        AjaxResult result = new AjaxResult();
        try {
            publishService.pulishIn(id, key, value, extend);
            result.setSuccess(true);
            result.setMsg("插入成功！");
            return result;
        } catch (MessageException e) {
            log.warn("插入失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("插入失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("插入失败", e);
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("投诉失败");
            e.printStackTrace();
            return result;
        }
    }
    /**
     *
     * 获取key—value值
     *
     * @param key key值
     *
     *         "id": 63645917454908320, //自增编号
     *         "mainKey": "feedback", //key
     *         "extend": "",
     *         "createUser": 9527,
     *         "createUserName": "姜帅哥",
     *         "createTime": 1542956769205,
     *         "updateUser": 9527,
     *         "updateUserName": "姜帅哥",
     *         "updateTime": 1542956769205,
     *         "isValid": "1",
     *         "value": "[{\"id\":\"2100001\",\"name\":\"bug\"},{\"id\":\"2100002\",\"name\":\"建议\"},{\"id\":\"2100003\",\"name\":\"五星好评\"}]"//对其进行json解析
     *
     * @return
     */
    @PostMapping("/publishGet")
    public Object get(String key) {
        AjaxResult result = new AjaxResult();
        try {
            TPublish publish = publishService.publishGet(key);
            result.setSuccess(true);
            result.setMsg("查询成功！");
            result.setData(publish);
            return result;
        } catch (MessageException e) {
            log.warn("查询失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查询失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("查询失败" , e);
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查询失败");
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 获取broadcast
     *
     * @param length
     * @param width
     * @return
     */
    @PostMapping("/publishGetBroadcast")
    public Object publishGetBroadcast(String length , String width) {
        AjaxResult result = new AjaxResult();
        try {
            List<BroadcastView> broadcastViewList = publishService.getBroadcast(length , width);
            result.setSuccess(true);
            result.setMsg("查询成功！");
            result.setData(broadcastViewList);
            return result;
        } catch (MessageException e) {
            log.warn("查询失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查询失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("查询失败" , e);
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("查询失败");
            e.printStackTrace();
            return result;
        }
    }
}
