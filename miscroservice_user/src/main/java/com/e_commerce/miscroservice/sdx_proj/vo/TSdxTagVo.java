package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTagPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书本类型
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxTagVo {
    
    /**
    *书本类型的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *名称
    *
    */
    
    private String name;
    
    /**
    *类型
    *
    */
    
    private Integer type;
    public TSdxTagPo copyTSdxTagPo() {
        return null;
    }
}
