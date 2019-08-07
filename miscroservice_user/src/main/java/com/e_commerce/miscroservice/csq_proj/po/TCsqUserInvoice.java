package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发票
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:45
 */
@Table(commit = "从善桥发票表")
@Data
@Builder
@NoArgsConstructor
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

	@Column(commit = "抬头", charset = "utf8mb4")
	private String name;

	@Column(commit = "税号", length = 11, isNUll = false)
	private String taxNo;

	@Column(commit = "地址", charset = "utf8mb4")
	private String addr;

	@Column(commit = "收件人", charset = "utf8mb4")
	private String person;

	@Column(commit = "联系方式")
	private String telephone;

	@Column(commit = "快递单号")
	private String expressNo;

	public CsqUserInvoiceVo copyCsqUserInvoiceVo() {
		return null;
	}

}
