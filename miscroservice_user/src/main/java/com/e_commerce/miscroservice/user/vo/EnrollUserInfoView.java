package com.e_commerce.miscroservice.user.vo;

import java.util.List;

/**
 * 功能描述:mainKey-theValue 权益view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月3日 下午6:18:50
 */
public class EnrollUserInfoView {
	
	private static final long serialVersionUID = 1L;
	
	private String userIdToString; //string 型的userid
	private String userNameForTeam; //名字
	private String userName; //用户姓名
	private String userUrl; //用户头像
	private Integer sex; //性别
	private String age; //年龄
	private String occupation; //职业
	private Boolean isGroup; //是否是组织成员
	private String skill; //技能
	private Integer serve_num; //服务次数
	private int total_eva; //评价总分
	private String creatTime; //申请时间
	private Integer status; //状态
	public String getUserIdToString() {
		return userIdToString;
	}
	public void setUserIdToString(Long userIdToString) {
		this.userIdToString = userIdToString+"";
	}
	public String getUserName() {
		return userName;
	}
	public String getUserNameForTeam() {
		return userNameForTeam;
	}
	public void setUserNameForTeam(String userNameForTeam) {
		this.userNameForTeam = userNameForTeam;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserUrl() {
		return userUrl;
	}
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public Boolean getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public Integer getServe_num() {
		return serve_num;
	}
	public void setServe_num(Integer serve_num) {
		this.serve_num = serve_num;
	}
	public int getTotal_eva() {
		return total_eva;
	}
	public void setTotal_eva(int total_eva) {
		this.total_eva = total_eva;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
