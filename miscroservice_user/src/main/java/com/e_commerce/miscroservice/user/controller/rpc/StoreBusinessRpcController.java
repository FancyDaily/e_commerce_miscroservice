package com.e_commerce.miscroservice.user.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.user.StoreShopVo;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import com.e_commerce.miscroservice.user.service.store.StoreWxaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 15:18
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "user/rpc/storeBusinessRpcController" )
public class StoreBusinessRpcController{


    private Log logger = Log.getInstance (StoreBusinessRpcController.class);


    @Autowired
    private StoreWxaService storeWxaService;
    @Autowired
    private StoreBusinessService storeBusinessService;


    /**
     * 根据店中店用户小程序用户id.查询APP账户id
     *
     * @param inShopMemberId inShopMemberId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/12 15:24
     */
    @RequestMapping( "findStoreIdByInShopMemberId" )
    public StoreBusinessVo findStoreIdByInShopMemberId(@RequestParam( "inShopMemberId" ) Long inShopMemberId) {
        StoreBusiness store = storeBusinessService.findByInMemberId (inShopMemberId);
        if (store == null) {
            return null;
        }
        StoreBusinessVo res = new StoreBusinessVo ();
        res.setId (store.getId ());
        res.setBusinessName (store.getBusinessName ());
        res.setBusinessAddress (store.getBusinessAddress ());
        res.setWxaBusinessType (store.getWxaBusinessType ());
        res.setInShopOpenId (store.getInShopOpenId ());
        res.setWxaAppId (store.getWxaAppId ());
        res.setIsOpenWxa (store.getIsOpenWxa ());
        res.setInShopOpenId (store.getInShopOpenId ());
        res.setInShopMemberId (store.getInShopMemberId ());
        res.setWxaCloseTime (store.getWxaCloseTime ());
        res.setWxaOpenTime (store.getWxaOpenTime ());
        return res;
    }


    @RequestMapping( "findOne" )
    public StoreBusinessVo findOne(@RequestBody StoreBusinessVo query) {
        StoreBusiness store = storeBusinessService.selectOne (query);
        if (store == null) {
            return null;
        }
        StoreBusinessVo vo = new StoreBusinessVo ();
        vo.setId (store.getId ());
        vo.setPhoneNumber (store.getPhoneNumber ());
        vo.setInShopOpenId (store.getInShopOpenId ());
        vo.setBindWeixinId (store.getBindWeixinId ());
        vo.setBindWeixinIcon (store.getBindWeixinIcon ());
        vo.setWxaCloseTime (store.getWxaCloseTime ());
        vo.setWxaOpenTime (store.getWxaOpenTime ());
        vo.setWxaAppId (store.getWxaAppId ());
        vo.setIsOpenWxa (store.getIsOpenWxa ());
        vo.setInShopMemberId (store.getInShopMemberId ());
        vo.setInShopOpenId (store.getInShopOpenId ());
        vo.setBusinessAddress (store.getBusinessAddress ());
        vo.setBusinessName (store.getBusinessName ());
        vo.setBusinessType (store.getBusinessType ());
        return vo;
    }


    /**
     * 用户是否可以开通店中店
     *
     * @param storeId storeId
     * @return boolean
     * @author Charlie
     * @date 2018/12/11 15:59
     */
    @RequestMapping( "isCanOpenInShop" )
    public boolean isCanOpenInShop(@RequestParam( "storeId" ) Long storeId) {
        return storeBusinessService.isCanOpenInShop (storeId);
    }


    /**
     * 用户的店铺状态
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.commons.entity.user.StoreShopVo
     * @author Charlie
     * @date 2018/12/17 15:04
     */
    @RequestMapping( "storeShopStatus" )
    public StoreShopVo storeShopStatus(Long storeId) {
        return storeBusinessService.storeShopStatus (storeId);
    }


    /**
     * 打开店铺状态
     *
     * @param storeId   storeId
     * @param isOpenWxa 是否开通小程序,0未开通(正常)1已开通(正常),2冻结(手工关闭)
     * @return boolean
     * @author Charlie
     * @date 2018/12/17 20:17
     */
    @RequestMapping( "openWxaStatus" )
    public boolean openWxaStatus(@RequestParam( "storeId" ) Long storeId, @RequestParam( "isOpenWxa" ) Integer isOpenWxa) {
        try {
            storeBusinessService.openWxaStatus (storeId, isOpenWxa);
        } catch (ErrorHelper e) {
            logger.info (e.getMsg ());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 初始化小程序开通时间
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



}
