package com.e_commerce.miscroservice.commons.exception.colligate;
/**
 * 功能描述:没有权限修改异常  暂时用在组织的分组中
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年1月15日 下午1:58:17
 */
public class NoAuthChangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoAuthChangeException(String message) {
		super(message);
	}
	
	

}
