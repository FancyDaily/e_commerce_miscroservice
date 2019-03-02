package com.e_commerce.miscroservice.commons.entity.application.distributionSystem;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/16 10:09
 * @Copyright 玖远网络
 */
@Data
@Table("shop_audit_management")
public class ShopAuditManagement implements Serializable {
    @Id
    private Long id;

    @Column(value = "user_id",commit = "用户id",length = 20)
    private Long userId;


    @Column(value = "real_name",commit = "真实姓名",length = 256)
    private String realName;

    @Column(value = "wx_num",commit = "微信号",length = 256)
    private String wxNum;

    @Column(value = "phone",commit = "手机号",length = 256)
    private String phone;

    @Column(value = "id_card",commit = "身份证",length = 256)
    private String idCard;

    @Column(value = "application_role",commit = "申请的角色",length = 4)
    private Integer applicationRole;

    @Column(value = "before_role",commit = "以前的的等级",length = 4)
    private Integer beforeRole;

    @Column(value = "audit_explain",commit = "审核说明",length = 1024,defaultVal = "/")
    private String auditExplain;

    @Column(value = "commit_time",commit = "申请时间",length = 256,defaultVal = "/")
    private String commitTime;

    @Column(value = "audit_time",commit = "审核时间",length = 256,defaultVal = "/")
    private String auditTime;

    @Column(value = "status",commit = "审核状态  0:待审核，1：通过 ，2：拒绝",defaultVal = "0",length = 4)
    private Integer status;

    /**
     * 状态 0:正常,1:失效
     */
    @Column(value = "del_status", commit = "状态 0:正常,1:失效", defaultVal = "0", length = 4)
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;
}
