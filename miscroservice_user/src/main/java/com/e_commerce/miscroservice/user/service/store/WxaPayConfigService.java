package com.e_commerce.miscroservice.user.service.store;

import com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 9:29
 * @Copyright 玖远网络
 */
public interface WxaPayConfigService{


    /**
     * 根据storeId获取支付配置
     *
     * @param storeId 门店id
     * @return com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig
     * @author Charlie
     * @date 2018/10/17 9:31
     */
    WxaPayConfig findByStoreId(Long storeId);
}
