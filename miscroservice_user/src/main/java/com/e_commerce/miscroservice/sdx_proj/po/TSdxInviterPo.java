package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxInviterVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 邀请人信息
 * @Author: FangyiXu
 * @Date: 2019-10-23 14:14
 */
@Data
@Builder
@Table(commit = "邀请人信息")
public class TSdxInviterPo extends BaseEntity {

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "邀请者")
	private Long inviterId;

	@Column(commit = "被邀请人")
	private Long userId;

    public TSdxInviterVo  copyTSdxInviterVo() {
        return null;
     }

}
