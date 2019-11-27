package com.e_commerce.miscroservice.sdx_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import lombok.Builder;
import lombok.Data;

/**
 * 购书订单编号
 * @Author: FangyiXu
 * @Date: 2019-10-24 17:12
 */
@Data
@Builder
public class SdxPurchaseOrderVo {

	private Long timeStamp;

	/**
	 * 日期
	 */
	private String monthDay;

	/**
	 * 带年份的日期
	 */
	private String wholeDate;

	@Column(commit = "快递单号")
	private String expressNo;

	@Column(commit = "多个书本编号")
	private String bookIds;

	@Column(commit = "书本信息编号")
	private Long bookInfoIds;

	@Column(commit = "状态")
	private Integer status;

	@Column(commit = "邮寄地址编号")
	private Long shippingAddressId;

	@Column(commit = "书本总价", precision = 2)
	private Double bookPrice;

	@Column(commit = "抵扣积分总额", length = 11)
	private Integer scoreUsed;

	@Column(commit = "运费")
	private Double shipPirce;

	@Column(commit = "订单总价")
	private Double totalPrice;

	@Column(commit = "总价", precision = 2)
	private Double price;

	/**
	 * 收件信息
	 */
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

	public TSdxBookOrderPo copyTSdxBookOrder() {return null;}

	public void setAddress(TSdxShippingAddressPo po) {
		this.name = po.getName();
		this.userTel = po.getUserTel();
		this.province = po.getProvince();
		this.city = po.getCity();
		this.county = po.getCounty();
		this.street = po.getStreet();
		this.detailAddress = po.getDetailAddress();
	}
}
