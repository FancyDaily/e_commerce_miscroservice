package com.e_commerce.miscroservice.commons.entity.application.distributionSystem;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/12 15:19
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_distribution_proposer")
public class DistributionProposer {
    @Id
    private Long id;

    @Column(value = "real_name",commit = "真实姓名",length = 256)
    private String realName;

    @Column(value = "wx_num",commit = "微信号",length = 256)
    private String wxNum;

    @Column(value = "phone",commit = "手机号",length = 256)
    private String phone;

    @Column(value = "id_card",commit = "身份证号",length = 256)
    private String idCard;

    @Column(value = "remark",commit = "备注",length = 1024)
    private String remark;

    @Column(value = "status",commit = "是否通过 默认 0:否  1:通过",length = 4)
    private Integer status;

    @Column(value = "audit_time",commit = "最近审核时间",length = 20)
    private Long auditTime;

    @Column(value = "audit_reason",commit = "审核理由",length = 1024)
    private String auditReason;


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
