package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;

import java.io.Serializable;

@Table
public class TGzUserLesson implements Serializable {
    @Id
    private Long id;

    private Long userId;

    private Long subjectId;

    private Long lessonId;

    private String sign;

    @Column(commit = "我的章节状态(可用、不可用)")
    private Integer status;

    private Integer videoCompletion;

    @Column(defaultVal = "0",commit = "章节完成状态")
    private Integer lessonCompletionStatus;

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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getSign() {
        return sign;
    }

    public Integer getStatus() {
        return status;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVideoCompletion() {
        return videoCompletion;
    }

    public void setVideoCompletion(Integer videoCompletion) {
        this.videoCompletion = videoCompletion;
    }

    public void setLessonCompletionStatus(Integer lessonCompletionStatus) {
        this.lessonCompletionStatus = lessonCompletionStatus;
    }

    public Integer getLessonCompletionStatus() {
        return lessonCompletionStatus;
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