package com.e_commerce.miscroservice.user.vo;

import java.util.List;

/**
 * 功能描述:key-value 权益view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月3日 下午6:18:50
 */
public class MessageShowLIstView {
	private Long parent;
	private String userName;
	private String userUrl;
	private String content;
	private Long time;
	private String parentToString;
	private String serviceIdToString;
	private String toUserIdToString;
	private Boolean isPublish;
	private Integer status;
	private Integer isNotice;
	public Long getParent() {
		return parent;
	}
	public void setParent(Long parent) {
		this.parent = parent;
	}
	public String getUserName() {
		return userName;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getParentToString() {
		return parentToString;
	}
	public void setParentToString(Long parentToString) {
		this.parentToString = parentToString+"";
	}
	public String getServiceIdToString() {
		return serviceIdToString;
	}
	public void setServiceIdToString(Long serviceIdToString) {
		this.serviceIdToString = serviceIdToString + "";
	}
	public String getToUserIdToString() {
		return toUserIdToString;
	}
	public void setToUserIdToString(Long toUserIdToString) {
		this.toUserIdToString = toUserIdToString + "";
	}
	public Boolean getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsNotice() {
		return isNotice;
	}
	public void setIsNotice(Integer isNotice) {
		this.isNotice = isNotice;
	}
	
}
