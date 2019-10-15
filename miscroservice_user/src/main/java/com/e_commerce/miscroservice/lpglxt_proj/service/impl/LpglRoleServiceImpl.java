package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglAuthority;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistionRole;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglRoleAuthority;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUserPosistion;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglPositionService;
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
			new TLpglUserPosistion(),new MybatisPlusBuild(TLpglUserPosistion.class)
				.eq(TLpglUserPosistion::getUserId,userId)
				.eq(TLpglUserPosistion::getDeletedFlag,0)
		);

		List<TLpglAuthorityVo> firstPageList = new ArrayList();
		List<TLpglAuthorityVo> secondPageList = new ArrayList();
		List<TLpglAuthorityVo> thirdPageList = new ArrayList();
		List<TLpglAuthorityVo> buttonList = new ArrayList();
//		List<TLpglAuthorityVo> totalList = new ArrayList();
		//获取职位 角色关联
		if (tLpglUserPosistion!=null&&tLpglUserPosistion.getPosistionId()!=null){
			List<TLpglPosistionRole> tLpglPosistionRole = MybatisPlus.getInstance().findAll(new TLpglPosistionRole(),new MybatisPlusBuild(TLpglPosistionRole.class)
				.eq(TLpglPosistionRole::getPosisitionId,tLpglUserPosistion.getPosistionId())
				.eq(TLpglPosistionRole::getDeletedFlag,0)
			);
			if (tLpglPosistionRole!=null&&tLpglPosistionRole.size()>0){
				for (TLpglPosistionRole lpglPosistionRole : tLpglPosistionRole) {
					//角色关联权限
					List<TLpglRoleAuthority> tLpglRoleAuthorityList =   MybatisPlus.getInstance().findAll(new TLpglRoleAuthority(),new MybatisPlusBuild(TLpglRoleAuthority.class)
						.eq(TLpglRoleAuthority::getRoleId,lpglPosistionRole.getRoleId())
						.eq(TLpglRoleAuthority::getDeletedFlag,0)
					);
					if (tLpglRoleAuthorityList!=null&&tLpglRoleAuthorityList.size()>0){
						for (TLpglRoleAuthority tLpglRoleAuthority : tLpglRoleAuthorityList) {
							//权限表
							List<TLpglAuthority> tLpglAuthoriyList =   MybatisPlus.getInstance().findAll(new TLpglAuthority(),new MybatisPlusBuild(TLpglAuthority.class)
								.eq(TLpglAuthority::getId,tLpglRoleAuthority.getAuthorityId())
								.eq(TLpglAuthority::getDeletedFlag,0)
							);
							//权限分配
							if (tLpglAuthoriyList!=null&&tLpglAuthoriyList.size()>0){
								for (TLpglAuthority tLpglAuthority : tLpglAuthoriyList) {
									TLpglAuthorityVo tLpglAuthorityVo = new TLpglAuthorityVo();
									BeanUtils.copyProperties(tLpglAuthority,tLpglAuthorityVo);
									String code = tLpglAuthority.getCode();
									//一级页面
									if (code.matches("\\d{1,2}0000")){
										firstPageList.add(tLpglAuthorityVo);
									}
									//二级页面
									if (code.matches("\\d{1,2}[1-9]000")){
										tLpglAuthorityVo.setParentCode(code.substring(0,code.length()-4)+"0000");
										secondPageList.add(tLpglAuthorityVo);
									}
									//三级级页面
									if (code.matches("\\d{1,2}[1-9][1-9]00")){
										tLpglAuthorityVo.setParentCode(code.substring(0,code.length()-3)+"000");
										thirdPageList.add(tLpglAuthorityVo);

									}
									//页面按钮
									if (code.matches("\\d{1,2}[1-9][1-9][1-9]0")){
										tLpglAuthorityVo.setParentCode(code.substring(0,code.length()-2)+"00");
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
		jsonObject.put("firstPageList",firstPageList);
		jsonObject.put("secondPageList",secondPageList);
		jsonObject.put("thirdPageList",thirdPageList);
		jsonObject.put("buttonList",buttonList);
//		jsonObject.put("totalList",totalList);

		return jsonObject;
	}

	public static void main(String[] args) {
		String code = "1190001";
		System.out.println(code.matches("\\d{1,3}0000"));
		System.out.println(code.substring(0,code.length()-4)+"0000");
	}
}
