package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxWishListPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 心愿单
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxWishListVo {
    
    /**
    *心愿单的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *书本信息编号
    *
    */
    
    private Long bookInfoId;
    public TSdxWishListPo copyTSdxWishListPo() {
        return null;
    }
}
