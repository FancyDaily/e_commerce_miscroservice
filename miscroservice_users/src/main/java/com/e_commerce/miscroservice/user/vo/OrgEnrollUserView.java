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
public class OrgEnrollUserView {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceIdToString; //string 型的serviceId
	private String title; //标题
	private Integer status; //状态
	private String startTime; //开始时间
	private String endTime; //结束时间
	private Boolean isRepeat; //是否为重复周期
	private Long enrollSum; //报名人数
	private Long chooseUserSum; //已选人数
	private Long canChooseSum; //可选人数
	
	public String getServiceIdToString() {
		return serviceIdToString;
	}
	public void setServiceIdToString(Long serviceIdToString) {
		this.serviceIdToString = serviceIdToString+"";
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Boolean getIsRepeat() {
		return isRepeat;
	}
	public void setIsRepeat(Boolean isRepeat) {
		this.isRepeat = isRepeat;
	}
	public Long getEnrollSum() {
		return enrollSum;
	}
	public void setEnrollSum(Long enrollSum) {
		this.enrollSum = enrollSum;
	}
	public Long getChooseUserSum() {
		return chooseUserSum;
	}
	public void setChooseUserSum(Long chooseUserSum) {
		this.chooseUserSum = chooseUserSum;
	}
	public Long getCanChooseSum() {
		return canChooseSum;
	}
	public void setCanChooseSum(Long canChooseSum) {
		this.canChooseSum = canChooseSum;
	}
}
