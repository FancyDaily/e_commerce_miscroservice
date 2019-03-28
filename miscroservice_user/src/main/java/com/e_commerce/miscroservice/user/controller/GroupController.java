package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.GroupService;
import com.e_commerce.miscroservice.user.vo.CompanyRecentView;
import com.e_commerce.miscroservice.user.vo.SmartUserView;
import com.e_commerce.miscroservice.user.vo.UserCompanyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.exception.colligate.NoAuthChangeException;
import com.e_commerce.miscroservice.user.vo.BaseGroupView;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 组织模块
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
            TUser user = UserUtil.getUser(token); // 组织账号
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
            TUser user = UserUtil.getUser(token);
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
            TUser user = UserUtil.getUser(token);
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
            TUser user = UserUtil.getUser(token);
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
            TUser user = UserUtil.getUser(token);
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
     * 删除组织成员(成员删除)
     * @param token
     * @param userIds
     * @return
     */
    @PostMapping("user/delete")
    public Object userDelete(String token, String userIds) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = UserUtil.getUser(token);
            groupService.delete(user, userIds);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("删除组织成员(成员删除)" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除组织成员(成员删除)" + errInfo(e));
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
            TUser user = UserUtil.getUser(token);
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
            TUser user = UserUtil.getUser(token);
            groupService.userReject(user, userIds);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("成员审核拒绝异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("成员审核拒绝异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 批量插入成员(Excel文件)
     * @param file
     * @param token
     * @return
     */
    @PostMapping("/multiUserInsert")
    public Object multiUserInsert(@RequestParam("file") MultipartFile file, String token) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = UserUtil.getUser(token);
            List<String> errorInfoList = groupService.multiUserInsert(user, file);
            result.setData(errorInfoList);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("批量插入成员(Excel文件)异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (IOException e) {
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.IOError.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("批量插入成员(Excel文件)异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

	/**
	 * 查询所有分组列表
	 *
	 * @param token
	 * @return
	 */
	@PostMapping("/list")
	public Object listGroup(String token) {
		AjaxResult result = new AjaxResult();
		TUser user = UserUtil.getUser(token);
		try {
			List<BaseGroupView> listGroup = groupService.listGroup(user);
			result.setData(listGroup);
			result.setSuccess(true);
			result.setMsg("查询分组列表成功");
		} catch (IllegalArgumentException e) {
			result.setSuccess(false);
			result.setMsg("查询分组列表失败");
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("查询分组列表失败");
//			logger.error(errInfo(e), e);
		}
		return result;
	}

	/**
	 * 插入分组
	 *
	 * @param group
	 * @param token
	 * @return
	 */
	@PostMapping("/insert")
	public Object insertGroup(TGroup group, String token) {
		TUser user = UserUtil.getUser(token);
		AjaxResult result = new AjaxResult();
		try {
			groupService.insert(group, user);
			result.setSuccess(true);
			result.setMsg("新建分组成功");
		} catch (IllegalArgumentException e) {
			result.setSuccess(false);
			result.setMsg("新建分组失败");
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("新建分组失败");
//			logger.error(errInfo(e), e);
		}
		return result;
	}

	/**
	 * 修改分组名称
	 *
	 * @param group
	 * @param token
	 * @return
	 */
	@PostMapping("/modify")
	public Object updateGroup(TGroup group, String token) {
		TUser user = UserUtil.getUser(token);
		AjaxResult result = new AjaxResult();
		try {
			groupService.updateGroup(group, user);
			result.setSuccess(true);
			result.setMsg("修改分组名称成功");
		} catch (NoAuthChangeException e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.info(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			result.setSuccess(false);
			result.setMsg("修改分组名称失败");
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("修改分组名称失败");
//			logger.error(errInfo(e), e);
		}
		return result;
	}

	/**
	 * 删除分组
	 *
	 * @param groupId
	 * @param token
	 * @return
	 */
	@PostMapping("/delete")
	public Object deleteGroup(Long groupId, String token) {
		TUser user = UserUtil.getUser(token);
		AjaxResult result = new AjaxResult();
		try {
			groupService.deleteGroup(groupId, user);
			result.setSuccess(true);
			result.setMsg("删除分组成功");
		} catch (NoAuthChangeException e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.info(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			result.setSuccess(false);
			result.setMsg("删除分组失败");
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("删除分组失败");
//			logger.error(errInfo(e), e);
		}
		return result;
	}

    /**
     * 新成员列表(待处理申请名单)
     * @param token
     * @param pageNum
     * @param pageSize
     * @param param
     * @param skill
     * @return
     */
    @PostMapping("user/waitToJoin")
    public Object userWaitToJoin(String token, Integer pageNum, Integer pageSize, String param, String skill) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = UserUtil.getUser(token);
            QueryResult<SmartUserView> userViews = groupService.userWaitToJoin(user, pageNum, pageSize, param, skill);
            result.setData(userViews);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("新成员列表(待处理申请名单)异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("新成员列表(待处理申请名单)异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 组织年度流水折线图数据
     * @param token
     * @return
     */
    @PostMapping("companyPaymentDiagram")
    public Object companyPaymentDiagram(String token) {
        AjaxResult result = new AjaxResult();
        try {
            TUser user = UserUtil.getUser(token);
            CompanyRecentView view = groupService.companyPaymentDiagram(user);
            result.setData(view);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error("组织年度流水折线图数据异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("组织年度流水折线图数据异常" + errInfo(e));
            result.setSuccess(false);
        }
        return result;
    }

}
