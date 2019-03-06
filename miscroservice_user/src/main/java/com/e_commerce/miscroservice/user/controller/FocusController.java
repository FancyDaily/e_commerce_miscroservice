package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.FocusService;
import com.e_commerce.miscroservice.user.vo.DesensitizedUserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/focus")
public class FocusController extends BaseController {

    Log logger = Log.getInstance(FocusController.class);

    @Autowired
    private FocusService focusService;

    /**
     * 关注
     *
     * @param token
     * @param userFollowId 将要被关注的人id
     * @return
     */
    @PostMapping("submit")
    public Object submit(String token, Long userFollowId) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            focusService.submit(user, userFollowId);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("关注注异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 关注列表
     *
     * @param token
     * @param lastTime
     * @param pageSize
     * @return
     */
    @PostMapping("focusList")
    public Object focusList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            QueryResult<DesensitizedUserView> queryResult = focusService.myList(user,lastTime,pageSize,true);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("关注列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 粉丝列表
     * @param token
     * @param lastTime
     * @param pageSize
     * @return
     */
    @PostMapping("fanList")
    public Object fanList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            QueryResult<DesensitizedUserView> queryResult = focusService.myList(user,lastTime,pageSize,false);
            result.setData(queryResult);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("关注列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

}
