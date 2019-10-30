package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxInviterPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 邀请人信息
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxInviterVo {
    
    /**
    *邀请人信息的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *被邀请人
    *
    */
    
    private Long userId;
    
    /**
    *邀请者
    *
    */
    
    private Long inviterId;
    
    /**
    *书籍信息编号
    *
    */
    
    private Long bookInfoId;
    public TSdxInviterPo copyTSdxInviterPo() {
        return null;
    }
}
