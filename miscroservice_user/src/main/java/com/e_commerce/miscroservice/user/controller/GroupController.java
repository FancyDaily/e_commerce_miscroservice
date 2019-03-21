package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.NoAuthChangeException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.user.service.GroupService;
import com.e_commerce.miscroservice.user.vo.BaseGroupView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 组织模块
 * 功能描述:用户Controller
 */
@RestController
@RequestMapping("api/v2/group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private RedisUtil redisUtil;

	Log logger = Log.getInstance(GroupController.class);

	/**
	 *
	 * 功能描述:查询所有分组列表
	 * 作者:马晓晨
	 * 创建时间:2019年1月14日 下午2:09:18
	 * @return
	 */
	@PostMapping("/list")
	public Object listGroup(String token) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
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
	 *
	 * 功能描述：新建一个分组
	 * 作者:马晓晨
	 * 创建时间:2019年1月14日 下午3:34:15
	 * @return
	 */
	@PostMapping("/insert")
	public Object insertGroup(TGroup group, String token) {
		TUser user = (TUser) redisUtil.get(token);
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
	 *
	 * 功能描述:修改分组的名称
	 * 作者:马晓晨
	 * 创建时间:2019年1月14日 下午6:43:34
	 * @param group  获取组织ID和组织名称
	 * @param token
	 * @return
	 */
	@PostMapping("/modify")
	public Object updateGroup(TGroup group, String token) {
		TUser user = (TUser) redisUtil.get(token);
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
	 *
	 * 功能描述:删除分组
	 * 作者:马晓晨
	 * 创建时间:2019年1月15日 下午2:35:55
	 * @param groupId 分组ID
	 * @param token
	 * @return
	 */
	@PostMapping("/delete")
	public Object deleteGroup(Long groupId, String token) {
		TUser user = (TUser) redisUtil.get(token);
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
}
