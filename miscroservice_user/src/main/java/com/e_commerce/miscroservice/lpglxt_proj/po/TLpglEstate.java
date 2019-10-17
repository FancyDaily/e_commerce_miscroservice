package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 楼盘表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:56
 */
@Table(commit = "楼盘表")
@Data
@Builder
@NoArgsConstructor
public class TLpglEstate extends BaseEntity {

	@Column(commit = "名字")
	private String name;

}
