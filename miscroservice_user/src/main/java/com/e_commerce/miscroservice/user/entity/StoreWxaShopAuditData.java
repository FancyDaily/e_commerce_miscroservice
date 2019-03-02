package com.e_commerce.miscroservice.user.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/18 19:25
 * @Copyright 玖远网络
 */
@Table("yjj_store_wxa_shop_audit_data")
@Data
public class StoreWxaShopAuditData{

    @Id
    private Long id;
    /**
     * 门店id
     */
    @Column( value = "store_id", length = 20 )
    private Long storeId;
    /**
     * 店铺名称(小程序中端店铺名称)
     */
    @Column(value = "shop_name", commit = "店铺名称", length = 50)
    private String shopName;
    /**
     * 共享小程序分享二维码图片的URL
     */
    @Column( value = "share_qr_code_url" , commit = "共享小程序分享二维码图片的URL", length = 500)
    private String shareQrCodeUrl;
    /**
     * 店主名称
     */
    @Column(value = "boss_name", commit = "店主名称", length = 50)
    private String bossName;
    /**
     * 行业
     */
    @Column(value = "industry", commit = "行业", length = 50)
    private String industry;
    /**
     * 主营业务
     */
    @Column(value = "main_business", commit = "主营业务", length = 500)
    private String mainBusiness;
    /**
     * 地址
     */
    @Column( value = "address", commit = "地址", length = 500 )
    private String address;

    /**
     * 是否删除 0正常,1删除,4草稿
     */
    @Column(value = "del_status",length = 4,defaultVal = "0",commit = "是否删除 0正常,1删除,4草稿")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;
}
