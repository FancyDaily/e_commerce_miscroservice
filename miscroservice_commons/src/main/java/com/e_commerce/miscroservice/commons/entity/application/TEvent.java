package com.e_commerce.miscroservice.commons.entity.application;

import lombok.Data;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;

import java.io.Serializable;

@Data
public class TEvent implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Integer templateId;

    private String tiggerId;

    private String parameter;

    private Integer priority;

    private String text;

    private Long createTime;

    private Long createUser;

    private String createUserName;

    private String isValid;

}