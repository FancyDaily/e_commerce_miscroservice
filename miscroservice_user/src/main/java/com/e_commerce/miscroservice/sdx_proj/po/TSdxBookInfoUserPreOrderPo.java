package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoUserPreOrderVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍信息预定(书籍信息用户关系)
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Table(commit = "书籍信息预定(书籍信息用户关系)")
@Data
@Builder
@NoArgsConstructor
public class TSdxBookInfoUserPreOrderPo extends BaseEntity {

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "用户编号")
	private Long userId;

	public static void main(String[] args) {
		AutoGenerateCode.generate(TSdxBookInfoUserPreOrderPo.class);
	}

    public TSdxBookInfoUserPreOrderVo  copyTSdxBookInfoUserPreOrderVo() {
        return null;
     }
 
}
