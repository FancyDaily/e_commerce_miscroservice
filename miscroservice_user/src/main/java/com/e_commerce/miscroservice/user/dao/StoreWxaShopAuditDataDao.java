package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.user.entity.StoreWxaShopAuditData;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/18 19:43
 * @Copyright 玖远网络
 */
public interface StoreWxaShopAuditDataDao{


    List<StoreWxaShopAuditData> findNormalAndDraftByShopName(String shopName);


    StoreWxaShopAuditData findDraftByStoreId(Long storeId, int shopType);

    int updateById(StoreWxaShopAuditData wxaUpd);

    int safeInsertInShopWxaDraft(StoreWxaShopAuditData data);

    List<StoreWxaShopAuditData> findAllNormal(Long storeId);

    int deleteById(Long dataId);

}
