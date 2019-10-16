package com.e_commerce.miscroservice.lpglxt_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.lpglxt_proj.po.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 楼盘管理权限表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:31
 */
@Data
@Builder
@NoArgsConstructor
public class TLpglAuthorityVo{


	private Long id;

	@Column(commit = "权限名称")
	private String authorityName;

	@Column(commit = "父级Id")
	private Long parentId;


	/**
	 * 一级 1xxxx 二级 11xxx 三级111xx 按钮 1111x
	 */
	@Column(commit = "权限Code:1xxxx 一级页面 11xxx二级页面 。。。")
	private String code;

	/**
	 * 上级权限code
	 */
	private String parentCode;

	@Column(commit = "权限地址")
	private String url;

	@Column(commit = "操作类型：1 增 2 删 3 改 4 查",length = 4)
	private Integer operationType;

	//下级权限列表
	private List<TLpglAuthorityVo> childAuthority;



}
