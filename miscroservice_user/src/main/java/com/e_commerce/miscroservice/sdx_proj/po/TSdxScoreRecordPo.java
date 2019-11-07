package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.SdxScoreRecordVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxScoreRecordVo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 积分流水
 * @Author: FangyiXu
 * @Date: 2019-10-23 14:14
 */
@Data
@Table(commit = "积分流水")
@Builder
@NoArgsConstructor
public class TSdxScoreRecordPo extends BaseEntity {

	/**
	 * 发生日期
	 */
	@Transient
	String date;

	@Column(commit = "用户编号")
	Long userId;

	@Column(commit = "书籍信息编号")
	String bookInfoIds;

	@Column(commit = "读后感编号")
	Long bookAfterReadingNoteId;

	@Column(commit = "订单号")
	Long orderId;

	@Column(commit = "描述")
	String description;

	@Column(commit = "收入或支出", isNUll = false)
	Integer inOut;

	@Column(commit = "数额", length = 11, isNUll = false)
	Integer amount;

	@Column(commit = "消耗金钱(支付场景)", precision = 2)
	Double money;

	@Column(commit = "类型", isNUll = false)
	Integer type;

    public TSdxScoreRecordVo  copyTSdxScoreRecordVo() {
        return null;
     }

	public SdxScoreRecordVo copySdxScoreRecordVo() {
		return null;
	}
}
