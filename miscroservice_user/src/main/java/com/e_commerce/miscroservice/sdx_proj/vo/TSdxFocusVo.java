package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxFocusPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 关注书友
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxFocusVo {
    
    /**
    *关注书友的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *关注类型
    *
    */
    
    private Integer type;
    
    /**
    *用户ID
    *
    */
    
    private Long userId;
    
    /**
    *关注类型名称
    *
    */
    
    private String typeName;
    
    /**
    *书友ID
    *
    */
    
    private Long bookFriendId;
    
    /**
    *书友头像
    *
    */
    
    private String bookFriendPic;
    
    /**
    *书友名称
    *
    */
    
    private String bookFriendName;
    public TSdxFocusPo copyTSdxFocusPo() {
        return null;
    }
}
