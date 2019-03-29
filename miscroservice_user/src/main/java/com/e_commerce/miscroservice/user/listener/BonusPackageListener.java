package com.e_commerce.miscroservice.user.listener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.entity.application.TBonusPackage;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class BonusPackageListener extends MqListenerConvert {

    @Autowired
    private UserService userService;

    @Override
    protected void transferTo(String transferData) {
        System.out.println("接收到红包消息");
        JSONObject jsonMap = JSONObject.parseObject(transferData);
        TBonusPackage bonusPackage = (TBonusPackage) jsonMap.get("bonusPackage");
        if(System.currentTimeMillis() - bonusPackage.getCreateTime() < DateUtil.interval) {
            return;
        }
        TUser user = new TUser();
        user.setId(bonusPackage.getCreateUser());
        user.setName(bonusPackage.getCreateUserName());
        userService.sendBackBonusPackage(user,bonusPackage.getId());
    }
}
