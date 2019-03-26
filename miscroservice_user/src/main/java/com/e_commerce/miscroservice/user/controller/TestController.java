package com.e_commerce.miscroservice.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.config.colligate.MqTemplate;
import com.e_commerce.miscroservice.commons.entity.service.TimerScheduler;
import com.e_commerce.miscroservice.commons.enums.colligate.MqChannelEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.TimerSchedulerTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InnerRestController
public class TestController {


    @Autowired
    @Lazy
    private MqTemplate mqTemplate;


    @GetMapping("index")

    public String init() {


        TimerScheduler timerScheduler = new TimerScheduler();
        timerScheduler.setName("111" + UUID.randomUUID().toString());
        timerScheduler.setCron("*/20 * * * * ? *");
        timerScheduler.setType(TimerSchedulerTypeEnum.TEST.toNum());

        Map<String, String> map = new HashMap<>();
        map.put("aaa", "11");
        map.put("bbb", "22");

        timerScheduler.setParams(JSON.toJSONString(map));


        mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
        return "OK";


    }


}
