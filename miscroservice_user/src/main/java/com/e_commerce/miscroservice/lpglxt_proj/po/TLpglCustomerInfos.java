package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户信息报备表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:58
 */
@Table(commit = "客户信息报备表")
@Data
@Builder
@NoArgsConstructor
public class TLpglCustomerInfos extends BaseEntity {

	@Column(commit = "楼盘编号")
	private Long estateId;

	@Column(commit = "商品房编号")
	private Long houseId;

	@Column(commit = "楼号", length = 11)
	private Integer buildingNum;

	@Column(commit = "单元号", length = 11)
	private String groupName;

	@Column(commit = "楼层", length = 11)
	private Integer floorNum;

	@Column(commit = "房间号")
	private Integer houseNum;

	@Column(commit = "申请用户编号")
	private Long applyUserId;

	@Column(commit = "审批用户编号")
	private Long certUserId;

	@Column(commit = "楼盘名字")
	private String estateName;

	@Column(commit = "商品房名称")
	private String houseName;

	@Column(commit = "客户名")
	private String customerName;

	@Column(commit = "手机号")
	private String telephone;

	@Column(commit = "看房时间")
	private String date;

	@Column(commit = "看房人数")
	private Integer customerNum;

	@Column(commit = "分销公司")
	private String companyName;

	@Column(commit = "业务员姓名")
	private String salesManName;

	@Column(commit = "业务员电话")
	private String salesManTel;

	@Column(commit = "自驾车牌号码")
	private String licenseName;

	/**
	 * 类型 - 自驾或接送
	 */
	@Column(commit = "类型")
	private Integer type;

	@Column(commit = "是否带看")
	private Integer isDone;

	@Column(commit = "区域")
	private String area;

	@Column(commit = "对接部门")
	private String department;

	@Column(commit = "报销金额")
	private Double reimbursementAmount;

	@Column(commit = "描述")
	private String description;

	@Column(commit = "状态", length = 11, defaultVal = "0")
	private Integer status;

}
