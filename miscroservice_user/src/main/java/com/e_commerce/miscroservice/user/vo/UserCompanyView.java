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
 * 创建时间:2019年1月14日 下午12:08:24
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class UserCompanyView implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idString;
	private Integer monthlyHelpCnt;
	private Integer monthlyServCnt;
	private Integer newlyJoinMemberCnt;
	private Integer memberCnt;
	private Integer avaliable_time;
	
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	public Integer getMonthlyHelpCnt() {
		return monthlyHelpCnt;
	}
	public void setMonthlyHelpCnt(Integer monthlyHelpCnt) {
		this.monthlyHelpCnt = monthlyHelpCnt;
	}
	public Integer getMonthlyServCnt() {
		return monthlyServCnt;
	}
	public void setMonthlyServCnt(Integer monthlyServCnt) {
		this.monthlyServCnt = monthlyServCnt;
	}
	public Integer getNewlyJoinMemberCnt() {
		return newlyJoinMemberCnt;
	}
	public void setNewlyJoinMemberCnt(Integer newlyJoinMemberCnt) {
		this.newlyJoinMemberCnt = newlyJoinMemberCnt;
	}
	public Integer getMemberCnt() {
		return memberCnt;
	}
	public void setMemberCnt(Integer memberCnt) {
		this.memberCnt = memberCnt;
	}
	public Integer getAvaliable_time() {
		return avaliable_time;
	}
	public void setAvaliable_time(Integer avaliable_time) {
		this.avaliable_time = avaliable_time;
	}
}
