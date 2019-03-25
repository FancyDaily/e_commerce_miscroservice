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
 * 创建时间:2018年11月7日 上午11:26:47
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class UserTimeRecordParamView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userId;	//用户id
	private String beetLeft; //开始时间戳
	private String beetRight;	//结束时间戳
	private String opt;	//收入还是支出
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getBeetLeft() {
		return beetLeft;
	}
	public void setBeetLeft(String beetLeft) {
		this.beetLeft = beetLeft;
	}
	public String getBeetRight() {
		return beetRight;
	}
	public void setBeetRight(String beetRight) {
		this.beetRight = beetRight;
	}
	
}
