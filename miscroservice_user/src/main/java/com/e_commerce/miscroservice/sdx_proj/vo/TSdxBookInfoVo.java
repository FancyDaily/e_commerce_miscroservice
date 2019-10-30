package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍信息
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookInfoVo {
    
    /**
    *书籍信息的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *书名
    *
    */
    
    private String name;
    
    /**
    *出版社
    *
    */
    
    private String press;
    
    /**
    *价格
    *
    */
    
    private Double price;
    
    /**
    *作者
    *
    */
    
    private String author;
    
    /**
    *目录
    *
    */
    
    private String catalog;
    
    /**
    *销量
    *
    */
    
    private Integer soldNum;
    
    /**
    *图片
    *
    */
    
    private String introPic;
    
    /**
    *类型编号
    *
    */
    
    private Integer categoryId;
    
    /**
    *豆瓣评分
    *
    */
    
    private Double scoreDouban;
    
    /**
    *装帧风格
    *
    */
    
    private String bindingStyle;
    
    /**
    *类型名
    *
    */
    
    private String categoryName;
    
    /**
    *简介
    *
    */
    
    private String introduction;
    
    /**
    *最大预购接收数
    *
    */
    
    private Integer maximumReserve;
    
    /**
    *最高可抵扣价格
    *
    */
    
    private Double maximumDiscount;
    public TSdxBookInfoPo copyTSdxBookInfoPo() {
        return null;
    }
}
