package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 15:18
 * @Copyright 玖远网络
 */
@RequestMapping( "/account" )
@RestController
public class StoreBusinessAccountController {


    @Autowired
    private StoreBusinessAccountService storeBusinessAccountService;

    /**
     * 更新用户账户
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    @RequestMapping( "/update" )
    public Response updateStoreBusinessAccount(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog){
        Integer in  = storeBusinessAccountService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
        return Response.success(in);
    }

    /**
     * 查询用户账户
     * @param userId
     * @return
     */
    @RequestMapping( {"/select","/select/auth"} )
    public Response selectStoreBusinessAccount(@RequestParam(value = "userId",required = false) Long userId){
        if (userId==null){
            userId= Long.valueOf(IdUtil.getId());
        }
         YjjStoreBusinessAccount y =storeBusinessAccountService.selectStoreBusinessAccount(userId);
         return  Response.success(y);
    }
    /**
     * 查询用户账户 日志
     * @param userId
     * @return
     */
    @RequestMapping( {"/log/select" ,"/log/select/auth" })
    public Response selectStoreBusinessAccountLog(@RequestParam(value = "userId",required = false) Long userId, @RequestParam(value = "pageNumber",required = false) Integer pageNumber){
        if (userId==null){
            userId= Long.valueOf(IdUtil.getId());
        }
         SimplePage simplePage =  storeBusinessAccountService.selectStoreBusinessAccountLog(userId,pageNumber);
        return Response.success(simplePage);
    }

    /**
     * 校验是否为店中店小程序
     * @param userId
     * @return
     */
    @RequestMapping({"/check/shop/auth","/check/shop"})
    public Response checkShopInShop(Long userId){
        if (userId==null){
            userId= Long.valueOf(IdUtil.getId());

        }
        return storeBusinessAccountService.checkShopInShop(userId);
    }

}
