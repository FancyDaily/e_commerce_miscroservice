package com.e_commerce.miscroservice.commons.entity.application;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;

import java.io.Serializable;

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

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTiggerId() {
        return tiggerId;
    }

    public void setTiggerId(String tiggerId) {
        this.tiggerId = tiggerId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}