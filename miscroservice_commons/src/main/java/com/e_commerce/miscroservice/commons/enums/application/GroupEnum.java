package com.xiaoshitimebank.app.constant;
/**
 * 功能描述:组织枚举
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年1月14日 下午3:38:43
 */
public enum GroupEnum {
	/**
	 * 分组的权限为默认分组  不可以修改，不可以删除 值为0
	 */
	AUTH_DEFAULT(0,"默认分组"),
	/**
	 * 分组的权限为用户创建分组  可以修改，可以删除 值为1
	 */
	AUTH_CREATED(1,"用户创建分组");
	
	private int value;
	private String desc;
	
	GroupEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String getDesc() {
		return this.desc;
	}
}
