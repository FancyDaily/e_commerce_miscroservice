package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxTagVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书本类型
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Table(commit = "书本类型")
@Data
@Builder
@NoArgsConstructor
public class TSdxTagPo extends BaseEntity {

	@Column(commit = "名称")
	private String name;

	@Column(commit = "类型", defaultVal = "1")
	private Integer type;

	public static void main(String[] args) {
		AutoGenerateCode.generate(TSdxTagPo.class);
	}
    public TSdxTagVo  copyTSdxTagVo() {
        return null;
     }

}
