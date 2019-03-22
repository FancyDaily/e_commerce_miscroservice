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
	 * 查询所有分组列表
	 *
	 * @param token
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
	 * 插入分组
	 *
	 * @param group
	 * @param token
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
	 * 修改分组名称
	 *
	 * @param group
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
	 * 删除分组
	 *
	 * @param groupId
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
