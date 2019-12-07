package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 积分流水
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxScoreRecordVo {

    /**
    *积分流水的Id,修改或者查询的需要
    *
    */

    private Long id;

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
	private Integer inOut;

	@Column(commit = "数额", length = 11, isNUll = false)
	private Integer amount;

	@Column(commit = "消耗金钱(支付场景)", precision = 2)
	private Double money;

	@Column(commit = "类型", isNUll = false)
	private Integer type;

    public TSdxScoreRecordPo copyTSdxScoreRecordPo() {
        return null;
    }
}
