package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 积分流水
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class SdxScoreRecordVo {

    /**
    *积分流水的Id,修改或者查询的需要
    *
    */

    private Long id;

	private String description;

	private String date;

	private Long timeStamp;

	/**
	 * 收入还是支出
	 */
	private Integer inOut;

	/**
	 * 积分数额
	 */
	private Integer amount;

    public TSdxScoreRecordPo copyTSdxScoreRecordPo() {
        return null;
    }
}
