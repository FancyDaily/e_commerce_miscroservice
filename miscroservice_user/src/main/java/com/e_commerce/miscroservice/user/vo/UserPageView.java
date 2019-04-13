package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;

public class UserPageView {

    DesensitizedUserView desensitizedUserView;    //基本信息

    QueryResult<TOrder> services;  //提供的服务列表

    QueryResult<TOrder> helps; //提供的求助列表

    UserSkillListView skills;    //技能列表

    boolean isCompanyAccount;   //是否为组织账号

    String companyType; //组织性质

    public DesensitizedUserView getDesensitizedUserView() {
        return desensitizedUserView;
    }

    public void setDesensitizedUserView(DesensitizedUserView desensitizedUserView) {
        this.desensitizedUserView = desensitizedUserView;
    }

    public QueryResult<TOrder> getServices() {
        return services;
    }

    public void setServices(QueryResult<TOrder> services) {
        this.services = services;
    }

    public QueryResult<TOrder> getHelps() {
        return helps;
    }

    public void setHelps(QueryResult<TOrder> helps) {
        this.helps = helps;
    }

    public UserSkillListView getSkills() {
        return skills;
    }

    public void setSkills(UserSkillListView skills) {
        this.skills = skills;
    }

    public boolean isCompanyAccount() {
        return isCompanyAccount;
    }

    public void setCompanyAccount(boolean companyAccount) {
        isCompanyAccount = companyAccount;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
}
