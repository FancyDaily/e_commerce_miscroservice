package com.e_commerce.miscroservice.commons.entity.application;

/**
 * @author 马晓晨
 * @date 2019/3/17
 */
public class AdminUser extends TUser {
	@Override
	public Long getId() {
		return 1L;
	}

	@Override
	public String getName() {
		return "系统管理员";
	}
}
