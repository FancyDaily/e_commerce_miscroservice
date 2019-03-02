package com.e_commerce.miscroservice.commons.entity.application.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 代理商
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:03
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_proxy_customer")
public class ProxyCustomer extends BasePage implements Serializable{

    @Id
    private Long id;

    @Column(value = "id_card_no", commit = "身份证号", length = 20, isNUll = false)
    private String idCardNo;

    @Column(value = "name", commit = "姓名", length = 50, isNUll = false)
    private String name;

    @Column(value = "phone", commit = "手机号", isNUll = false, length = 20)
    private String phone;

    @Column(value = "type", commit = "1 市代理 2 县代理", length = 4, isNUll = false)
    private Integer type;

    @Column(value = "user_id", commit = "公账号用户表的id", length = 20, isNUll = false)
    private Long userId;

    @Column(value = "province", commit = "省份", length = 10)
    private String province;

    @Column(value = "city", commit = "市", length = 20)
    private String city;

    @Column(value = "county", commit = "县", length = 30)
    private String county;

    @Column(value = "proxy_qr_code", commit = "代理海报二维码", length = 500)
    private String proxyQrCode;

    @Column(value = "customer_qr_code", commit = "客户海报二维码", length = 500)
    private String customerQrCode;

    @Column(value = "address_detail", commit = "详细地址", length = 500)
    private String addressDetail;

    @Column(value = "del_status", commit = "状态 0:正常,1:删除", length = 4, defaultVal = "0", isNUll = false)
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

}
