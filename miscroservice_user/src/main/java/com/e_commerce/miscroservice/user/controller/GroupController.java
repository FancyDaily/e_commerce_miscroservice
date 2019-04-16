package com.e_commerce.miscroservice.user.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.exception.colligate.NoAuthChangeException;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.product.controller.BaseController;
import com.e_commerce.miscroservice.user.service.GroupService;
import com.e_commerce.miscroservice.user.vo.BaseGroupView;
import com.e_commerce.miscroservice.user.vo.CompanyRecentView;
import com.e_commerce.miscroservice.user.vo.SmartUserView;
import com.e_commerce.miscroservice.user.vo.UserCompanyView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织模块
 */
@RestController
@RequestMapping("api/v2/group")
@Data
public class GroupController extends BaseController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GroupService groupService;

//    Log log = Log.getInstance(UserController.class);

	private String endpoint = "sts.aliyuncs.com";
	private String roleArn = "acs:ram::1833330782534832:role/aliyunosstokengeneratorrole";
	private String roleSessionName = "session-name";
	private String policy = "{\n" +
			"    \"Version\": \"1\", \n" +
			"    \"Statement\": [\n" +
			"        {\n" +
			"            \"Action\": [\n" +
			"                \"oss:*\"\n" +
			"            ], \n" +
			"            \"Resource\": [\n" +
			"                \"acs:oss:*:*:*\" \n" +
			"            ], \n" +
			"            \"Effect\": \"Allow\"\n" +
			"        }\n" +
			"    ]\n" +
			"}";

	/**
	 *
	 * 功能描述:获取操作oss时的临时token
	 * 作者:马晓晨
	 * 创建时间:2019年1月29日 下午10:53:05
	 * @return
	 */
	@ResponseBody
	@PostMapping("/getStsToken")
	public Object getStsToken(String accessKeyId, String accessKeySecret) {
		AjaxResult result = new AjaxResult();
		try {
			// 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
			DefaultProfile.addEndpoint("", "", "Sts", endpoint);
			// 构造default profile（参数留空，无需添加region ID）
			IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
			// 用profile构造client
			DefaultAcsClient client = new DefaultAcsClient(profile);
			final AssumeRoleRequest request = new AssumeRoleRequest();
			request.setMethod(MethodType.POST);
			request.setRoleArn(roleArn);
			request.setRoleSessionName(roleSessionName);
			request.setPolicy(policy); // Optional
			request.setDurationSeconds(900L); //持续时间
			final AssumeRoleResponse response = client.getAcsResponse(request);
			Map<String, String> tokenInfo = new HashMap<>();
			tokenInfo.put("securityToken", response.getCredentials().getSecurityToken());
			tokenInfo.put("accessKeyId", response.getCredentials().getAccessKeyId());
			tokenInfo.put("accessKeySecret", response.getCredentials().getAccessKeySecret());
			result.setSuccess(true);
			result.setData(tokenInfo);
/*            System.out.println("Expiration: " + response.getCredentials().getExpiration());
            System.out.println("Access Key Id: " + response.getCredentials().getAccessKeyId());
            System.out.println("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
            System.out.println("Security Token: " + response.getCredentials().getSecurityToken());
            System.out.println("RequestId: " + response.getRequestId());*/
		} catch (ClientException e) {
			result.setSuccess(false);
			result.setMsg("获取token失败");
			log.error(e.getErrMsg());
/*            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());*/
		}
		return result;
	}

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
            log.warn("组织概况查询" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("组织概况查询" , e);
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
            log.warn("组织近况(年度服务、需求、新增人员数目统计)查询" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("组织近况(年度服务、需求、新增人员数目统计)查询" , e);
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
            log.warn("组内成员列表异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("组内成员列表异常" , e);
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
            log.warn("成员新增(批量)异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("成员新增(批量)异常" , e);
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
            log.warn("创建新成员(如果为存在用户，直接加入组织，不存在则创建假用户再加入组织)" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("创建新成员(如果为存在用户，直接加入组织，不存在则创建假用户再加入组织)" , e);
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
            log.warn("删除组织成员(成员删除)" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除组织成员(成员删除)" , e);
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
            log.warn("成员审核通过异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("成员审核通过异常" , e);
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
            log.warn("成员审核拒绝异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("成员审核拒绝异常" , e);
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
            log.warn("批量插入成员(Excel文件)异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (IOException e) {
            log.error("批量插入成员(Excel文件)异常", e);
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.IOError.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("批量插入成员(Excel文件)异常", e);
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
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("查询分组列表失败");
			log.error("查询所有分组列表失败", e);
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
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("新建分组失败");
			log.error("新建分组失败", e);
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
			log.info(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			result.setSuccess(false);
			result.setMsg("修改分组名称失败");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("修改分组名称失败");
			log.error("修改分组名称失败", e);
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
			log.info(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			result.setSuccess(false);
			result.setMsg("删除分组失败");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("删除分组失败");
			log.error("删除分组失败", e);
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
            log.warn("新成员列表(待处理申请名单)异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("新成员列表(待处理申请名单)异常", e);
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
            log.warn("组织年度流水折线图数据异常" + e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("组织年度流水折线图数据异常", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 成员移动
     *
     * @param token token
     * @param userIds 要修改的用户id，逗号拼接
     * @param groupId 修改至分组的组编号
     *
     *      "success": true,
     *      "errorCode": "",
     *      "msg": "移动成功",
     *      "data": ""
     *
     * @return
     */
    @PostMapping("/userMove")
    public Object userMove(String token, String userIds, Long groupId) {
        TUser nowUser = (TUser) redisUtil.get(token);
        AjaxResult result = new AjaxResult();
        String[] split = userIds.split(",");
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(Long.valueOf(split[i]));
        }
        try {
            groupService.userMove(nowUser, groupId, list);
            result.setSuccess(true);
            result.setMsg("移动成功！");
            return result;
        } catch (MessageException e) {
            log.error("已知移动失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("移动失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("未知移动失败：", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("移动失败");
            return result;
        }
    }

    /**
     * 成员修改
     *
     * @param token token
     * @param userName 修改的名字
     * @param userId 修改用户的id
     * @param sex 修改的性别
     * @param groupId 修改的分组
     *
     *      "success": true,
     *      "errorCode": "",
     *      "msg": "修改成功",
     *      "data": ""
     *
     * @return
     */
    @PostMapping("/userModify")
    public Object userModify(String token, String userName, Long userId, Integer sex, Long groupId) {
        TUser nowUser = (TUser) redisUtil.get(token);
        AjaxResult result = new AjaxResult();
        try {
            groupService.userModify(nowUser, userName, userId, sex, groupId);
            result.setSuccess(true);
            result.setMsg("修改成功！");
            return result;
        } catch (MessageException e) {
            log.warn("已知修改失败," + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode("499");
            result.setMsg("修改失败," + e.getMessage());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("成员修改失败：",e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("修改失败");
            return result;
        }
    }

}
