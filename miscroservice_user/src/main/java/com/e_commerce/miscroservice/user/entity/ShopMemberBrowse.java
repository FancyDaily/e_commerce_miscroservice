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
 * @date 2018/12/7 13:31
 * @Copyright 玖远网络
 */
@Data
@Table("shop_member_browse")
public class ShopMemberBrowse{

    @Id
    private Long id;

    @Column(value = "target_id", commit = "浏览id", length = 20, defaultVal = "0")
    private Long targetId;

    @Column(value = "in_shop_member_id", commit = "店中店下用户id", length = 20, isNUll = false)
    private Long inShopMemberId;

    @Column(value = "remark", commit = "备注", length = 255)
    private String remark;

    @Column(value = "type",length = 4,isNUll = false,commit = "类型,1浏览店中店")
    private Integer type;

    @Column(value = "del_status",length = 4,defaultVal = "0",commit = "是否删除 0正常,1删除")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;
}
