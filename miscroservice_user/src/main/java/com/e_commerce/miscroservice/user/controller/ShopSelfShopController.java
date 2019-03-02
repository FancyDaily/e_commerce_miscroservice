package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.user.service.shop.ShopMemberService;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/18 9:51
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "user/shopSelfShop" )
public class ShopSelfShopController{

    private Log logger = Log.getInstance(ShopSelfShopController.class);


    @Autowired
    private ShopMemberService shopMemberService;
    @Autowired
    private StoreBusinessService storeBusinessService;


    /**
     * 用户是否可以开通专项版小程序
     * <p>
     *     该步骤调用在 老运营系统,小程序授权之前
     *     主要用来控制用户只能拥有一个小程序,专享或共享
     *     如果系统迁移了,建议将该code块,放在专项版小程序授权的业务里
     * </p>
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/18 9:53
     */
    @RequestMapping( "isCanOpenSelfShop" )
    public Response isCanOpenSelfShop(Long storeId) {
        return Response.success (storeBusinessService.isCanOpenSelfShop (storeId));
    }




    /**
     * 初始化小程序开通时间
     * <p>老运营授权后调用</p>
     *
     * @param storeId      用户id
     * @param wxaOpenTime  开通时间
     * @param wxaCloseTime 结束时间
     * @param force 是否强制更新
     * @return boolean
     * @author Charlie
     * @date 2018/12/17 20:46
     */
    @RequestMapping( "initWxaOpenTime" )
    public boolean initWxaOpenTime(
            @RequestParam( "storeId" ) Long storeId,
            @RequestParam( "wxaOpenTime" ) Long wxaOpenTime,
            @RequestParam( "wxaCloseTime" ) Long wxaCloseTime,
            @RequestParam( value = "force", defaultValue = "false" ) Boolean force) {
        try {
            storeBusinessService.initWxaOpenTime (storeId, wxaOpenTime, wxaCloseTime, force);
        } catch (ErrorHelper e) {
            logger.info (e.getMsg ());
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }


    /**
     * 更新用户微信手机号
     *
     * @param appId appId
     * @param sessionId sessionId
     * @param encryptedData 加密数据
     * @param iv 加密数据
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/26 19:05
     */
    @RequestMapping( "addUpdWxPhone" )
    public Response addUpdWxPhone(
            @RequestParam("appId") String appId,
            @RequestParam("sessionId") String sessionId,
            @RequestParam("encryptedData") String encryptedData,
            @RequestParam("iv") String iv
    ) {
        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(shopMemberId-> shopMemberService.addUpdWxPhone(appId, sessionId, shopMemberId, encryptedData, iv))
                .returnResponse();

    }


    /**
     * 获取小程序用户信息
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/27 13:09
     */
    @RequestMapping( "userInfo" )
    public Response userInfo() {
        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(shopMemberId-> shopMemberService.userInfo(shopMemberId))
                .returnResponse();
    }


}
