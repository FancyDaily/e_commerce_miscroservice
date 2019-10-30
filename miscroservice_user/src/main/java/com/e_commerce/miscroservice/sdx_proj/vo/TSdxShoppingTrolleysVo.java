package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 购物车
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxShoppingTrolleysVo {
    
    /**
    *购物车的Id,修改或者查询的需要
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
    public TSdxShoppingTrolleysPo copyTSdxShoppingTrolleysPo() {
        return null;
    }
}
