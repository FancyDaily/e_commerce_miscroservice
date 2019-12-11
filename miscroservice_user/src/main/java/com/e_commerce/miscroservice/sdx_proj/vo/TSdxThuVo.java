package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxThuPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 动态点赞
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxThuVo {
    
    /**
    *动态点赞的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *点赞类型
    *
    */
    
    private Integer type;
    
    /**
    *用户ID
    *
    */
    
    private Integer userId;
    
    /**
    *动态ID
    *
    */
    
    private Integer trendsId;
    
    /**
    *类型名
    *
    */
    
    private String typeName;
    
    /**
    *用户名
    *
    */
    
    private String userName;
    public TSdxThuPo copyTSdxThuPo() {
        return null;
    }
}
