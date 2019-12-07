package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.*;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单
 * @Author: FangyiXu
 * @Date: 2019-10-24 17:01
 */
@Table(commit = "订单")
@Data
@Builder
@NoArgsConstructor
public class TSdxBookOrderPo extends BaseEntity {

	@Column(commit = "快递单号")
	private String expressNo;

	@Column(commit = "订单号")
	private String orderNo;

	@Column(commit = "多个书本编号")
	private String bookIds;

	/*@Column(commit = "书本信息编号")
	private Long bookIfIs;*/
	@Column(commit = "书本信息编号")
	private String bookIfIs;

	@Column(commit = "购书人或者捐书人编号")
	private Long userId;

	@Column(commit = "状态")
	private Integer status;

	@Column(commit = "邮寄地址编号")
	private Long shippingAddressId;

	@Column(commit = "预计获得总积分", length = 11)
	private Integer expectedTotalScores;

	@Column(commit = "实际获得积分", length = 11)
	private Integer exactTotalScores;

	@Column(commit = "配送类型(邮寄、自送）", defaultVal = "1")
	private Integer shipType;

	@Column(commit = "书籍驿站编号")
	private Long bookStationId;

	@Column(commit = "订单类型(捐书、购书)")
	private Integer type;

	@Column(commit = "书本总价", precision = 2)
	private Double bookPrice;

	@Column(commit = "积分抵扣总额", length = 11)
	private Integer scoreDiscount;

	@Column(commit = "积分抵扣总金额", precision = 2)
	private Double scoreDiscountPrice;

	@Column(commit = "运费", precision = 2)
	private Double shipPirce;

	@Column(commit = "订单总价", precision = 2)
	private Double totalPrice;

	@Column(commit = "总价", precision = 2)
	private Double price;

	public TSdxBookDonateOrderVo copyTSdxBookDonateOrderVo() {return null;}

	public TSdxBookPurchaseOrderVo copyTSdxBookPurchaseOrderVo() {return null;}

	public SdxPurchaseOrderVo copySdxPurchaseOrderVo() {return null;}

	public SdxDonateOrderVo copySdxDonateOrderVo() {return null;}

	public static void main(String[] args) {
		AutoGenerateCode.generate(TSdxBookOrderPo.class);
	}
    public TSdxBookOrderVo  copyTSdxBookOrderVo() {
        return null;
     }

}
