package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.user.service.apiImpl.SendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-21 16:00
 */
@Component
public class SmsUtil {

    @Autowired
    private SendSmsService smsService;

    public String sendSms(String telephone, String msg, Integer application) {
        Map<String, Object> params = new HashMap<>();
        String content = "【壹晓时】%s";
        if(application!=null && application.equals(2)) {
            content = "【观照律师训练营】%s";
        }
        content = String.format(content, msg);
        params.put("mobile", telephone);
        params.put("content", content);
        return smsService.sendServMsg(params);
    }

    public String sendSms(String telephone, String msg) {
        return sendSms(telephone, msg, ApplicationEnum.XIAOSHI_APPLICATION.toCode());
    }
}
