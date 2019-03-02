package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.user.entity.ShopMemberBrowse;
import com.e_commerce.miscroservice.user.vo.ShopMemberBrowseQuery;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/7 15:54
 * @Copyright 玖远网络
 */
public interface ShopMemberBrowseDao{




    /**
     * 新增一条
     *
     * @param query query
     * @return com.e_commerce.miscroservice.user.entity.ShopMemberBrowse
     * @author Charlie
     * @date 2018/12/7 16:01
     */
    ShopMemberBrowse insertShopInShop(ShopMemberBrowseQuery query);


    ShopMemberBrowse findById(Long userId, Long browseId);

    int updateById(ShopMemberBrowse history);

}
