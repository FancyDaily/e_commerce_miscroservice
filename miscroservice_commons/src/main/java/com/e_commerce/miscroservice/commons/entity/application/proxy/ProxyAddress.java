package com.e_commerce.miscroservice.commons.entity.application.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 描述 代理地址表
 *
 * @author hyq
 * @date 2018/9/18 13:52
 */
@Data
@Table("yjj_proxy_address")
@javax.persistence.Table(name = "yjj_proxy_address")
public class ProxyAddress implements Serializable {

    @javax.persistence.Id
    @Id
    private Long id;

    @Column(value = "user_id", commit = "用户ID", length = 20)
    private Long userId;

    @Column(value = "is_default", commit = "是否是默认 1 是  0 否", defaultVal = "0", length = 2)
    private Integer isDefault;

    @Column(value = "province", commit = "省份", length = 20)
    private String province;

    @Column(value = "city", commit = "市", length = 20)
    private String city;

    @Column(value = "county", commit = "县", length = 20)
    private String county;

    @Column(value = "receiver_name", commit = "收件人姓名", length = 20)
    private String receiverName;

    @Column(value = "receiver_phone", commit = "收件人手机号", length = 11)
    private String receiverPhone;

    @Column(value = "receiver_address", commit = "收件人手地址", length = 500)
    private String receiverAddress;

    @Column(value = "del_status",commit = "删除状态 0 未删除 1 删除",defaultVal ="0" ,length = 2)
    private Integer delStatus;

    @Column(value = "create_time", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "修改时间")
    private Timestamp updateTime;


}
