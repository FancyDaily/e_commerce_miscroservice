package com.e_commerce.miscroservice.user.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 15:18
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "user/rpc/storeBusinessAccountRpcController" )
public class StoreBusinessAccountRpcController {

    private Log logger = Log.getInstance(StoreBusinessAccountRpcController.class);


    @Autowired
    private StoreBusinessAccountService storeBusinessAccountService;

    /**
     * 更新用户账户
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    @RequestMapping( "/accountUpdate" )
    public Integer updateStoreBusinessAccount(@RequestBody YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog){
        return storeBusinessAccountService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
    }

    /**
     * 查询用户账户
     * @param userId
     * @return
     */
    @RequestMapping( "/accountSelect" )
    public YjjStoreBusinessAccount selectStoreBusinessAccount(@RequestParam("userId") Long userId){
        return storeBusinessAccountService.selectStoreBusinessAccount(userId);
    }
       /**
     * 查询用户账户 日志
     * @param userId
     * @return
     */
    @RequestMapping( "/logSelect" )
    public SimplePage selectStoreBusinessAccountLog(@RequestParam("userId") Long userId, @RequestParam("pageNumber") Integer pageNumber){
        return storeBusinessAccountService.selectStoreBusinessAccountLog(userId,pageNumber);
    }

    /**
     * 将冻结金额转换为 可用金额
     * @param userId
     */
    @RequestMapping("/waitInToUse")
    public void waitInMoneyToUse(@RequestParam("userId") Long userId){
        storeBusinessAccountService.waitInMoneyToUse(userId);
    }

    /**
     * 更新账户待结算到已结算
     *
     * @param shopMemberOrderNo shopMemberOrderNo
     * @return boolean
     * @author Charlie
     * @date 2019/2/19 14:21
     */
    @RequestMapping( "/sendGoodsAfter15DaysWaitMoneyIn" )
    public boolean sendGoodsAfter15DaysWaitMoneyIn(@RequestParam( "shopMemberOrderNo" ) String shopMemberOrderNo,
                                                   @RequestParam( "storeId" ) Long storeId,
                                                   @RequestParam( "storeOrderNo" )Long storeOrderNo){
        return storeBusinessAccountService.sendGoodsAfter15DaysWaitMoneyIn(shopMemberOrderNo, storeId, storeOrderNo);
    }
}
