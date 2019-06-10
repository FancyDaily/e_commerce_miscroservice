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
@Table
@Data
@Builder
public class CsqUserAuth {

	@Id
	private Long id;

	private Long userId;

	@Column(commit = "身份证号")
	private String cardId;

	@Column(commit = "姓名")
	private String name;

	@Column(commit = "营业执照图片")
	private String licensePic;

	private String licenseId;

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