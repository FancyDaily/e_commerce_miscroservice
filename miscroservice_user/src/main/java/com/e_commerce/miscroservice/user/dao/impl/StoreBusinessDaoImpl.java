package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.user.dao.StoreBusinessDao;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.mapper.StoreBusinessMapper;
import org.apache.catalina.StoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:46
 * @Copyright 玖远网络
 */
@Component
public class StoreBusinessDaoImpl implements StoreBusinessDao{

    @Autowired
    private StoreBusinessMapper storeBusinessMapper;

    /**
     * 根据店中店 openId查找门店
     *
     * @param inShopOpenId inShopOpenId
     * @return com.e_commerce.miscroservice.user.entity.StoreBusiness
     * @author Charlie
     * @date 2018/12/10 18:05
     */
    @Override
    public StoreBusiness findByInShopOpenId(String inShopOpenId) {
        StoreBusinessVo query = new StoreBusinessVo ();
        query.setInShopOpenId (inShopOpenId);
        return storeBusinessMapper.selectOne (query);
    }

    @Override
    public StoreBusiness findById(Long storeId) {
        StoreBusinessVo query = new StoreBusinessVo ();
        query.setId (storeId);
        return storeBusinessMapper.selectOne (query);
    }

    @Override
    public StoreBusiness findByInMemberId(Long inShopMemberId) {
        if (inShopMemberId == null) {
            return null;
        }
        StoreBusinessVo query = new StoreBusinessVo ();
        query.setInShopMemberId (inShopMemberId);
        return storeBusinessMapper.selectOne (query);
    }


}
