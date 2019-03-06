package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2019年1月25日 上午10:18:29
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class CompanyRecentView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] helpRecent;
	private int[] servRecent;
	private int[] addMemberRecent;
	
	private int[] inRecent;
	private int[] outRecent;
	
	public int[] getHelpRecent() {
		return helpRecent;
	}
	public void setHelpRecent(int[] helpRecent) {
		this.helpRecent = helpRecent;
	}
	public int[] getServRecent() {
		return servRecent;
	}
	public void setServRecent(int[] servRecent) {
		this.servRecent = servRecent;
	}
	public int[] getAddMemberRecent() {
		return addMemberRecent;
	}
	public void setAddMemberRecent(int[] addMemberRecent) {
		this.addMemberRecent = addMemberRecent;
	}
	public int[] getInRecent() {
		return inRecent;
	}
	public void setInRecent(int[] inRecent) {
		this.inRecent = inRecent;
	}
	public int[] getOutRecent() {
		return outRecent;
	}
	public void setOutRecent(int[] outRecent) {
		this.outRecent = outRecent;
	}
	
}
