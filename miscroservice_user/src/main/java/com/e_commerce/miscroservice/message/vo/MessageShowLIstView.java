package com.e_commerce.miscroservice.message.vo;

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
	private Long parent;//分组id
	private String userName;//用户姓名
	private String userUrl;//用户头像链接
	private String content;//内容
	private Long time;//时间
	private String parentToString;//string型的分组id
	private String toUserIdToString;//stirng型的对方用户id
	private Long unReadSum;//未读消息数量
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
	public String getToUserIdToString() {
		return toUserIdToString;
	}
	public void setToUserIdToString(Long toUserIdToString) {
		this.toUserIdToString = toUserIdToString + "";
	}
	public Long getUnReadSum() {
		return unReadSum;
	}
	public void setUnReadSum(Long unReadSum) {
		this.unReadSum = unReadSum;
	}
	
}
