package com.e_commerce.miscroservice.user.service.store;

import com.e_commerce.miscroservice.user.entity.StoreWxa;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/6 15:38
 * @Copyright 玖远网络
 */
public interface StoreWxaService{


    /**
     * 查询
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.user.entity.StoreWxa
     * @author Charlie
     * @date 2018/12/6 15:43
     */
    StoreWxa findByStoreId(Long storeId);

}
