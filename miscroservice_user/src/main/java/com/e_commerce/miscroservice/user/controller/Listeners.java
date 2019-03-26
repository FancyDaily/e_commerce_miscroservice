package com.e_commerce.miscroservice.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Listeners extends MqListenerConvert {
    @Override
    protected void transferTo(String transferData) {
        //System.out.println("接受消息");
        Map<String,String> map = JSONObject.parseObject(transferData, Map.class);
        //System.out.println(map);
    }
}
