package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.user.service.shop.ShopMemberBrowseService;
import com.e_commerce.miscroservice.user.vo.ShopMemberBrowseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/7 11:46
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "user/shopMemberBrowse" )
public class ShopMemberBrowseController{


    @Autowired
    private ShopMemberBrowseService shopMemberBrowseService;


    /**
     * 新增用户浏览记录
     *
     * @param type     浏览id
     * @param targetId 浏览目标id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/7 13:36
     */
    @RequestMapping( "add" )
    public Response add(
            @RequestParam( value = "type" ) Integer type,
            @RequestParam( value = "targetId", required = false ) Long targetId
    ) {

        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId->{
                    ShopMemberBrowseQuery query = new ShopMemberBrowseQuery ();
                    query.setType (type);
                    query.setTargetId (targetId);
                    query.setInShopMemberId (userId);
                    return shopMemberBrowseService.add (query);
                })
                .returnResponse ();
    }




    /**
     * 店中店浏览记录
     *
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/8 16:37
     */
    @RequestMapping( "list/shopInShop" )
    public Response listShopInShop(
                                   @RequestParam( value = "pageSize", defaultValue = "10" ) Integer pageSize,
                                   @RequestParam( value = "pageNumber", defaultValue = "1" ) Integer pageNumber) {
        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId -> shopMemberBrowseService.listShopInShop (userId, pageSize, pageNumber))
                .returnResponse ();
    }




    /**
     * 删除浏览记录
     *
     * @param browseId browseId
     * @author Charlie
     * @date 2018/12/12 11:53
     */
    @RequestMapping("delete")
    public Response delete(
            @RequestParam( value = "browseId") Long browseId
    ){
            return ResponseHelper.shouldLogin ()
                    .invokeNoReturnVal (userId -> shopMemberBrowseService.delete (userId, browseId))
                    .returnResponse ();
    }

}
