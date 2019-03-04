package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table
public class TestTmpDTO extends BaseDTO{
	@Id
	private Long id;
	private Integer age;
	@Column(commit = "用户名",length = 52)
	private String userName;

	private String userPass;

	@Column(precision = 5,length = 10)
	private BigDecimal money;

	@Transient
	private String ksksk;



}
