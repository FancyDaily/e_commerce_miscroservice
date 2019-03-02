package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.user.entity.StoreWxa;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/6 15:41
 * @Copyright 玖远网络
 */
public interface StoreWxaDao{


    StoreWxa findSelfShopByStoreId(Long storeId);
}
