package com.e_commerce.miscroservice.sdx_proj.vo;
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
    public TSdxScoreRecordPo copyTSdxScoreRecordPo() {
        return null;
    }
}
