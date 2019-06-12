package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 基金账户
 *
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:47
 */
@Table
@Data
@Builder
public class CsqFund  extends BaseEntity{

	@Id
	private Long id;

	private Long userId;

	@Column(commit = "余额")
	private Double balance;


}
