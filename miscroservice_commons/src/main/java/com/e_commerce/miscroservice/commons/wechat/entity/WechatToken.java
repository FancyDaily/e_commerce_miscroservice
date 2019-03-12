package com.e_commerce.miscroservice.commons.wechat.entity;
/**
 * 功能描述:定义微信token实体
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 20, 2018 3:10:34 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class WechatToken extends WechatError{

	private String access_token;
	//过期时间
	private String expires_in;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	
	
	
}
