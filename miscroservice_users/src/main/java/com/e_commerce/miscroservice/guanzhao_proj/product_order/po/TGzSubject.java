package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;

import java.io.Serializable;

@Table
public class TGzSubject implements Serializable {
    @Id
    private Long id;

    private String name;

    private String subjectHeadPortraitPath;

    private Integer period;

    private String price;

    private String forSalePrice;

    private Integer forSaleStatus;

    private Integer forSaleSurplusNum;

    private Integer seriesIndex;

    private Integer avaliableStatus;

    private Integer availableDate;

    private Integer availableTime;

    private String remarks;

    private String securityKey;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getSubjectHeadPortraitPath() {
        return subjectHeadPortraitPath;
    }

    public void setSubjectHeadPortraitPath(String subjectHeadPortraitPath) {
        this.subjectHeadPortraitPath = subjectHeadPortraitPath == null ? null : subjectHeadPortraitPath.trim();
    }

    public void setSeriesIndex(Integer seriesIndex) {
        this.seriesIndex = seriesIndex;
    }

    public Integer getSeriesIndex() {
        return seriesIndex;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getForSalePrice() {
        return forSalePrice;
    }

    public void setForSalePrice(String forSalePrice) {
        this.forSalePrice = forSalePrice == null ? null : forSalePrice.trim();
    }

    public Integer getForSaleStatus() {
        return forSaleStatus;
    }

    public void setForSaleStatus(Integer forSaleStatus) {
        this.forSaleStatus = forSaleStatus;
    }

    public Integer getForSaleSurplusNum() {
        return forSaleSurplusNum;
    }

    public void setForSaleSurplusNum(Integer forSaleSurplusNum) {
        this.forSaleSurplusNum = forSaleSurplusNum;
    }

    public void setAvaliableStatus(Integer avaliableStatus) {
        this.avaliableStatus = avaliableStatus;
    }

    public Integer getAvaliableStatus() {
        return avaliableStatus;
    }

    public Integer getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Integer availableDate) {
        this.availableDate = availableDate;
    }

    public Integer getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(Integer availableTime) {
        this.availableTime = availableTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey == null ? null : securityKey.trim();
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