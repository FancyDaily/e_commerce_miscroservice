package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserSkill;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.vo.UserFreezeView;
import com.e_commerce.miscroservice.user.vo.UserSkillListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v2/user")
public class UserController extends BaseController {

    Log logger = Log.getInstance(UserController.class);

    @Autowired
    private UserService userService;


    /**
     * 时间轨迹
     *
     * @param token
     * @param ymString
     * @param option
     * @return
     */
    @PostMapping("payments")
    public Object payments(String token, String ymString, String option) {
        AjaxResult result = new AjaxResult();
        //TODO redis
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            Map<String, Object> payments = userService.payments(user, ymString, option);
            result.setData(payments);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.info("时间轨迹异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 冻结明细
     *
     * @param token
     * @param lastTime
     * @param pageSize
     * @return
     */
    @PostMapping("freezeList")
    public Object freezeList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            QueryResult<UserFreezeView> queryResult = userService.frozenList(user.getId(), lastTime, pageSize);
            result.setSuccess(true);
            result.setData(queryResult);
        } catch (Exception e) {
            logger.info("冻结明细异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }


    /**
     * 公益历程列表
     *
     * @param token
     * @param lastTime
     * @param pageSize
     * @param year
     * @return
     */
    @PostMapping("publicWelfareList")
    public Object publicWelfareList(String token, Long lastTime, Integer pageSize, Integer year) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            Map<String, Object> map = userService.publicWelfareList(user, lastTime, pageSize, year);
            result.setData(map);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("公益历程列表异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查看技能（包含详细信息）
     *
     * @param token
     * @return
     */
    @PostMapping("skill/list")
    public Object skillList(String token) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            UserSkillListView skillView = userService.skills(user);
            result.setData(skillView);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 增加技能
     *
     * @param token
     * @param name
     * @param description
     * @param headUrl
     * @param detailUrls
     * @return
     */
    @PostMapping("skill/add")
    @Consume(TUserSkill.class)
    public Object skillAdd(String token, String name, String description, String headUrl, String detailUrls) {
        AjaxResult result = new AjaxResult();
        TUserSkill skill = (TUserSkill) ConsumeHelper.getObj();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            userService.skillAdd(user, skill);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 修改技能
     *
     * @param token
     * @param name
     * @param description
     * @param headUrl
     * @param detailUrl
     * @return
     */
    @PostMapping("skill/modify")
    @Consume(TUserSkill.class)
    public Object skillModify(String token, Long id, String name, String description, String headUrl, String detailUrl) {
        AjaxResult result = new AjaxResult();
        TUserSkill skill = (TUserSkill) ConsumeHelper.getObj();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            userService.skillModify(user, skill);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 收藏列表
     *
     * @param token
     * @param lastTime
     * @param pageSize
     * @return
     */
    @PostMapping("collect/list")
    public Object collectList(String token, Long lastTime, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        TUser user = new TUser();
        user.setId(68813259007852544l);
        try {
            //TODO 调订单模块的收藏列表方法
//            QueryResult<Map<String, Object>> queryResult = userService.collectList(user,lastTime,pageSize);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查看技能异常", errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 收藏/取消收藏
     * @param token
     * @param orderId
     * @return
     */
    @PostMapping("collect")
    public Object collect(String token, Long orderId) {

        return null;
    }

    /**
     * 查看用户基本信息
     * @param token
     * @param userId
     * @return
     */
    @PostMapping("info")
    public Object info(String token, Long userId) {

        return null;
    }


    public TUser getUserById(Long userId) {
        return userService.getUserbyId(userId);
    }

}
