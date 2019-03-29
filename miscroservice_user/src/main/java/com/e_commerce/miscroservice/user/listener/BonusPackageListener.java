package com.e_commerce.miscroservice.user.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.entity.application.TBonusPackage;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.user.service.UserService;
import jodd.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BonusPackageListener extends MqListenerConvert {

    @Autowired
    private UserService userService;

    @Override
    protected void transferTo(String transferData) {
        System.out.println("接收到红包消息");
        JSONObject jsonObject = JSONObject.parseObject(transferData);
        TBonusPackage bonusPackage = jsonObject.getJSONObject("bonusPackage").toJavaObject(TBonusPackage.class);
        if(System.currentTimeMillis() - bonusPackage.getCreateTime() < DateUtil.interval) {
            return;
        }
        TUser user = new TUser();
        user.setId(bonusPackage.getCreateUser());
        user.setName(bonusPackage.getCreateUserName());
        userService.sendBackBonusPackage(user,bonusPackage.getId());
    }
}
