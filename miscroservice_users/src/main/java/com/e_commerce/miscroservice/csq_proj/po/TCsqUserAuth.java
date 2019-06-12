package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 实名认证(个人、机构)
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:42
 */
@Table(commit = "从善桥认证表")
@Data
@Builder
public class TCsqUserAuth extends BaseEntity {

	@Id
	private Long id;

	private Long userId;

	@Column(commit = "类型", isNUll = false)
	private Integer type;

	@Column(commit = "身份证号")
	private String cardId;

	@Column(commit = "姓名")
	private String name;

	@Column(commit = "营业执照图片")
	private String licensePic;

	@Column(commit = "营业执照编号")
	private String licenseId;

}
