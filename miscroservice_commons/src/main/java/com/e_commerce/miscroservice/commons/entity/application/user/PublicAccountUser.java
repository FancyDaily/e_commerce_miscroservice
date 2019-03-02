package com.e_commerce.miscroservice.commons.entity.application.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/21 18:04
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_public_account_user")
public class PublicAccountUser extends BaseEntity implements Serializable{

    @Id
    private Long id;

    @Column(value = "store_id",commit = "门店用户ID",length = 20)
    private Long storeId;

    @Column(value = "open_id",commit = "用户openId",length = 50)
    private String openId;

    @Column(value = "phone",commit = "电话号码", isNUll = false, length = 20)
    private String phone;

    @Column(value = "del_status",commit = "0:正常,1删除", defaultVal = "0", length = 4)
    private Integer delStatus;

    @Column(value = "wx_user_icon", commit = "微信用户头像",length = 255)
    private String wxUserIcon;

    @Column( value = "wx_name", commit = "微信名称", length = 50 )
    private String wxName;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

//    /**
//     * 最后一次登录时间
//     * <p>当一个微信绑定多个手机号时,按最后一次登录时间拿用户</p>
//     */
//    @Column( value = "last_login_time" , commit = "最后一次登录时间", length = 20, defaultVal = "0")
//    private Long lastLoginTime;

    /**
     * 0:非主体账号,1:主体账号
     * <p>用户可以拥有多个账号,但是登录时候只能用主体账号登录,没有主体则要重新登录,确定主体账号</p>
     */
    @Column(value = "subject_account", commit = "0:非主体账号,1:主体账号", length = 4, defaultVal = "0")
    private Integer subjectAccount;

    //--------------------------------------- 以下数据库非数据库字段 --------------------------------
    @Transient
    private String createTimeReadable;
}
