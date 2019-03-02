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
 * 代理关系
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 19:42
 * @Copyright 玖远网络
 */
@Data
@Table ("yjj_proxy_referee")
public class ProxyReferee extends BasePage implements Serializable{
    @Id
    private Long id;

    /**
     * 推荐人公众号用户id
     */
    @Column(value = "referee_user_id", commit = "推荐人公众号用户id", defaultVal = "0", length = 20)
    private Long refereeUserId;

    @Column(value = "recommon_user_type", commit = "推荐人类型1:客户,2:县级代理商", length = 4)
    private Integer recommonUserType;

    /**
     * 被推荐人公众号用户id
     */
    @Column(value = "recommon_user_id", commit = "被推荐人公众号用户id ", defaultVal = "0", length = 20)
    private Long recommonUserId;

    @Column(value = "refere_parent_id", commit = "推荐人上级的公众号用户id", defaultVal = "0", length = 20)
    private Long refereParentId;

    @Column(value = "status", commit = "绑定状态 0:正常,1:解绑定:", defaultVal = "0", length = 4)
    private Integer status;

    @Column(value = "del_status", commit = "删除状态 0:正常,1:解绑定:", defaultVal = "0", length = 4)
    private Integer delStatus;

    @Column(value = "update_user_id", commit = "操作人员id", defaultVal = "0", length = 20)
    private Long updateUserId;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

}
