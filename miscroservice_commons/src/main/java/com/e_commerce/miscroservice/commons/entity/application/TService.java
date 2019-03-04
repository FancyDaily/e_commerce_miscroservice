package com.e_commerce.miscroservice.commons.entity.application;

import java.io.Serializable;

public class TService implements Serializable {
    private Long id;

    private Long userId;

    private Integer type;

    private Integer status;

    private Integer source;

    private Long serviceTypeId;

    private Integer serveNum;

    private Integer totalEvaluate;

    private String serviceName;

    private Integer servicePlace;

    private String labels;

    private Integer servicePersonnel;

    private Long startTime;

    private Long endTime;

    private Integer timeType;

    private String dateWeek;

    private String startDateS;

    private String endDateS;

    private String startTimeS;

    private String endTimeS;

    private String addressName;

    private Double longitude;

    private Double latitude;

    private Double radius;

    private Integer collectType;

    private Long collectTime;

    private Long companyId;

    private Integer openAuth;

    private String extend;

    private Long createUser;

    private String createUserName;

    private Long createTime;

    private Long updateUser;

    private String updateUserName;

    private Long updateTime;

    private String isValid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getServeNum() {
        return serveNum;
    }

    public void setServeNum(Integer serveNum) {
        this.serveNum = serveNum;
    }

    public Integer getTotalEvaluate() {
        return totalEvaluate;
    }

    public void setTotalEvaluate(Integer totalEvaluate) {
        this.totalEvaluate = totalEvaluate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public Integer getServicePlace() {
        return servicePlace;
    }

    public void setServicePlace(Integer servicePlace) {
        this.servicePlace = servicePlace;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels == null ? null : labels.trim();
    }

    public Integer getServicePersonnel() {
        return servicePersonnel;
    }

    public void setServicePersonnel(Integer servicePersonnel) {
        this.servicePersonnel = servicePersonnel;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public String getDateWeek() {
        return dateWeek;
    }

    public void setDateWeek(String dateWeek) {
        this.dateWeek = dateWeek == null ? null : dateWeek.trim();
    }

    public String getStartDateS() {
        return startDateS;
    }

    public void setStartDateS(String startDateS) {
        this.startDateS = startDateS == null ? null : startDateS.trim();
    }

    public String getEndDateS() {
        return endDateS;
    }

    public void setEndDateS(String endDateS) {
        this.endDateS = endDateS == null ? null : endDateS.trim();
    }

    public String getStartTimeS() {
        return startTimeS;
    }

    public void setStartTimeS(String startTimeS) {
        this.startTimeS = startTimeS == null ? null : startTimeS.trim();
    }

    public String getEndTimeS() {
        return endTimeS;
    }

    public void setEndTimeS(String endTimeS) {
        this.endTimeS = endTimeS == null ? null : endTimeS.trim();
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName == null ? null : addressName.trim();
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Integer getCollectType() {
        return collectType;
    }

    public void setCollectType(Integer collectType) {
        this.collectType = collectType;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getOpenAuth() {
        return openAuth;
    }

    public void setOpenAuth(Integer openAuth) {
        this.openAuth = openAuth;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend == null ? null : extend.trim();
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
        this.createUserName = createUserName == null ? null : createUserName.trim();
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
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
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
        this.isValid = isValid == null ? null : isValid.trim();
    }
}