package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.DependsOn;

import java.sql.Timestamp;

/**
 * 发票
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:45
 */
@Table(commit = "从善桥发票表")
@Data
@Builder
public class TCsqUserInvoice extends BaseEntity {

	@Id
	private Long id;

	private Long userId;

	@Transient
	private String dateString;

	@Transient
	private Integer recordCnt;	//发票下对应项目数量

	@Column(commit = "面额", precision = 2, isNUll = false)
	private Double amount;

	@Column(commit = "已出票(尤指纸质发票已经邮寄)", length = 11, defaultVal = "0", isNUll = false)
	private Integer isOut;

	@Column(commit = "订单编号(多)", length = 2048, isNUll = false)
	private String orderNos;

	@Column(commit = "类型(0个人、1企业)", length = 11, isNUll = false)
	private Integer type;

	@Column(commit = "抬头")
	private String name;

	@Column(commit = "税号", length = 11, isNUll = false)
	private Integer taxNo;

	@Column(commit = "地址")
	private String addr;

	@Column(commit = "收件人")
	private String person;

	@Column(commit = "联系方式")
	private String telephone;

}
