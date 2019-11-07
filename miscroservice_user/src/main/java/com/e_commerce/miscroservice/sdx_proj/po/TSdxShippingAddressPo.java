package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShippingAddressVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配送地址
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Table(commit = "配送地址")
@Data
@Builder
@NoArgsConstructor
public class TSdxShippingAddressPo extends BaseEntity {

	@Column(commit = "用户编号")
	private Long userId;

	@Column(commit = "姓名")
	private String name;

	@Column(commit = "手机号")
	private String userTel;

	@Column(commit = "省")
	private String province;

	@Column(commit = "市")
	private String city;

	@Column(commit = "区/县")
	private String county;

	@Column(commit = "街道")
	private String street;

	@Column(commit = "详细地址")
	private String detailAddress;
    public TSdxShippingAddressVo  copyTSdxShippingAddressVo() {
        return null;
     }

}
