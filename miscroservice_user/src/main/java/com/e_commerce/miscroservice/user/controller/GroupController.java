package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.GroupService;
import com.e_commerce.miscroservice.user.vo.CompanyRecentView;
import com.e_commerce.miscroservice.user.vo.SmartUserView;
import com.e_commerce.miscroservice.user.vo.UserCompanyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 组织模块
 * 功能描述:用户Controller
 */
@RestController
@RequestMapping("api/v2/group")
public class GroupController extends BaseController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GroupService groupService;

    Log logger = Log.getInstance(UserController.class);

    /**
     * 组织概况
     *
     * @param token
     * @return
     */
    @PostMapping("gatherInfo")
    public Object gatherInfo(String token) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token); // 组织账号
            UserCompanyView gatherInfo = groupService.gatherInfo(user);
            result.setData(gatherInfo);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组织概况查询" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("组织概况查询" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }


    /**
     * 组织近况(年度服务、需求、新增人员数目统计)
     *
     * @param token
     * @return
     */
    @PostMapping("companyRecent")
    public Object companyRecent(String token) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            CompanyRecentView companyRecentView = groupService.companyRecent(user);
            result.setData(companyRecentView);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组织近况(年度服务、需求、新增人员数目统计)查询" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("组织近况(年度服务、需求、新增人员数目统计)查询" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 组内成员列表
     * @param token
     * @param groupId
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("user/list")
    public Object userList(String token, Long groupId, String param, Integer pageNum, Integer pageSize) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            QueryResult<SmartUserView> userList = groupService.userList(user, groupId, param, pageNum, pageSize);
            result.setData(userList);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组内成员列表异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("组内成员列表异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 成员新增(批量)
     * @param token
     * @param groupId
     * @param userIds
     * @return
     */
    @PostMapping("user/add")
    public Object userAdd(String token, Long groupId, String userIds) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            groupService.userAdd(user, groupId, userIds);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("成员新增(批量)异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("成员新增(批量)异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 创建新成员(如果为存在用户，直接加入组织，不存在则创建假用户再加入组织)
     * @param token
     * @param groupId
     * @param phone
     * @param userName
     * @param sex
     * @return
     */
    @PostMapping("user/insert")
    public Object userInsert(String token, Long groupId, String phone, String userName, Integer sex) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            groupService.userInsert(user, groupId, phone, userName, sex);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("创建新成员(如果为存在用户，直接加入组织，不存在则创建假用户再加入组织)" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("创建新成员(如果为存在用户，直接加入组织，不存在则创建假用户再加入组织)" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除组织成员
     * @param token
     * @param userIds
     * @return
     */
    @PostMapping("user/delete")
    public Object userDelete(String token, String userIds) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            groupService.delete(user, userIds);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("创建新成员(如果为存在用户，直接加入组织，不存在则创建假用户再加入组织)" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("创建新成员(如果为存在用户，直接加入组织，不存在则创建假用户再加入组织)" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 成员审核通过
     * @param token
     * @param userIds
     * @return
     */
    @PostMapping("user/pass")
    public Object userPass(String token, String userIds) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            groupService.userPass(user, userIds);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("成员审核通过异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("成员审核通过异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 成员审核拒绝
     * @param token
     * @param userIds
     * @return
     */
    @PostMapping("user/reject")
    public Object userReject(String token, String userIds) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            groupService.userReject(user, userIds);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("成员审核通过异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("成员审核通过异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 功能描述: 批量插入成员(Excel文件)
     * 作者: 许方毅
     * 创建时间: 2019年1月23日 下午6:07:49
     * @param token
     * @param file
     * @return
     */
    @PostMapping("/multiUserInsert")
    public Object multiUserInsert(@RequestParam("file") MultipartFile file, String token) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = (TUser) redisUtil.get(token);
            List<String> errorInfoList = groupService.multiUserInsert(user, file);
            result.setData(errorInfoList);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("成员审核通过异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (IOException e) {
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.IOError.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("成员审核通过异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }


}
