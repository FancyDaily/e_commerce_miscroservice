package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍漂流记录
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookTransRecordVo {
    
    /**
    *书籍漂流记录的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *类型1.xxx成为主人2.xxx的读后感3.存放在书籍驿站多少天etc.
    *
    */
    
    private Integer type;
    
    /**
    *书籍编号
    *
    */
    
    private Long bookId;
    
    /**
    *书籍主人编号
    *
    */
    
    private Long userId;
    
    /**
    *书籍信息编号
    *
    */
    
    private Long bookInfoId;
    
    /**
    *描述
    *
    */
    
    private String description;
    public TSdxBookTransRecordPo copyTSdxBookTransRecordPo() {
        return null;
    }
}
