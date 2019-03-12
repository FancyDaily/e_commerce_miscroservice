package com.e_commerce.miscroservice.commons.entity.application;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;

import java.io.Serializable;

public class TOrderRecord implements Serializable {
    @Id
    private Long id;

    private Long orderId;

    private String content;

    private Long creatUserId;

    private String creatUserName;

    private Long creatTime;

    private Long updateUserId;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getCreatUserId() {
        return creatUserId;
    }

    public void setCreatUserId(Long creatUserId) {
        this.creatUserId = creatUserId;
    }

    public String getCreatUserName() {
        return creatUserName;
    }

    public void setCreatUserName(String creatUserName) {
        this.creatUserName = creatUserName == null ? null : creatUserName.trim();
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
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