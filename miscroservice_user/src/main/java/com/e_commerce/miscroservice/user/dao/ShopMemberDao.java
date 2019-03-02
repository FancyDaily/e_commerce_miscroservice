package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.user.entity.ShopMember;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:38
 * @Copyright 玖远网络
 */
public interface ShopMemberDao{

    ShopMember selectOne(ShopMemberQuery query);



    /**
     * 根据openId查询
     *
     * @param openId openId
     * @return com.e_commerce.miscroservice.user.entity.ShopMember
     * @author Charlie
     * @date 2018/12/12 9:50
     */
    ShopMember findByOpenId(String openId);



    int save(ShopMember newUser);

    ShopMember findById(Long id);



    /**
     * 中端用户id查找店中店的用户id
     *
     * @param inShopMember inShopOpenId
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.user.entity.ShopMember
     * @author Charlie
     * @date 2018/12/12 9:39
     */
    ShopMember findByInShopMemberIdAndStoreId(Long inShopMember, Long storeId);

    int updateById(ShopMember updShopMember);


    ShopMember findByCurrentOpenIdAndStoreId(String currentOpenId, Long storeId);
}
