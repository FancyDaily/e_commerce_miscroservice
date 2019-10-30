package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookStationPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍回收驿站
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookStationVo {
    
    /**
    *书籍回收驿站的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *驿站名字
    *
    */
    
    private String name;
    
    /**
    *地址
    *
    */
    
    private String address;
    
    /**
    *纬度
    *
    */
    
    private Double latitude;
    
    /**
    *经度
    *
    */
    
    private Double longitude;
    public TSdxBookStationPo copyTSdxBookStationPo() {
        return null;
    }
}
