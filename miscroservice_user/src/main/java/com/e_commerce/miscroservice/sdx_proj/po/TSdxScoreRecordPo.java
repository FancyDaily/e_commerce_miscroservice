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
	private String date;

	@Column(commit = "用户编号")
	private Long userId;

	@Column(commit = "书籍信息编号")
	private String bookInfoIds;

	@Column(commit = "书籍编号")
	private String bookIds;

	@Column(commit = "读后感编号")
	private Long bookAfterReadingNoteId;

	@Column(commit = "订单号")
	private Long orderId;

	@Column(commit = "描述")
	private String description;

	@Column(commit = "收入或支出", isNUll = false)
	private Integer inOrOut;

	@Column(commit = "数额", length = 11, isNUll = false)
	private Integer amount;

	@Column(commit = "消耗金钱(支付场景)", precision = 2)
	private Double money;

	@Column(commit = "类型", isNUll = false)
	private Integer type;

    public TSdxScoreRecordVo  copyTSdxScoreRecordVo() {
        return null;
     }

	public SdxScoreRecordVo copySdxScoreRecordVo() {
		return null;
	}
}
