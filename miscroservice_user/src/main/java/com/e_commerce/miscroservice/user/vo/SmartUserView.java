package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;

/**
 * 功能描述:	人性化的用户实体view
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2019年1月15日 下午4:14:02
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class SmartUserView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String idString; 
	
	private String groupIdString;
	
	private String groupName;
	
	private String corpNickName;
	
	private String name;
	
	private String sexString;
	
	private String ageString;
	
	private String gainTimeString;
	
	private String totalComment;
	
	private Long id;

    private String userAccount;

    private String userTel;

    private Integer jurisdiction;

    private String userHeadPortraitPath;

    private String userPicturePath;

    private String vxId;

    private String occupation;

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

    private Long surplusTime;

    private Long freezeTime;

    private Long publicWelfareTime;

    private Integer authenticationStatus;

    private Integer authenticationType;

    private Integer totalEvaluate;

    private Integer creditEvaluate;

    private Integer majorEvaluate;

    private Integer attitudeEvaluate;

    private String skill;

    private Integer integrity;

    private Integer accreditStatus;

    private Integer masterStatus;

    private Integer authStatus;

    private String inviteCode;

    private String avaliableStatus;

    private Integer isCompanyAccount;

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

	public String getIdString() {
		return idString;
	}

	public void setIdString(String idString) {
		this.idString = idString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSexString() {
		return sexString;
	}

	public void setSexString(String sexString) {
		this.sexString = sexString;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getGainTimeString() {
		return gainTimeString;
	}

	public void setGainTimeString(String gainTimeString) {
		this.gainTimeString = gainTimeString;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
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

	public Integer getTotalEvaluate() {
		return totalEvaluate;
	}

	public void setTotalEvaluate(Integer totalEvaluate) {
		this.totalEvaluate = totalEvaluate;
	}

	public Integer getCreditEvaluate() {
		return creditEvaluate;
	}

	public void setCreditEvaluate(Integer creditEvaluate) {
		this.creditEvaluate = creditEvaluate;
	}

	public Integer getMajorEvaluate() {
		return majorEvaluate;
	}

	public void setMajorEvaluate(Integer majorEvaluate) {
		this.majorEvaluate = majorEvaluate;
	}

	public Integer getAttitudeEvaluate() {
		return attitudeEvaluate;
	}

	public void setAttitudeEvaluate(Integer attitudeEvaluate) {
		this.attitudeEvaluate = attitudeEvaluate;
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

	public String getTotalComment() {
		return totalComment;
	}

	public void setTotalComment(String totalComment) {
		this.totalComment = totalComment;
	}

	public String getCorpNickName() {
		return corpNickName;
	}

	public void setCorpNickName(String corpNickName) {
		this.corpNickName = corpNickName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupIdString() {
		return groupIdString;
	}

	public void setGroupIdString(String groupIdString) {
		this.groupIdString = groupIdString;
	}
}
