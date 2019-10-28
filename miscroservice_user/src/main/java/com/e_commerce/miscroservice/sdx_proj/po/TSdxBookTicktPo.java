package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTicktVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 预定书券
 * @Author: FangyiXu
 * @Date: 2019-10-24 15:47
 */
@Data
@Builder
@Table(commit = "预定书券")
public class TSdxBookTicktPo extends BaseEntity{

	@Column(commit = "过期时间点")
	private Long expire;

	@Column(commit = "拥有者用户编号")
	private Long userId;

    public TSdxBookTicktVo  copyTSdxBookTicktVo() {
        return null;
     }
 
}
