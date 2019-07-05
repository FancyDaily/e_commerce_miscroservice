package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-05 09:21
 */
@Data(matchSuffix = true)
public class CsqUserAuthVo {
	@Id
	private Long id;

	private Long userId;

	@Column(commit = "类型", isNUll = false)
	private Integer type;

	@Column(commit = "身份证号")
	private String cardId;

	@Column(commit = "姓名")
	private String name;

	@Column(commit = "手机号")
	private String phone;

	@Column(commit = "营业执照图片")
	private String licensePic;

	@Column(commit = "营业执照编号")
	private String licenseId;

	@Column(commit = "状态", length = 11, defaultVal = "0")
	private Integer status;

	public TCsqUserAuth copyTCsqUserAuth() {
		return null;
	}

}
