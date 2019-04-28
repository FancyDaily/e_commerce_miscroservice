package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;

public class UserPageView {

    DesensitizedUserView desensitizedUserView;    //基本信息

    QueryResult services;  //提供的服务列表

    QueryResult helps; //提供的求助列表

    UserSkillListView skills;    //技能列表

    boolean isCompanyAccount;   //是否为组织账号

    String companyType; //组织性质

    public void setDesensitizedUserView(DesensitizedUserView desensitizedUserView) {
        this.desensitizedUserView = desensitizedUserView;
    }

    public void setServices(QueryResult services) {
        this.services = services;
    }

    public void setHelps(QueryResult helps) {
        this.helps = helps;
    }

    public void setSkills(UserSkillListView skills) {
        this.skills = skills;
    }

    public void setCompanyAccount(boolean companyAccount) {
        isCompanyAccount = companyAccount;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public DesensitizedUserView getDesensitizedUserView() {
        return desensitizedUserView;
    }

    public QueryResult getServices() {
        return services;
    }

    public QueryResult getHelps() {
        return helps;
    }

    public UserSkillListView getSkills() {
        return skills;
    }

    public boolean isCompanyAccount() {
        return isCompanyAccount;
    }

    public String getCompanyType() {
        return companyType;
    }
}
