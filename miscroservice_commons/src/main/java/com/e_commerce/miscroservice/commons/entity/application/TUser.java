package com.e_commerce.miscroservice.commons.entity.application;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Data;

import java.io.Serializable;

@Data
public class TUser implements Serializable {
    @Id
    private Long id;

    @Transient
    private String deviceId;

    @Transient
    private String token;

    private String password;

    private String userAccount;

    private String name;

    private String userTel;

    private Integer jurisdiction;

    private String userHeadPortraitPath;

    private String userPicturePath;

    private String vxOpenId;

    private String vxId;

    private String occupation;

    private String workPlace;

    private String college;

    private Integer age;

    private Long birthday;

    private Integer sex;

    private String maxEducation;

    private Integer followNum;

    private Integer receiptNum;

    private String remarks;

    private Integer level;

    private Long growthValue;

    private Integer seekHelpNum;

    private Integer serveNum;

    private Integer seekHelpPublishNum;

    private Integer servePublishNum;

    private Integer payNum;

    private Long surplusTime;

    private Long freezeTime;

    private Long creditLimit;

    private Long publicWelfareTime;

    private Integer authenticationStatus;

    private Integer authenticationType;

    private Integer servTotalEvaluate;

    private Integer servCreditEvaluate;

    private Integer servMajorEvaluate;

    private Integer servAttitudeEvaluate;

    private Integer helpTotalEvaluate;

    private Integer helpCreditEvaluate;

    private Integer helpMajorEvaluate;

    private Integer helpAttitudeEvaluate;

    private String companyIds;

    private String companyNames;

    private String skill;

    private Integer integrity;

    private Integer accreditStatus;

    private Integer masterStatus;

    private Integer authStatus;

    private String inviteCode;

    private String avaliableStatus;

    private Integer isCompanyAccount;

    private String userType;

    private Integer isFake;

    private String extend;

    private Long createUser;

    private String createUserName;

    private Long createTime;

    private Long updateUser;

    private String updateUserName;

    private Long updateTime;

    private String isValid;

    private Integer praise;

    private static final long serialVersionUID = 1L;

}