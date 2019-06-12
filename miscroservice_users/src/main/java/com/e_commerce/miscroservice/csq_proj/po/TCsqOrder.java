package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 涉及应用外支付的订单表
 * @Author: FangyiXu
 * @Date: 2019-06-11 17:57
 */
@Table(commit = "应用外支付的订单表")
@Data
@Builder
public class TCsqOrder {

	@Id
	private Long id;

	private Long userId;

	private Long fundId;

	private Long serviceId;

	@Column(commit = "订单编号")
	private String orderNo;

	@Column(commit = "订单金额", precision = 2, isNUll = false)
	private Double price;

	@Column(commit = "订单状态", length = 11, defaultVal = "1", isNUll = false)
	private Integer status;

	@Column(commit = "订单创建时间戳")
	private Long orderTime;

	@Column(commit = "扩展字段")
	private String extend;

	@Column(commit = "创建者编号", isNUll = false)
	private Long createUser;

	@Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Timestamp createTime;

	@Column(commit = "更新者编号", isNUll = false)
	private Long updateUser;

	@Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
	private Timestamp updateTime;

	@Column(commit = "有效性", defaultVal = "1")
	private String isValid;

}
