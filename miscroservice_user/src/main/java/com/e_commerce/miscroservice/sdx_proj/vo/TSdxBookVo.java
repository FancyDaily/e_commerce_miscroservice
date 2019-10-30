package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书袋熊书籍
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookVo {
    
    /**
    *书袋熊书籍的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *状态(捐助审核中(初始)，在书架，失效（被买走、审核失败）
    *
    */
    
    private Integer status;
    
    /**
    *捐助者编号
    *
    */
    
    private Long donaterId;
    
    /**
    *公益项目编号
    *
    */
    
    private Long serviceId;
    
    /**
    *书籍信息编号
    *
    */
    
    private Long bookInfoId;
    
    /**
    *实际售卖价格
    *
    */
    
    private Double exactPrice;
    
    /**
    *实际抵扣积分
    *
    */
    
    private Integer exactScore;
    
    /**
    *预估折抵积分价值
    *
    */
    
    private Integer expectedScore;
    
    /**
    *当前拥有者编号
    *
    */
    
    private Long currentOwnerId;
    public TSdxBookPo copyTSdxBookPo() {
        return null;
    }
}
