package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.lpglxt_proj.vo.LpglFloorHouseMapVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品房表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:58
 */
@Table(commit = "商品房表")
@Data
@Builder
@NoArgsConstructor
public class TLpglHouse extends BaseEntity {

	/**
	 * 楼盘编号
	 */
	@Column(commit = "楼盘编号")
	private Long estateId;

	/**
	 * 房间号
	 */
	@Column(commit = "房间号", length = 11)
	private Integer houseNum;

	/**
	 * 房间名称
	 */
	@Column(commit = "房间名称")
	private String name;

	/**
	 * 户型
	 */
	@Column(commit = "户型")
	private String houseType;

	/**
	 * 房源结构
	 */
	@Column(commit = "房源结构")
	private String structType;

	/**
	 * 楼号
	 */
	@Column(commit = "楼号", length = 11)
	private Integer buildingNum;

	/**
	 * 单元号
	 */
	@Column(commit = "单元号", length = 11)
	private String groupName;

	/**
	 * 楼层
	 */
	@Column(commit = "楼层", length = 11)
	private Integer floorNum;

	/**
	 * 朝向(东北到西北，分为八个方位)
	 */
	@Column(commit = "朝向")
	private String direction;

	/**
	 * 建筑面积
	 */
	@Column(commit = "建筑面积", precision = 2)
	private Double buildingArea;

	/**
	 * 室内面积
	 */
	@Column(commit = "室内面积", precision = 2)
	private Double insideArea;

	/**
	 * 建筑单价
	 */
	@Column(commit = "建筑单价", precision = 2)
	private Double buildingPrice;

	/**
	 * 套内单价
	 */
	@Column(commit = "室内单价", precision = 2)
	private Double insidePrice;

	@Column(commit = "总价", precision = 2)
	private Double totalPrice;

	@Column(commit = "优惠总价", precision = 2)
	private Double disCountPrice;

	@Column(commit = "优惠状态", length = 11)
	private Integer disCountStatus;

	@Column(commit = "销售员编号")
	private Long saleManId;

	/**
	 * 状态
	 */
	@Column(commit = "状态", length = 11, defaultVal = "0")
	private Integer status;

	@Column(commit = "首付")
	private Double downPayment;

	@Column(commit = "贷款金额")
	private Double loanAmount;

	@Column(commit = "购买日期")
	private String buyDate;

	@Column(commit = "签约日期")
	private String signDate;

	@Column(commit = "买受人姓名")
	private String purchaserName;

	@Column(commit = "买受人电话")
	private String purchaserTel;

	@Column(commit = "市场专员编号")
	private Long marketingCommisionerId;

	@Column(commit = "分销公司名字")
	private String companyName;

	@Column(commit = "自行车位编号")
	private String bicycleNum;

	@Column(commit = "自行车位总价")
	private String bicyclePrice;

	@Column(commit = "汽车位总价")
	private String carPrice;

	public LpglFloorHouseMapVo copyLpglFloorHouseMapVo() {return null;}

}
