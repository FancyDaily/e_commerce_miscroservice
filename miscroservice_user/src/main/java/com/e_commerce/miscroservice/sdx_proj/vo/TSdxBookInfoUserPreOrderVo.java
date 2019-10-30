package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoUserPreOrderPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍信息预定(书籍信息用户关系)
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookInfoUserPreOrderVo {
    
    /**
    *书籍信息预定(书籍信息用户关系)的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *用户编号
    *
    */
    
    private Long userId;
    
    /**
    *书籍信息编号
    *
    */
    
    private Long bookInfoId;
    public TSdxBookInfoUserPreOrderPo copyTSdxBookInfoUserPreOrderPo() {
        return null;
    }
}
