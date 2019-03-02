package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.user.StoreWxaShopAuditDataQuery;
import com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.commons.utils.SmsUtils;
import com.e_commerce.miscroservice.user.service.shop.ShopMemberService;
import com.e_commerce.miscroservice.user.service.store.BusinessInformationService;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/8 16:39
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "user/shopInShop" )
public class ShopInShopController{


    @Autowired
    private BusinessInformationService businessInformationService;
    @Autowired
    private ShopMemberService shopMemberService;
    @Autowired
    private StoreBusinessService storeBusinessService;


    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param type  1:店中店用户绑定
     * @return true 成功,false 失败
     * @author Charlie
     * @date 2018/9/24 10:24
     */
    @RequestMapping( "sendAuthCode" )
    public Response sendAuthCode(
            @RequestParam( "phone" ) String phone,
            @RequestParam( "type" ) Integer type) {

        RedisKeyEnum redisKey = RedisKeyEnum.createKey (type);
        return redisKey == null ?
                Response.errorMsg ("未知的短信发送类型") : Response.success (SmsUtils.sendCodeSafe (redisKey, phone));
    }


    /**
     * 店中店,店主登录
     *
     * @param phone        phone
     * @param phoneCode    phoneCode
     * @return storeId 门店id
     * @author Charlie
     * @date 2018/12/10 13:55
     */
    @RequestMapping( "inShopStoreLogin" )
    public Response inShopStoreLogin(
            @RequestParam( "phone" ) String phone,
            @RequestParam( "phoneCode" ) String phoneCode
    ) {
        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId-> storeBusinessService.inShopStoreLogin (userId, phone, phoneCode))
                .returnResponse ();
    }


    /**
     * 用户进店
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/12 11:07
     */
    @RequestMapping( "inStoreShop" )
    public Response inStoreShop(
                                @RequestParam( value = "storeId" ) Long storeId,
                                @RequestParam( value = "inShopMemberId" ) Long inShopMemberId
    ) {

        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId-> shopMemberService.findByInShopOpenIdIfNullCreateNew (userId, storeId, inShopMemberId))
                .returnResponse ();
    }



    /**
     * 我要开店
     *
     * @return {
     * 1是,0否
     *     needLoginBindApp : 是否需要登录
     *      hasInShop : 是有已有店铺
     * }
     * @author Charlie
     * @date 2018/12/13 19:36
     */
    @RequestMapping("iWantOpenStore")
    public Response iWantOpenStore(){
        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId-> storeBusinessService.iWantOpenStore (userId))
                .returnResponse ();
    }


    /**
     * 用户店铺资料
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/13 20:30
     */
    @RequestMapping( "wxaDataHistoryList" )
    public Response wxaDataHistoryList() {
        return ResponseHelper.shouldLogin ()
                .invokeHasReturnVal (userId-> storeBusinessService.wxaDataHistoryList (userId))
                .returnResponse ();
    }




    /**
     * 提交共享小程序店铺资料
     *
     * @param bossName 店主名称
     * @param shopName 店铺名称
     * @param industry 行业
     * @param mainBusiness 主营业务
     * @param address 地址
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/12 13:37
     */
    @RequestMapping( "commitWxaData" )
    public Response commitWxaData(
            @RequestParam( "storeName" ) String shopName,
            @RequestParam( "nickName" ) String bossName,
            @RequestParam( "industry" ) String industry,
            @RequestParam( "mainBusiness" ) String mainBusiness,
            @RequestParam( "businessAddress" ) String address
    ) {
        return ResponseHelper.shouldLogin ()
                .invokeNoReturnVal (userId->{
                    StoreWxaShopAuditDataQuery query = new StoreWxaShopAuditDataQuery ();
                    query.setInShopMemberId (userId);
                    query.setBossName (bossName);
                    query.setShopName (shopName);
                    query.setAddress (address);
                    query.setIndustry (industry);
                    query.setMainBusiness (mainBusiness);
                    storeBusinessService.commitInShopWxaData (query);
                })
                .returnResponse ();
    }



    /**
     * 店铺是否可用
     *
     * @param storeId storeId
     * @return boolean true:可用
     * @author Charlie
     * @date 2018/12/19 16:17
     */
    @RequestMapping( "checkStoreShop" )
    public Response checkStoreShop(@RequestParam( "storeId" ) Long storeId) {
        try {
            return Response.success (storeBusinessService.checkStoreShop (storeId));
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler (e);
        }
    }


    /**
     * 门店信息
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/25 2:55
     */
    @RequestMapping( "businessInformationData" )
    public Response businessInformationData(@RequestParam( "storeId" ) Long storeId) {
        return Response.success(businessInformationService.findByStoreId(storeId));
    }



    /**
     * 测试用 随时删
     */
    @RequestMapping("/demo")
    public String creatUrl(Long storeId){
       return storeBusinessService.demo(storeId);
    }

    @RequestMapping("/xunhuan")
    public void checkUrl(){
        storeBusinessService.check();
    }
    @RequestMapping("/delete/url")
    public void deleteUrl(){
        storeBusinessService.deleteUrl();
    }

}
