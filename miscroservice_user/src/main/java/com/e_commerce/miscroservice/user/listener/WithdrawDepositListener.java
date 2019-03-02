//package com.e_commerce.miscroservice.user.listener;
//
//import com.alibaba.fastjson.JSONObject;
//import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
//import com.e_commerce.miscroservice.commons.helper.log.Log;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.stereotype.Component;
//
//@Component
//@RefreshScope
//public class WithdrawDepositListener extends MqListenerConvert {
//    private static Log logger = Log.getInstance(WithdrawDepositListener.class);
//
//    @Override
//    protected void transferTo(String transferData) {
//
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(transferData);
//            System.out.println(jsonObject);
//        } catch (Exception e) {
//            logger.error("【提现回调处理】发送异常，系统异常:{}", e);
//            e.printStackTrace();
//        }
//    }
//
//
//}
