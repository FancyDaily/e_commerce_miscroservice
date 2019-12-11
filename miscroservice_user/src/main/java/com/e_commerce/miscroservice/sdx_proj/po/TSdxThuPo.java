package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxThuVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(commit = "动态点赞")
@Data
@Builder
@NoArgsConstructor
public class TSdxThuPo extends BaseEntity {
	@Column(commit = "动态ID")
	private Integer trendsId;

	@Column(commit = "用户ID")
	private Integer userId;

	@Column(commit = "用户名")
	private String userName;

	@Column(commit = "点赞类型")//点赞/点踩
	private Integer type;

	@Column(commit = "类型名")
	private String typeName;

//	public static void main(String[] args) {
//		AutoGenerateCode.generate(com.e_commerce.miscroservice.sdx_proj.po.TSdxThuPo.class);
//	}
    public TSdxThuVo  copyTSdxThuVo() {
        return null;
     }

}
