package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
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
@Table
@Data
@Builder
public class CsqUserInvoice {

	@Id
	private Long id;

	@Column(commit = "类型(0个人、1企业)")
	private Integer type;

	@Column(commit = "抬头")
	private String name;

	@Column(commit = "税号")
	private Integer taxNo;

	@Column(commit = "地址")
	private String addr;

	@Column(commit = "收件人")
	private String person;

	@Column(commit = "联系方式")
	private String telephone;

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
