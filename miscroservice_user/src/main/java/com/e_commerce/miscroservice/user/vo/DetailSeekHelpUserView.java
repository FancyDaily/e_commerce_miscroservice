package com.e_commerce.miscroservice.user.vo;
/**
 * 功能描述:用户详情返回的用户view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月4日 下午5:23:19
 */
public class DetailSeekHelpUserView extends BaseUserView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 信息完整度
	 */
	private Integer integrity;
	
	public Integer getIntegrity() {
		return integrity;
	}
	public void setIntegrity(Integer integrity) {
		this.integrity = integrity;
	}
	
}
