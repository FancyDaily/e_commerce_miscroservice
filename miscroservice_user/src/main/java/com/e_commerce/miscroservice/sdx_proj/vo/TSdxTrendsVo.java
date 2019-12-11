package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 发布动态
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxTrendsVo {
    
    /**
    *发布动态的Id,修改或者查询的需要
    *
    */
    
    private Long id;
    
    /**
    *价格
    *
    */
    
    private Double price;
    
    /**
    *用户ID
    *
    */
    
    private Long userId;
    
    /**
    *用户此时地址
    *
    */
    
    private String address;
    
    /**
    *动态图片
    *
    */
    
    private String bookPic;
    
    /**
    *用户头像
    *
    */
    
    private String userPic;
    
    /**
    *书籍名
    *
    */
    
    private String bookName;
    
    /**
    *书籍图片
    *
    */
    
    private String boookPic;
    
    /**
    *好友id
    *
    */
    
    private Long friendId;
    
    /**
    *纬度
    *
    */
    
    private Double latitude;
    
    /**
    *用户名
    *
    */
    
    private String userName;
    
    /**
    *经度
    *
    */
    
    private Double longitude;
    
    /**
    *动态内容
    *
    */
    
    private String trentInfo;
    
    /**
    *书籍信息编号
    *
    */
    
    private Long bookInfoId;
    
    /**
    *好友名称
    *
    */
    
    private String friendName;
    
    /**
    *豆瓣评分
    *
    */
    
    private Double scoreDouban;
    public TSdxTrendsPo copyTSdxTrendsPo() {
        return null;
    }
}
