package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookVo;



import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍
 * @Author: FangyiXu
 * @Date: 2019-10-23 14:14
 */
@Table(commit = "书袋熊书籍")
@Data
@Builder
@NoArgsConstructor
public class TSdxBookPo extends BaseEntity {

	@Column(commit = "公益项目编号")
	private Long serviceId;

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "当前拥有者编号")
	private Long currentOwnerId;

	@Column(commit = "捐助者编号")
	private Long donaterId;

	@Column(commit = "状态(捐助审核中(初始)，在书架，失效（被买走、审核失败）")
	private Integer status;

	@Column(commit = "预估折抵积分价值", length = 11)
	private Integer expectedScore;

	@Column(commit = "实际抵扣积分", length = 11)
	private Integer exactScore;

	@Column(commit = "实际售卖价格", precision = 2)
	private Double exactPrice;

	public static void main(String[] args) {
		AutoGenerateCode.generate(TSdxBookPo.class);
	}

    public TSdxBookVo  copyTSdxBookVo() {
        return null;
     }

}
