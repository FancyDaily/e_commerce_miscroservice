package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.SdxWishListVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxWishListVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 心愿单
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Data
@Builder
@Table(commit = "心愿单")
@NoArgsConstructor
public class TSdxWishListPo extends BaseEntity {

	@Column(commit = "用户编号")
	private Long userId;
	@Column(commit = "书本信息编号")
	private Long bookInfoId;
	@Column(commit = "是否等待来货提醒")
	private Integer isWaitingForNotice;
    public TSdxWishListVo  copyTSdxWishListVo() {
        return null;
     }

	public SdxWishListVo copySdxWishListVo() {
		return null;
	}
}
