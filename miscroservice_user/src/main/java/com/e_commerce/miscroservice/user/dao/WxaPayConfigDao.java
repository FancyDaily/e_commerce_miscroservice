package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 9:38
 * @Copyright 玖远网络
 */
public interface WxaPayConfigDao{


    /**
     * 根据wxaAppId查询
     *
     * @param wxaAppId wxaAppId
     * @return com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig
     * @author Charlie
     * @date 2018/10/17 9:40
     */
    WxaPayConfig findByAppId(String wxaAppId);
}
