package com.e_commerce.miscroservice.user.service.shop;

import com.e_commerce.miscroservice.user.entity.ShopMemberBrowse;
import com.e_commerce.miscroservice.user.vo.ShopMemberBrowseQuery;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/7 15:45
 * @Copyright 玖远网络
 */
public interface ShopMemberBrowseService{


    /**
     * 新增用户的浏览记录
     *
     * @param query query
     * @return com.e_commerce.miscroservice.user.entity.ShopMemberBrowseHistory
     * @author Charlie
     * @date 2018/12/7 15:49
     */
    ShopMemberBrowse add(ShopMemberBrowseQuery query);


    /**
     * 浏览记录
     *
     * @param userId userId
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Charlie
     * @date 2018/12/8 11:49
     */
    Map<String, Object> listShopInShop(Long userId, Integer pageSize, Integer pageNumber);


    /**
     * 删除浏览记录
     *
     * @param userId openId
     * @param browseId browseId
     * @author Charlie
     * @date 2018/12/12 11:53
     */
    void delete(Long userId, Long browseId);
}
