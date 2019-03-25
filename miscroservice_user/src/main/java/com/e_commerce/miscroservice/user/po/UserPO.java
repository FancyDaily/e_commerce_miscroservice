package com.e_commerce.miscroservice.user.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户数据实体类
 */
@Data
@Table("t_user_b")
public class UserPO extends BasePage implements Serializable {

    public String password;
    @Id
    private Integer userId;
    @Column(value = "user_djdjdj", commit = "skksk")
    private String username;
    private String phone;
    private String userNamePass;
    @Transient
    private String uid;


}
