package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.service.SdxShoppingTrolleysService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxShoppingTrolleysVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShoppingTrolleysVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Data
@Builder
@NoArgsConstructor
@Table(commit = "购物车")
public class TSdxShoppingTrolleysPo extends BaseEntity {

	@Column(commit = "用户编号")
	private Long userId;

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "关联项目编号")
	private Long serviceId;

    public TSdxShoppingTrolleysVo  copyTSdxShoppingTrolleysVo() {
        return null;
     }

 	public SdxShoppingTrolleysVo copySdxShoppingTrolleysVo() {return null;}
}
