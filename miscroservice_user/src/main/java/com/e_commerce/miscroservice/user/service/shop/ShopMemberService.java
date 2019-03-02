package com.e_commerce.miscroservice.user.service.shop;


import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.user.entity.ShopMember;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/9 11:06
 * @Copyright 玖远网络
 */
public interface ShopMemberService{

    ShopMember selectOne(ShopMemberQuery query);



    /**
     * 用户进店,获取memberId
     *
     * @param currentUserId 跳转的小程序用户id
     * @param storeId 跳转的门店id
     * @param inShopMemberId 店中店中端小程序用户id
     * @return com.e_commerce.miscroservice.user.entity.ShopMember
     * @author Charlie
     * @date 2018/12/12 9:35
     */
    Map<String, Object> findByInShopOpenIdIfNullCreateNew(Long currentUserId, Long storeId, Long inShopMemberId);




    /**
     * 更细用户微信手机号
     *
     * @param appId appId
     * @param sessionId sessionId
     * @param encryptedData 加密数据
     * @param iv 加密数据
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/26 19:05
     */
    Map<String, Object> addUpdWxPhone(String appId, String sessionId, Long shopMemberId, String encryptedData, String iv);



    /**
     * 获取小程序用户信息
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/27 13:09
     */

    Map<String, Object> userInfo(Long shopMemberId);
    /**
     * 根据id查询会员
     * @param memberId
     * @return
     */
    ShopMember findShopMemberById(Long memberId);
}
