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
public class NoticesListView {
	private static final long serialVersionUID = 1L;
	
	private Long time;
	
	private int isNotice;
	
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public int getIsNotice() {
		return isNotice;
	}
	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}
	
}
