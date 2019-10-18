package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.po.*;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglRoleService;
import com.e_commerce.miscroservice.lpglxt_proj.vo.TLpglAuthorityVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
@Service
public class LpglRoleServiceImpl implements LpglRoleService {


	@Override
	public JSONObject findRole(Long userId) {
		TLpglUserPosistion tLpglUserPosistion = MybatisPlus.getInstance().findOne(
			new TLpglUserPosistion(), new MybatisPlusBuild(TLpglUserPosistion.class)
				.eq(TLpglUserPosistion::getUserId, userId)
				.eq(TLpglUserPosistion::getDeletedFlag, 0)
		);

		List<TLpglAuthorityVo> firstPageList = new ArrayList();
		List<TLpglAuthorityVo> secondPageList = new ArrayList();
		List<TLpglAuthorityVo> thirdPageList = new ArrayList();
		List<TLpglAuthorityVo> buttonList = new ArrayList();
//		List<TLpglAuthorityVo> totalList = new ArrayList();
		//获取职位 角色关联
		if (tLpglUserPosistion != null && tLpglUserPosistion.getPosistionId() != null) {
			List<TLpglPosistionRole> tLpglPosistionRole = MybatisPlus.getInstance().findAll(new TLpglPosistionRole(), new MybatisPlusBuild(TLpglPosistionRole.class)
				.eq(TLpglPosistionRole::getPosisitionId, tLpglUserPosistion.getPosistionId())
				.eq(TLpglPosistionRole::getDeletedFlag, 0)
			);
			if (tLpglPosistionRole != null && tLpglPosistionRole.size() > 0) {
				for (TLpglPosistionRole lpglPosistionRole : tLpglPosistionRole) {
					//角色关联权限
					List<TLpglRoleAuthority> tLpglRoleAuthorityList = MybatisPlus.getInstance().findAll(new TLpglRoleAuthority(), new MybatisPlusBuild(TLpglRoleAuthority.class)
						.eq(TLpglRoleAuthority::getRoleId, lpglPosistionRole.getRoleId())
						.eq(TLpglRoleAuthority::getDeletedFlag, 0)
					);
					if (tLpglRoleAuthorityList != null && tLpglRoleAuthorityList.size() > 0) {
						for (TLpglRoleAuthority tLpglRoleAuthority : tLpglRoleAuthorityList) {
							//权限表
							List<TLpglAuthority> tLpglAuthoriyList = MybatisPlus.getInstance().findAll(new TLpglAuthority(), new MybatisPlusBuild(TLpglAuthority.class)
								.eq(TLpglAuthority::getId, tLpglRoleAuthority.getAuthorityId())
								.eq(TLpglAuthority::getDeletedFlag, 0)
							);
							//权限分配
							if (tLpglAuthoriyList != null && tLpglAuthoriyList.size() > 0) {
								for (TLpglAuthority tLpglAuthority : tLpglAuthoriyList) {
									TLpglAuthorityVo tLpglAuthorityVo = new TLpglAuthorityVo();
									BeanUtils.copyProperties(tLpglAuthority, tLpglAuthorityVo);
									String code = tLpglAuthority.getCode();
									//一级页面
									if (code.matches("\\d{1,2}0000")) {
										firstPageList.add(tLpglAuthorityVo);
									}
									//二级页面
									if (code.matches("\\d{1,2}[1-9]000")) {
										tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 4) + "0000");
										secondPageList.add(tLpglAuthorityVo);
									}
									//三级级页面
									if (code.matches("\\d{1,2}[1-9][1-9]00")) {
										tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 3) + "000");
										thirdPageList.add(tLpglAuthorityVo);

									}
									//页面按钮
									if (code.matches("\\d{1,2}[1-9][1-9][1-9]0")) {
										tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 2) + "00");
										buttonList.add(tLpglAuthorityVo);
									}
								}
							}

						}
					}


				}
			}
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("firstPageList", firstPageList);
		jsonObject.put("secondPageList", secondPageList);
		jsonObject.put("thirdPageList", thirdPageList);
		jsonObject.put("buttonList", buttonList);
//		jsonObject.put("totalList",totalList);

		return jsonObject;
	}

	@Override
	public JSONObject findRole(Long userId, Integer status) {
		{
			TLpglUserPosistion tLpglUserPosistion = MybatisPlus.getInstance().findOne(
				new TLpglUserPosistion(), new MybatisPlusBuild(TLpglUserPosistion.class)
					.eq(TLpglUserPosistion::getUserId, userId)
					.eq(TLpglUserPosistion::getDeletedFlag, 0)
			);

			List<TLpglAuthorityVo> firstPageList = new ArrayList();
			List<TLpglAuthorityVo> secondPageList = new ArrayList();
			List<TLpglAuthorityVo> thirdPageList = new ArrayList();
			List<TLpglAuthorityVo> buttonList = new ArrayList();
//		List<TLpglAuthorityVo> totalList = new ArrayList();
			//获取职位 角色关联
			if (tLpglUserPosistion != null && tLpglUserPosistion.getPosistionId() != null) {
				List<TLpglPosistionRole> tLpglPosistionRole = MybatisPlus.getInstance().findAll(new TLpglPosistionRole(), new MybatisPlusBuild(TLpglPosistionRole.class)
					.eq(TLpglPosistionRole::getPosisitionId, tLpglUserPosistion.getPosistionId())
					.eq(TLpglPosistionRole::getDeletedFlag, 0)
				);
				if (tLpglPosistionRole != null && tLpglPosistionRole.size() > 0) {
					for (TLpglPosistionRole lpglPosistionRole : tLpglPosistionRole) {
						//角色关联权限
						List<TLpglRoleAuthority> tLpglRoleAuthorityList = MybatisPlus.getInstance().findAll(new TLpglRoleAuthority(), new MybatisPlusBuild(TLpglRoleAuthority.class)
							.eq(TLpglRoleAuthority::getRoleId, lpglPosistionRole.getRoleId())
							.eq(TLpglRoleAuthority::getDeletedFlag, 0)
						);
						if (tLpglRoleAuthorityList != null && tLpglRoleAuthorityList.size() > 0) {
							for (TLpglRoleAuthority tLpglRoleAuthority : tLpglRoleAuthorityList) {
								//权限表
								List<TLpglAuthority> tLpglAuthoriyList = MybatisPlus.getInstance().findAll(new TLpglAuthority(), new MybatisPlusBuild(TLpglAuthority.class)
									.eq(TLpglAuthority::getId, tLpglRoleAuthority.getAuthorityId())
									.eq(TLpglAuthority::getDeletedFlag, 0)
								);
								//权限分配
								if (tLpglAuthoriyList != null && tLpglAuthoriyList.size() > 0) {
									for (TLpglAuthority tLpglAuthority : tLpglAuthoriyList) {
										TLpglAuthorityVo tLpglAuthorityVo = new TLpglAuthorityVo();
										BeanUtils.copyProperties(tLpglAuthority, tLpglAuthorityVo);
										String code = tLpglAuthority.getCode();
										if (status!=null){
											if (tLpglAuthority.getStatus()==status){
												//一级页面
												if (code.matches("\\d{1,2}0000")) {
													firstPageList.add(tLpglAuthorityVo);
												}
												//二级页面
												if (code.matches("\\d{1,2}[1-9]000")) {
													tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 4) + "0000");
													secondPageList.add(tLpglAuthorityVo);
												}
												//三级级页面
												if (code.matches("\\d{1,2}[1-9][1-9]00")) {
													tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 3) + "000");
													thirdPageList.add(tLpglAuthorityVo);

												}
												//页面按钮
												if (code.matches("\\d{1,2}[1-9][1-9][1-9]0")) {
													tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 2) + "00");
													buttonList.add(tLpglAuthorityVo);
												}
											}

										}else {
											//一级页面
											if (code.matches("\\d{1,2}0000")) {
												firstPageList.add(tLpglAuthorityVo);
											}
											//二级页面
											if (code.matches("\\d{1,2}[1-9]000")) {
												tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 4) + "0000");
												secondPageList.add(tLpglAuthorityVo);
											}
											//三级级页面
											if (code.matches("\\d{1,2}[1-9][1-9]00")) {
												tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 3) + "000");
												thirdPageList.add(tLpglAuthorityVo);

											}
											//页面按钮
											if (code.matches("\\d{1,2}[1-9][1-9][1-9]0")) {
												tLpglAuthorityVo.setParentCode(code.substring(0, code.length() - 2) + "00");
												buttonList.add(tLpglAuthorityVo);
											}
										}

									}
								}

							}
						}


					}
				}
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("firstPageList", firstPageList);
			jsonObject.put("secondPageList", secondPageList);
			jsonObject.put("thirdPageList", thirdPageList);
			jsonObject.put("buttonList", buttonList);
//		jsonObject.put("totalList",totalList);

			return jsonObject;
		}
	}

	@Override
	public List<TLpglRole> findAllRole() {
		List<TLpglRole> tLpglRoles = MybatisPlus.getInstance().findAll(new TLpglRole(), new MybatisPlusBuild(TLpglRole.class)
			.eq(TLpglRole::getDeletedFlag, 0)
		);

		return tLpglRoles;
	}

	@Override
	public List<TLpglPosistion> findAllPosistion() {
		List<TLpglPosistion> list = MybatisPlus.getInstance().findAll(new TLpglPosistion(), new MybatisPlusBuild(TLpglPosistion.class)
			.eq(TLpglRole::getDeletedFlag, 0)
		);

		return list;
	}

	@Override
	public List<TLpglAuthority> findAllAuthority() {
		List<TLpglAuthority> list = MybatisPlus.getInstance().findAll(new TLpglAuthority(), new MybatisPlusBuild(TLpglAuthority.class)
			.eq(TLpglRole::getDeletedFlag, 0)
		);

		return list;
	}

	@Override
	public List<TLpglRole> findAllPosistionRole(Long posistionId) {
		List<TLpglPosistionRole> list = MybatisPlus.getInstance().findAll(new TLpglPosistionRole(), new MybatisPlusBuild(TLpglPosistionRole.class)
			.eq(TLpglPosistionRole::getDeletedFlag, 0)
			.eq(TLpglPosistionRole::getPosisitionId, posistionId)
		);
		List<TLpglRole> lpglRoles = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (TLpglPosistionRole tLpglPosistionRole : list) {
				TLpglRole role = MybatisPlus.getInstance().findOne(new TLpglRole(), new MybatisPlusBuild(TLpglRole.class)
					.eq(TLpglRole::getId, tLpglPosistionRole.getRoleId())
					.eq(TLpglRole::getDeletedFlag, 0)
				);
				if (role != null) {
					lpglRoles.add(role);
				}
			}
		}
		return lpglRoles;
	}

	@Override
	public List<TLpglAuthority> findAllRoleAuthority(Long roleId) {

		List<TLpglRoleAuthority> list = MybatisPlus.getInstance().findAll(new TLpglRoleAuthority(), new MybatisPlusBuild(TLpglRoleAuthority.class)
			.eq(TLpglRoleAuthority::getDeletedFlag, 0)
			.eq(TLpglRoleAuthority::getRoleId, roleId)
		);
		List<TLpglAuthority> tLpglAuthorities = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (TLpglRoleAuthority tLpglRoleAuthority : list) {
				TLpglAuthority authority = MybatisPlus.getInstance().findOne(new TLpglAuthority(), new MybatisPlusBuild(TLpglAuthority.class)
					.eq(TLpglAuthority::getId, tLpglRoleAuthority.getAuthorityId())
					.eq(TLpglAuthority::getDeletedFlag, 0)
				);
				if (authority != null) {
					tLpglAuthorities.add(authority);
				}
			}
		}
		return tLpglAuthorities;
	}

	@Override
	public JSONObject findAllPosistionRoleAuthority() {
		return null;
	}

	@Override
	public TLpglPosistion findAllPosistionById(Long posistionId) {
		TLpglPosistion posistion = MybatisPlus.getInstance().findOne(new TLpglPosistion(), new MybatisPlusBuild(TLpglPosistion.class)
			.eq(TLpglRole::getDeletedFlag, 0)
			.eq(TLpglRole::getId, posistionId)
		);
		return posistion;
	}

	@Override
	public int updatePosistionRole(Long posistionId, Long userId) {
		TLpglUserPosistion tLpglUserPosistion = new TLpglUserPosistion();
		tLpglUserPosistion.setUserId(userId);
		tLpglUserPosistion.setPosistionId(posistionId);
		int i = MybatisPlus.getInstance().save(tLpglUserPosistion);
		return i;
	}

	@Override
	public AjaxResult findRolePage(Long userId, Long id, Integer status) {
		AjaxResult ajaxResult = new AjaxResult();
		List<TLpglAuthority> firstPageList = new ArrayList();

		if (id==null){
			TLpglUserPosistion tLpglUserPosistion = MybatisPlus.getInstance().findOne(
				new TLpglUserPosistion(), new MybatisPlusBuild(TLpglUserPosistion.class)
					.eq(TLpglUserPosistion::getUserId, userId)
					.eq(TLpglUserPosistion::getDeletedFlag, 0)
			);
			//获取职位 角色关联
			if (tLpglUserPosistion != null && tLpglUserPosistion.getPosistionId() != null) {
				List<TLpglPosistionRole> tLpglPosistionRole = MybatisPlus.getInstance().findAll(new TLpglPosistionRole(), new MybatisPlusBuild(TLpglPosistionRole.class)
					.eq(TLpglPosistionRole::getPosisitionId, tLpglUserPosistion.getPosistionId())
					.eq(TLpglPosistionRole::getDeletedFlag, 0)
				);
				if (tLpglPosistionRole != null && tLpglPosistionRole.size() > 0) {
					for (TLpglPosistionRole lpglPosistionRole : tLpglPosistionRole) {
						//角色关联权限
						List<TLpglRoleAuthority> tLpglRoleAuthorityList = MybatisPlus.getInstance().findAll(new TLpglRoleAuthority(), new MybatisPlusBuild(TLpglRoleAuthority.class)
							.eq(TLpglRoleAuthority::getRoleId, lpglPosistionRole.getRoleId())
							.eq(TLpglRoleAuthority::getDeletedFlag, 0)
						);
						if (tLpglRoleAuthorityList != null && tLpglRoleAuthorityList.size() > 0) {
							for (TLpglRoleAuthority tLpglRoleAuthority : tLpglRoleAuthorityList) {
								//权限表
								List<TLpglAuthority> tLpglAuthoriyList = MybatisPlus.getInstance().findAll(new TLpglAuthority(), new MybatisPlusBuild(TLpglAuthority.class)
									.eq(TLpglAuthority::getId, tLpglRoleAuthority.getAuthorityId())
									.eq(TLpglAuthority::getDeletedFlag, 0)
									.eq(TLpglAuthority::getStatus, status)
								);
								//权限分配
								if (tLpglAuthoriyList != null && tLpglAuthoriyList.size() > 0) {
									for (TLpglAuthority tLpglAuthority : tLpglAuthoriyList) {
										if (tLpglAuthority.getParentId()==null){
											firstPageList.add(tLpglAuthority);
										}
									}
								}

							}
						}


					}
				}
			}
		}else {
			TLpglRoleAuthority tLpglRoleAuthority = MybatisPlus.getInstance().findOne(new TLpglRoleAuthority(),new MybatisPlusBuild(TLpglRoleAuthority.class).eq(TLpglRoleAuthority::getAuthorityId,id));
			if (tLpglRoleAuthority!=null&&tLpglRoleAuthority.getRoleId()!=null){
				TLpglPosistionRole tLpglPosistionRole = MybatisPlus.getInstance().findOne(new TLpglPosistionRole(),new MybatisPlusBuild(TLpglPosistionRole.class).eq(TLpglPosistionRole::getRoleId,tLpglRoleAuthority.getRoleId()));

				if (tLpglPosistionRole!=null&&tLpglPosistionRole.getPosisitionId()!=null){
					TLpglUserPosistion tLpglUserPosistion = MybatisPlus.getInstance().findOne(new TLpglUserPosistion(),new MybatisPlusBuild(TLpglUserPosistion.class)
						.eq(TLpglUserPosistion::getPosistionId,tLpglPosistionRole.getPosisitionId())
						.eq(TLpglUserPosistion::getUserId,userId)
					);
					if (tLpglUserPosistion!=null){
						List<TLpglAuthority> list = MybatisPlus.getInstance().findAll(new TLpglAuthority(),new MybatisPlusBuild(TLpglAuthority.class)
							.eq(TLpglAuthority::getParentId,id)
							.eq(TLpglAuthority::getStatus,status)
						);
						firstPageList = list;
					}

				}

			}
		}

		ajaxResult.setSuccess(true);
		ajaxResult.setData(firstPageList);
		return ajaxResult;

	}

	public static void main(String[] args) {
		String code = "1190000";
		System.out.println(code.matches("\\d{1,3}0000"));
		System.out.println(code.substring(0, code.length() - 4) + "0000");
	}
}
