package com.e_commerce.miscroservice.xiaoshi_proj.order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.xiaoshi_proj.order.vo.BaseUserView;
import lombok.Data;


import java.io.Serializable;

@Data
public class TUsers implements Serializable {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public Integer getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(Integer jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getUserHeadPortraitPath() {
        return userHeadPortraitPath;
    }

    public void setUserHeadPortraitPath(String userHeadPortraitPath) {
        this.userHeadPortraitPath = userHeadPortraitPath;
    }

    public String getUserPicturePath() {
        return userPicturePath;
    }

    public void setUserPicturePath(String userPicturePath) {
        this.userPicturePath = userPicturePath;
    }

    public String getVxOpenId() {
        return vxOpenId;
    }

    public void setVxOpenId(String vxOpenId) {
        this.vxOpenId = vxOpenId;
    }

    public String getVxId() {
        return vxId;
    }

    public void setVxId(String vxId) {
        this.vxId = vxId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMaxEducation() {
        return maxEducation;
    }

    public void setMaxEducation(String maxEducation) {
        this.maxEducation = maxEducation;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(Integer receiptNum) {
        this.receiptNum = receiptNum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(Long growthValue) {
        this.growthValue = growthValue;
    }

    public Integer getSeekHelpNum() {
        return seekHelpNum;
    }

    public void setSeekHelpNum(Integer seekHelpNum) {
        this.seekHelpNum = seekHelpNum;
    }

    public Integer getServeNum() {
        return serveNum;
    }

    public void setServeNum(Integer serveNum) {
        this.serveNum = serveNum;
    }

    public Integer getSeekHelpPublishNum() {
        return seekHelpPublishNum;
    }

    public void setSeekHelpPublishNum(Integer seekHelpPublishNum) {
        this.seekHelpPublishNum = seekHelpPublishNum;
    }

    public Integer getServePublishNum() {
        return servePublishNum;
    }

    public void setServePublishNum(Integer servePublishNum) {
        this.servePublishNum = servePublishNum;
    }

    public Integer getPayNum() {
        return payNum;
    }

    public void setPayNum(Integer payNum) {
        this.payNum = payNum;
    }

    public Long getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(Long surplusTime) {
        this.surplusTime = surplusTime;
    }

    public Long getFreezeTime() {
        return freezeTime;
    }

    public void setFreezeTime(Long freezeTime) {
        this.freezeTime = freezeTime;
    }

    public Long getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Long creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Long getPublicWelfareTime() {
        return publicWelfareTime;
    }

    public void setPublicWelfareTime(Long publicWelfareTime) {
        this.publicWelfareTime = publicWelfareTime;
    }

    public Integer getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(Integer authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public Integer getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(Integer authenticationType) {
        this.authenticationType = authenticationType;
    }

    public Integer getServTotalEvaluate() {
        return servTotalEvaluate;
    }

    public void setServTotalEvaluate(Integer servTotalEvaluate) {
        this.servTotalEvaluate = servTotalEvaluate;
    }

    public Integer getServCreditEvaluate() {
        return servCreditEvaluate;
    }

    public void setServCreditEvaluate(Integer servCreditEvaluate) {
        this.servCreditEvaluate = servCreditEvaluate;
    }

    public Integer getServMajorEvaluate() {
        return servMajorEvaluate;
    }

    public void setServMajorEvaluate(Integer servMajorEvaluate) {
        this.servMajorEvaluate = servMajorEvaluate;
    }

    public Integer getServAttitudeEvaluate() {
        return servAttitudeEvaluate;
    }

    public void setServAttitudeEvaluate(Integer servAttitudeEvaluate) {
        this.servAttitudeEvaluate = servAttitudeEvaluate;
    }

    public Integer getHelpTotalEvaluate() {
        return helpTotalEvaluate;
    }

    public void setHelpTotalEvaluate(Integer helpTotalEvaluate) {
        this.helpTotalEvaluate = helpTotalEvaluate;
    }

    public Integer getHelpCreditEvaluate() {
        return helpCreditEvaluate;
    }

    public void setHelpCreditEvaluate(Integer helpCreditEvaluate) {
        this.helpCreditEvaluate = helpCreditEvaluate;
    }

    public Integer getHelpMajorEvaluate() {
        return helpMajorEvaluate;
    }

    public void setHelpMajorEvaluate(Integer helpMajorEvaluate) {
        this.helpMajorEvaluate = helpMajorEvaluate;
    }

    public Integer getHelpAttitudeEvaluate() {
        return helpAttitudeEvaluate;
    }

    public void setHelpAttitudeEvaluate(Integer helpAttitudeEvaluate) {
        this.helpAttitudeEvaluate = helpAttitudeEvaluate;
    }

    public String getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(String companyIds) {
        this.companyIds = companyIds;
    }

    public String getCompanyNames() {
        return companyNames;
    }

    public void setCompanyNames(String companyNames) {
        this.companyNames = companyNames;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Integer getIntegrity() {
        return integrity;
    }

    public void setIntegrity(Integer integrity) {
        this.integrity = integrity;
    }

    public Integer getAccreditStatus() {
        return accreditStatus;
    }

    public void setAccreditStatus(Integer accreditStatus) {
        this.accreditStatus = accreditStatus;
    }

    public Integer getMasterStatus() {
        return masterStatus;
    }

    public void setMasterStatus(Integer masterStatus) {
        this.masterStatus = masterStatus;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getAvaliableStatus() {
        return avaliableStatus;
    }

    public void setAvaliableStatus(String avaliableStatus) {
        this.avaliableStatus = avaliableStatus;
    }

    public Integer getIsCompanyAccount() {
        return isCompanyAccount;
    }

    public void setIsCompanyAccount(Integer isCompanyAccount) {
        this.isCompanyAccount = isCompanyAccount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getIsFake() {
        return isFake;
    }

    public void setIsFake(Integer isFake) {
        this.isFake = isFake;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Integer getPraise() {
        return praise;
    }

    public void setPraise(Integer praise) {
        this.praise = praise;
    }

    public BaseUserView copyBaseUserView(){
        return null;
    }
    private static final long serialVersionUID = 1L;

    public TUsers exchangeTUser(com.e_commerce.miscroservice.commons.entity.application.TUser tUser){
        return BeanUtil.copy(tUser, TUsers.class);
    }


}