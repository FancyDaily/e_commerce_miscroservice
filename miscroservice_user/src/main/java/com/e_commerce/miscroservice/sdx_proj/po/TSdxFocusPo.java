package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxFocusVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(commit = "关注书友")
@Builder
@NoArgsConstructor
public class TSdxFocusPo extends BaseEntity {

	@Column(commit = "用户ID")
	private Long userId;

	@Column(commit = "书友ID")
	private Long bookFriendId;

	@Column(commit = "书友名称")
	private String bookFriendName;

	@Column(commit = "书友头像")
	private String bookFriendPic;

	@Column(commit = "关注类型")
	private Integer type;

	@Column(commit = "关注类型名称")
	private String typeName;

//	public static void main(String[] args) {
//		AutoGenerateCode.generate(com.e_commerce.miscroservice.sdx_proj.po.TSdxFocusPo.class);
//	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public TSdxFocusVo copyTSdxFocusVo() {
		return null;
	}

}
