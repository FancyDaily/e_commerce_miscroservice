package com.e_commerce.miscroservice.user.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import com.e_commerce.miscroservice.user.service.shop.ShopMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:27
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping("user/rpc/shopMemberRpcController")
public class ShopMemberRpcController {

    @Autowired
    private ShopMemberService shopMemberService;

    @RequestMapping("findOne")
    public ShopMemberVo findOne(@RequestBody ShopMemberQuery query) {
        ShopMember shopMember = shopMemberService.selectOne(query);
        if (shopMember == null) {
            return null;
        }

        ShopMemberVo vo = new ShopMemberVo();
        vo.setStoreId(shopMember.getStoreId());
        vo.setUserIcon(shopMember.getUserIcon());
        vo.setUserNickname(shopMember.getUserNickname());
        vo.setSource(shopMember.getSource());
        vo.setBindWeixin(shopMember.getBindWeixin());
        vo.setSex(shopMember.getSex());
        vo.setInShopMemberId(shopMember.getInShopMemberId());
        vo.setInShopOpenId(shopMember.getInShopOpenId());
        vo.setId(shopMember.getId());
        vo.setWxPhone(shopMember.getWxPhone());
        vo.setBindPhone(shopMember.getBindPhone());

        return vo;
    }

    /**
     * 根据id查询会员
     * @param memberId
     * @return
     */
    @RequestMapping("findById")
    public ShopMember findById(@RequestParam(value = "memberId") Long memberId) {
        ShopMember shopMember = shopMemberService.findShopMemberById(memberId);
        return shopMember;
    }


}
