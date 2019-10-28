package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoReserveVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 书籍预定信息
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Data
@Builder
@Table(commit = "书籍预定信息")
public class TSdxBookInfoReservePo extends BaseEntity {

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "预定用户编号")
	private Long userId;

	@Column(commit = "书券编号")
	private Long bookTicketId;
    public TSdxBookInfoReserveVo  copyTSdxBookInfoReserveVo() {
        return null;
     }

}
