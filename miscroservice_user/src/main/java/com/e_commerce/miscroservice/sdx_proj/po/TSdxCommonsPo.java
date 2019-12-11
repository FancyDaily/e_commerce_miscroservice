package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxCommonsVo;



import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(commit = "用户评论")
@Data
@Builder
@NoArgsConstructor
public class TSdxCommonsPo extends BaseEntity {

	@Column(commit = "动态id")
	private Integer trendsId;//评论的哪条动态；

	@Column(commit = "用户ID")
	private Long userId;

	@Column(commit = "被评论的好友Id")
	private Long friendId;

	@Column(commit = "评论内容")
	private String contentInfo;


    public TSdxCommonsVo  copyTSdxCommonsVo() {
        return null;
     }

}
