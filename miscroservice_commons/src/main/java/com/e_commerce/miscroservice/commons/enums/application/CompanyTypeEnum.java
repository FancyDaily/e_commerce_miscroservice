package com.e_commerce.miscroservice.commons.enums.application;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年12月20日 下午5:06:41
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum CompanyTypeEnum {
	TYPE_ONE(1,"民办非企业单位"),
	TYPE_TWO(2,"社会团体"),
	TYPE_THREE(3,"事业单位"),
	TYPE_FOUR(4,"政府机关"),
	TYPE_FIVE(5,"企业");
	
	private Integer code;
	private String name;
	public Integer getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	private CompanyTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}
}
