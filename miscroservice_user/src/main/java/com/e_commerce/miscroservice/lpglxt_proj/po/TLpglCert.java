package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审批表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:58
 */
@Table(commit = "商品房表")
@Data
@Builder
@NoArgsConstructor
public class TLpglCert extends BaseEntity {

	@Transient
	private Long estateId;

	@Transient
	private Integer floorNum;

	@Transient
	private String houseName;

	@Transient
	private String customerName;

	@Transient
	private String telephone;

	@Transient
	private String date;

	@Transient
	private Integer customerNum;

	@Transient
	private String companyName;

	@Transient
	private String salesManName;

	@Transient
	private String salesManTel;

	@Transient
	private String licenseName;

	/**
	 * 类型 - 自驾或接送
	 */
	@Transient
	private Integer driveType;

	@Transient
	private Integer isDone;

	@Transient
	private String area;

	@Transient
	private String department;

	@Transient
	private Double reimbursementAmount;

	/**
	 * 楼盘编号
	 */
	@Transient
	private String estateName;

	/**
	 * 房间号
	 */
	@Transient
	private Integer houseNum;

	/**
	 * 楼号
	 */
	@Transient
	private Integer buildingNum;

	/**
	 * 单元号
	 */
	@Transient
	private String groupName;

	@Column(commit = "商品房编号")
	private Long houseId;

	@Column(commit = "申请用户编号")
	private Long applyUserId;

	@Column(commit = "审批用户编号")
	private Long certUserId;

	@Column(commit = "客户报备编号")
	private Long customerInfoId;

	@Column(commit = "类型", defaultVal = "1")
	private Integer type;

	@Column(commit = "优惠价")
	private Double discountPrice;

	@Column(commit = "描述")
	private String description;

	@Column(commit = "状态", defaultVal = "0")
	private Integer status;

	public void setCustomerInfo(TLpglCustomerInfos customerInfo) {
		if(customerInfo == null) return;

		this.estateId = customerInfo.getEstateId();

		this.floorNum = customerInfo.getFloorNum();

		this.houseName = customerInfo.getHouseName();

		this.customerName = customerInfo.getCustomerName();

		this.telephone = customerInfo.getTelephone();

		this.date = customerInfo.getDate();

		this.customerNum = customerInfo.getCustomerNum();

		this.companyName = customerInfo.getCompanyName();

		this.salesManName = customerInfo.getSalesManName();

		this.salesManTel = customerInfo.getSalesManTel();

		this.licenseName = customerInfo.getLicenseName();

		this.driveType = customerInfo.getType();

		this.isDone = customerInfo.getIsDone();

		this.area = customerInfo.getArea();

		this.department = customerInfo.getDepartment();

		this.reimbursementAmount = customerInfo.getReimbursementAmount();
	}

}
