package com.e_commerce.miscroservice.user.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaVo;
import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.user.entity.StoreWxa;
import com.e_commerce.miscroservice.user.service.store.StoreWxaService;
import com.e_commerce.miscroservice.user.service.store.WxaPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:56
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "user/rpc/wxaPayConfig" )
public class WxaPayConfigRpcController{


    @Autowired
    private StoreWxaService storeWxaService;


    /**
     * 查询用户微信信息
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.user.entity.StoreWxa
     * @author Charlie
     * @date 2018/12/6 15:43
     */
    @RequestMapping("findByStoreId")
    public StoreWxaVo findByStoreId(@RequestParam("storeId") Long storeId){
        StoreWxaVo result = new StoreWxaVo ();
        StoreWxa entity = storeWxaService.findByStoreId (storeId);
        if (entity != null) {
            result.setMchId (entity.getMchId());
            result.setPayKey (entity.getPayKey());
            result.setAppId (entity.getAppId());
            result.setStoreId (storeId);
            result.setAlias (entity.getAlias());
            result.setAuthorizerInfoJson (entity.getAuthorizerInfoJson());
            result.setNickName (entity.getNickName());
            result.setUserName (entity.getUserName());
            result.setSignature (entity.getSignature());

            DebugUtils.todo ("临时方案");
            result.setCerPath ("/tmp/cert/yjj/apiclient_cert.p12");
        }
        return result;
    }
}
