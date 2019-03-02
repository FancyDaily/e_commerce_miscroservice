package com.e_commerce.miscroservice.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/7 19:16
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberBrowseMapper{



    /**
     * 插入一个店中店的浏览记录
     *
     * @param targetId targetId
     * @param inShopMemberId inShopMemberId
     * @return int
     * @author Charlie
     * @date 2018/12/7 19:25
     */
    int safeInsertShopInShop(@Param ("targetId") Long targetId, @Param ("inShopMemberId") Long inShopMemberId);

    /**
     * 店中店浏览记录
     *
     * @param inShopMemberId inShopMemberId
     * @return java.util.List<java.util.Map
     * @author Charlie
     * @date 2018/12/8 15:49
     */
    List<Map<String,Object>> listShopInShop(@Param ("inShopMemberId") Long inShopMemberId);
}
