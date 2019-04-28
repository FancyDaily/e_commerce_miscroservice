package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月7日 下午3:45:55
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class BaseUserView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户id
	 */
	private Long id;
	/**
	 * 用户名称
	 */
	private String name;
	/**
	 * 用户头像
	 */
	private String userHeadPortraitPath;
	/**
	 * 用户服务次数
	 */
	private Integer serveNum;
	/**
	 * 总评价
	 */
	private Integer totalEvaluate;
	/**
	 * 用户技能
	 */
	private String skill;
	/**
	 * 是否是达人
	 */
	private Integer masterStatus;
	/**
	 * 用户职业
	 */
	private String occupation;
	/**
	 * 关注状态 1、显示关注 2、显示已关注
	 */
	private Integer careStatus;
	/**
	 * 订单详情中用户小点的显示状态  1、未到  、2、异常  3、已到
	 */
	private Integer pointStatus;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 用户手机号
	 */
	private String userTel;
	/**
	 * 性别  0 未知  1、男 2、女
	 */
	private Integer sex;
	
	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * 
	 * 功能描述:获取字符串id
	 * 作者:马晓晨
	 * 创建时间:2018年11月19日 下午12:13:09
	 * @return
	 */
	public String getIdString() {
		return id + "";
	}
	
	public Integer getPointStatus() {
		return pointStatus;
	}

	public void setPointStatus(Integer pointStatus) {
		this.pointStatus = pointStatus;
	}


	public Integer getMasterStatus() {
		return masterStatus;
	}

	public void setMasterStatus(Integer masterStatus) {
		this.masterStatus = masterStatus;
	}

	public Integer getCareStatus() {
		return careStatus;
	}
	public void setCareStatus(Integer careStatus) {
		this.careStatus = careStatus;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public Integer getServeNum() {
		return serveNum;
	}
	public void setServeNum(Integer serveNum) {
		this.serveNum = serveNum;
	}
	public Integer getTotalEvaluate() {
		return totalEvaluate;
	}
	public void setTotalEvaluate(Integer totalEvaluate) {
		this.totalEvaluate = totalEvaluate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserHeadPortraitPath() {
		return userHeadPortraitPath;
	}
	public void setUserHeadPortraitPath(String userHeadPortraitPath) {
		this.userHeadPortraitPath = userHeadPortraitPath;
	}

}
