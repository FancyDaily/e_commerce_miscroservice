package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 用户评论
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxCommonsVo {
    
    /**
    *用户评论的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *用户ID
    *
    */
    
    private Long userId;
    
    /**
    *被评论的好友Id
    *
    */
    
    private Long friendId;
    
    /**
    *动态id
    *
    */
    
    private Integer trendsId;
    
    /**
    *评论内容
    *
    */
    
    private String contentInfo;
    public TSdxCommonsPo copyTSdxCommonsPo() {
        return null;
    }
}
