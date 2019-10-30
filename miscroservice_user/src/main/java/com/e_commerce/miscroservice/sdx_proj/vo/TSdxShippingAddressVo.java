package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 配送地址
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxShippingAddressVo {
    
    /**
    *配送地址的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *市
    *
    */
    
    private String city;
    
    /**
    *姓名
    *
    */
    
    private String name;
    
    /**
    *区/县
    *
    */
    
    private String county;
    
    /**
    *街道
    *
    */
    
    private String street;
    
    /**
    *用户编号
    *
    */
    
    private Long userId;
    
    /**
    *手机号
    *
    */
    
    private String userTel;
    
    /**
    *省
    *
    */
    
    private String province;
    
    /**
    *详细地址
    *
    */
    
    private String detailAddress;
    public TSdxShippingAddressPo copyTSdxShippingAddressPo() {
        return null;
    }
}
