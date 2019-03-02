package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.user.dao.StoreWxaDao;
import com.e_commerce.miscroservice.user.entity.StoreWxa;
import com.e_commerce.miscroservice.user.service.store.StoreWxaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/6 15:38
 * @Copyright 玖远网络
 */
@Service
public class StoreWxaServiceImpl implements StoreWxaService{

    @Autowired
    private StoreWxaDao storeWxaDao;


    /**
     * 查询
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.user.entity.StoreWxa
     * @author Charlie
     * @date 2018/12/6 15:43
     */
    @Override
    public StoreWxa findByStoreId(Long storeId) {
        return storeWxaDao.findSelfShopByStoreId (storeId);
    }

}
