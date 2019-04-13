package com.e_commerce.miscroservice.commons.entity.application;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import lombok.Data;

import java.io.Serializable;

@Data
public class TUserAuth implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private String cardId;

    private String cardName;

    private Integer sex;

    private Long birthday;

    private String address;

    private String photo;

    private Long createUser;

    private String createUserName;

    private Long createTime;

    private Long updateUser;

    private String updateUserName;

    private Long updateTime;

    private String isValid;

    private static final long serialVersionUID = 1L;

}