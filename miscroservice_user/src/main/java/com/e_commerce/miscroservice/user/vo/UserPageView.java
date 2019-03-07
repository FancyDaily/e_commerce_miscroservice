package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class UserPageView {

   DesensitizedUserView desensitizedUserView;    //基本信息

    List<TOrder> services;  //提供的服务列表

    List<TOrder> helps; //提供的求助列表

    UserSkillListView skills;    //技能列表

}
