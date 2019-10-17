package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.po.*;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglRoleService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 楼盘管理 权限管理Controller
 *
 * @Author:
 * @Date: 2019-06-11 15:33
 */
@RequestMapping("lpgl/user")
@RestController
@Log
public class LpglUserController {


	@Autowired
	private LpglRoleService lpglRoleService;


	@Autowired
	private LpglUserService lpglUserService;



	/**
	 * 查找所有职位角色权限
	 * @return
	 */
//	@RequestMapping("findAllPosistionRoleAuthority")
//	public Object findAllPosistionRoleAuthority(){
//		AjaxResult ajaxResult = new AjaxResult();
//		JSONObject jsonObject = lpglRoleService.findAllPosistionRoleAuthority();
//		ajaxResult.setSuccess(true);
//		ajaxResult.setData(jsonObject);
//		return ajaxResult;
//	}

	/**
	 * 查找所有职位
	 * @return
	 */
	@RequestMapping("findAllPosistion")
	public Object findAllPosistion(){
		AjaxResult ajaxResult = new AjaxResult();
		List<TLpglPosistion> tLpglPosistions = lpglRoleService.findAllPosistion();
		ajaxResult.setSuccess(true);
		ajaxResult.setData(tLpglPosistions);
		return ajaxResult;
	}



	/**
	 * 查找所有角色
	 * @return
	 */
	@RequestMapping("findAllRole")
	public Object findAllRole(){
		AjaxResult ajaxResult = new AjaxResult();
		List<TLpglRole> tLpglRoles = lpglRoleService.findAllRole();
		ajaxResult.setSuccess(true);
		ajaxResult.setData(tLpglRoles);
		return ajaxResult;
	}

	/**
	 * 查找职位角色
	 * @return
	 */
	@RequestMapping("findAllPosistionRole")
	public Object findAllPosistionRole(Long posistionId){
		AjaxResult ajaxResult = new AjaxResult();
		List<TLpglRole> tLpglRoles = lpglRoleService.findAllPosistionRole(posistionId);
		ajaxResult.setSuccess(true);
		ajaxResult.setData(tLpglRoles);
		return ajaxResult;
	}
	/**
	 * 查找角色权限
	 * @return
	 */
	@RequestMapping("findAllRoleAuthority")
	public Object findAllRoleAuthority(Long roleId){
		AjaxResult ajaxResult = new AjaxResult();
		List<TLpglAuthority> tLpglAuthorities = lpglRoleService.findAllRoleAuthority(roleId);
		ajaxResult.setSuccess(true);
		ajaxResult.setData(tLpglAuthorities);
		return ajaxResult;
	}

	/**
	 * 查找所有权限
	 * @return
	 */
	@RequestMapping("findAllAuthority")
	public Object findAllAuthority(){
		AjaxResult ajaxResult = new AjaxResult();
		List<TLpglAuthority> tLpglAuthorities = lpglRoleService.findAllAuthority();
		ajaxResult.setSuccess(true);
		ajaxResult.setData(tLpglAuthorities);
		return ajaxResult;
	}


	/**
	 * 添加职位
	 * @param posistionName 职位名称
	 * @return
	 */
	@RequestMapping("editPosistion")
	public Object editPosistion(String posistionName){
		AjaxResult ajaxResult = new AjaxResult();
		TLpglPosistion tLpglPosistion = new TLpglPosistion();
		tLpglPosistion.setPosisitionName(posistionName);
		MybatisPlus.getInstance().save(tLpglPosistion);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 添加职位角色
	 * @param posistionId 职位名称
	 * @param roleId 角色id
	 * @return
	 */
	@RequestMapping("editPosistionRole")
	public Object editPosistionRole(Long posistionId,Long roleId){
		AjaxResult ajaxResult = new AjaxResult();
		TLpglPosistion posistion = MybatisPlus.getInstance().findOne(new TLpglPosistion(),new MybatisPlusBuild(TLpglPosistion.class)
			.eq(TLpglPosistion::getId,posistionId)
			.eq(TLpglPosistion::getDeletedFlag,0)
		);

		TLpglRole role = MybatisPlus.getInstance().findOne(new TLpglRole(),new MybatisPlusBuild(TLpglRole.class)
			.eq(TLpglRole::getId,roleId)
			.eq(TLpglRole::getDeletedFlag,0)
		);
		if (posistion==null){
			log.warn("该职位不存在");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("该职位不存在");
			return ajaxResult;
		}
		if (role==null){
			log.warn("该角色不存在");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("该角色不存在");
			return ajaxResult;
		}
		TLpglPosistionRole tLpglPosistionRole  = new TLpglPosistionRole();
		tLpglPosistionRole.setPosisitionId(posistionId);
		tLpglPosistionRole.setRoleId(roleId);
		int i  = MybatisPlus.getInstance().save(tLpglPosistionRole);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 添加角色
	 * @param roleName 角色名称
	 * @return
	 */
	@RequestMapping("editRole")
	public Object editRole(String roleName){
		AjaxResult ajaxResult = new AjaxResult();
		TLpglRole role = new TLpglRole();
		role.setRoleName(roleName);
		ajaxResult.setSuccess(true);
		MybatisPlus.getInstance().save(role);

		return ajaxResult;
	}



	/**
	 * 添加角色权限
	 * @param authorityId 权限id
	 * @param roleId 角色id
	 * @return
	 */
	@RequestMapping("editRoleAuthority")
	public Object editRoleAuthority(Long authorityId,Long roleId){
		AjaxResult ajaxResult = new AjaxResult();

		TLpglRole role = MybatisPlus.getInstance().findOne(new TLpglRole(),new MybatisPlusBuild(TLpglRole.class)
			.eq(TLpglRole::getId,roleId)
			.eq(TLpglRole::getDeletedFlag,0)
		);
		TLpglAuthority authority = MybatisPlus.getInstance().findOne(new TLpglAuthority(),new MybatisPlusBuild(TLpglAuthority.class)
			.eq(TLpglAuthority::getId,authorityId)
			.eq(TLpglAuthority::getDeletedFlag,0)
		);


		if (role==null){
			log.warn("该角色不存在");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("该角色不存在");
			return ajaxResult;
		}
		if (authority==null){
			log.warn("该权限不存在");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("该权限不存在");
			return ajaxResult;
		}
		TLpglRoleAuthority tLpglRoleAuthority  = new TLpglRoleAuthority();
		tLpglRoleAuthority.setAuthorityId(authorityId);
		tLpglRoleAuthority.setRoleId(roleId);
		int i  = MybatisPlus.getInstance().save(tLpglRoleAuthority);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
	/**
	 * 添加权限
	 * @param authorityName 权限名称
	 * @param parentId 父级Id
	 * @param code 权限Code 一级 1xxxx 二级 11xxx 三级111xx 按钮 1111x
	 * @param url 权限地址
	 * @param operationType 操作类型：1 增 2 删 3 改 4 查
	 * @return
	 */
	@RequestMapping("editAuthority")
	public Object editAuthority(String authorityName, Long parentId,String code,String url,Integer operationType){
		AjaxResult ajaxResult = new AjaxResult();
		TLpglAuthority authority = new TLpglAuthority();
		authority.setAuthorityName(authorityName);
		authority.setCode(code);

		if(parentId!=null){
			authority.setParentId(parentId);
		}

		if(StringUtils.isNotEmpty(url)){
			authority.setUrl(url);
		}
		if(operationType!=null){
			authority.setOperationType(operationType);
		}

		MybatisPlus.getInstance().save(authority);

		ajaxResult.setSuccess(true);
		return ajaxResult;
	}


	/**
	 * 注册
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("register")
	public Object register(String username,String password,Long posistionId){
		AjaxResult ajaxResult = new AjaxResult();


		TLpglUser req = new TLpglUser();
		TLpglUser user = MybatisPlus.getInstance().findOne(new TLpglUser(),new MybatisPlusBuild(TLpglUser.class).eq(TLpglUser::getUserAccount,username));
		if (user!=null){
			log.warn("用户已注册");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("用户已注册");
			return ajaxResult;
		}
		TLpglPosistion tLpglPosistion = lpglRoleService.findAllPosistionById(posistionId);
		if (tLpglPosistion==null){
			log.warn("职位不存在");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("用户已注册");
			return ajaxResult;
		}
		req.setUserAccount(username);
		req.setPassword(password);
		MybatisPlus.getInstance().save(req);
		TLpglUser result = MybatisPlus.getInstance().findOne(new TLpglUser(),new MybatisPlusBuild(TLpglUser.class).eq(TLpglUser::getUserAccount,username));

		int i =lpglRoleService.updatePosistionRole(posistionId,result.getId());

		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("login")
	public Object login(String username,String password){
		AjaxResult ajaxResult = new AjaxResult();

		TLpglUser user = MybatisPlus.getInstance().findOne(new TLpglUser(),new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getUserAccount,username)
			.eq(TLpglUser::getPassword,password)
		);
		if (user==null){
			log.warn("用户名或密码错误");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("用户名或密码错误");
			return ajaxResult;
		}
		JSONObject jsonObject = lpglRoleService.findRole(user.getId());

		ajaxResult.setData(jsonObject);

		ajaxResult.setSuccess(true);
		return ajaxResult;
	}




}
