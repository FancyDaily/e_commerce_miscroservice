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
	Long userId;

	@Column(commit = "书籍信息编号")
	String bookInfoIds;

	@Column(commit = "书籍编号")
	String bookIds;

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

    public TSdxScoreRecordPo copyTSdxScoreRecordPo() {
        return null;
    }
}
