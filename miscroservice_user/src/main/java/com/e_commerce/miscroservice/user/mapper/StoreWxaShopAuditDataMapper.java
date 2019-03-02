package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.user.entity.StoreWxaShopAuditData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/19 0:41
 * @Copyright 玖远网络
 */
@Mapper
public interface StoreWxaShopAuditDataMapper{
    int safeInsertInShopWxaDraft(StoreWxaShopAuditData data);
}
