package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 订单
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookOrderVo {
    
    /**
    *订单的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *订单类型(捐书、购书)
    *
    */
    
    private Integer type;
    
    /**
    *总价
    *
    */
    
    private Double price;
    
    /**
    *状态
    *
    */
    
    private Integer status;
    
    /**
    *多个书本编号
    *
    */
    
    private String bookIds;
    
    /**
    *配送类型(邮寄、自送）
    *
    */
    
    private Integer shipType;
    
    /**
    *书本总价
    *
    */
    
    private Double bookPrice;
    
    /**
    *运费
    *
    */
    
    private Double shipPirce;
    
    /**
    *订单总价
    *
    */
    
    private Double totalPrice;
    
    /**
    *书本信息编号
    *
    */
    
    private Long bookInfoIds;
    
    /**
    *书籍驿站编号
    *
    */
    
    private Long bookStationId;
    
    /**
    *积分抵扣总额
    *
    */
    
    private Integer scoreDiscount;
    
    /**
    *实际获得积分
    *
    */
    
    private Integer exactTotalScores;
    
    /**
    *邮寄地址编号
    *
    */
    
    private Long shippingAddressId;
    
    /**
    *预计获得总积分
    *
    */
    
    private Integer expectedTotalScores;
    public TSdxBookOrderPo copyTSdxBookOrderPo() {
        return null;
    }
}
