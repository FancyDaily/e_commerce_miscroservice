package com.e_commerce.miscroservice.commons.entity.application.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 代理商申请审核表
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 15:42
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_proxy_customer_audit")
public class ProxyCustomerAudit extends BaseEntity implements Serializable{

    @Id
    private Long id;

    @Column(value = "id_card_no", commit = "身份证号", length = 20, isNUll = false)
    private String idCardNo;

    @Column(value = "referee_user_id", commit = "推荐人公众号用户id", length = 20, isNUll = false)
    private Long refereeUserId;

    @Column(value = "recommon_user_id", commit = "被推荐人公众号用户id",length = 20, defaultVal = "0", isNUll = false)
    private Long recommonUserId;

    @Column(value = "name", commit = "姓名", length = 50, isNUll = false)
    private String name;

    @Column(value = "phone", commit = "手机号", length = 20, isNUll = false)
    private String phone;

    @Column(value = "type", commit = "1 市代理 2 县代理", length = 4, isNUll = false)
    private Integer type;

    @Column(value = "audit_status", commit = "审核状态 0 成功 1 处理中 2失败", length = 4, defaultVal = "1", isNUll = false)
    private Integer auditStatus;

    @Column(value = "audit_msg", commit = "审核意见", length = 500)
    private String auditMsg;

    @Column(value = "del_status", commit = "状态 0:正常,1:删除", length = 4, defaultVal = "0", isNUll = false)
    private Integer delStatus;


    @Column(value = "province", commit = "省份", isNUll = false, length = 10)
    private String province;

    @Column(value = "city", commit = "市", length = 20)
    private String city;

    @Column(value = "county", commit = "县", length = 30)
    private String county;

    @Column(value = "address_detail", commit = "详细地址", isNUll = false, length = 500)
    private String addressDetail;

    @Column(value = "update_user_id",commit = "操作人id",length = 20, defaultVal = "0", isNUll = false)
    private Long updateUserId;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

}
