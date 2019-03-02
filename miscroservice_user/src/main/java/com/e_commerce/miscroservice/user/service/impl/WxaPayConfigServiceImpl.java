package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.user.dao.WxaPayConfigDao;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import com.e_commerce.miscroservice.user.service.store.WxaPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 9:30
 * @Copyright 玖远网络
 */
@Service
public class WxaPayConfigServiceImpl implements WxaPayConfigService{

    private Log logger = Log.getInstance (WxaPayConfigServiceImpl.class);

    @Autowired
    private StoreBusinessService storeBusinessService;
    @Autowired
    private WxaPayConfigDao wxaPayConfigDao;



    /**
     * 根据storeId获取支付配置
     *
     * @param storeId 门店id
     * @return com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig
     * @author Charlie
     * @date 2018/10/17 9:31
     */
    @Override
    public WxaPayConfig findByStoreId(Long storeId) {
        logger.info ("根据storeId获取支付配置 storeId={}", storeId);
        if (BeanKit.hasNull (storeId)) {
            return null;
        }


        StoreBusiness storeBusiness = storeBusinessService.selectById (storeId);
        if (storeBusiness == null) {
            return null;
        }
        return wxaPayConfigDao.findByAppId (storeBusiness.getWxaAppId ());
    }
}
