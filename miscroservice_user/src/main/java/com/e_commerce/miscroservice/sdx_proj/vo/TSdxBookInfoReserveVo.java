package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoReservePo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍预定信息
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookInfoReserveVo {
    
    /**
    *书籍预定信息的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *预定用户编号
    *
    */
    
    private Long userId;
    
    /**
    *书籍信息编号
    *
    */
    
    private Long bookInfoId;
    
    /**
    *书券编号
    *
    */
    
    private Long bookTicketId;
    public TSdxBookInfoReservePo copyTSdxBookInfoReservePo() {
        return null;
    }
}
