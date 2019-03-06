package com.e_commerce.miscroservice.user.vo;


/**
 * 功能描述:用户ID，name，头像 ，状态 view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月3日 下午6:18:50
 */
public class UserIdNameUrlInfoView {
	private Long id;

	private String name;

	private String userHeadPortraitPath;
	
	private int status;
	
	private String toStringId;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getToStringId() {
		return toStringId;
	}

	public void setToStringId(Long toStringId) {
		this.toStringId = toStringId+"";
	}
	
	
}
