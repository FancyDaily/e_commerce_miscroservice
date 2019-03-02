package com.e_commerce.miscroservice.user.service.store;

import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.user.MemberOperatorRequest;
import com.e_commerce.miscroservice.commons.entity.user.StoreShopVo;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaShopAuditDataQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.entity.StoreWxaShopAuditData;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:47
 * @Copyright 玖远网络
 */
public interface StoreBusinessService{

    /**
     * 更新微信小程序关闭时间
     *
     *
     * @param storeId 门店id
     * @param newWxaCloseTime 新的时间
     * @param historyWxaCloseTime 老的时间
     * @return int
     * @author Charlie
     * @date 2018/8/16 14:24
     */
    int updateWxaCloseTime(Long storeId, Long newWxaCloseTime, Long historyWxaCloseTime);

    StoreBusiness selectById(Long id);

    int updateByPrimaryKeySelective(StoreBusiness updInfo);

    StoreBusiness selectOne(StoreBusinessVo query);

    int insertSelective(StoreBusiness user);

    /**
     * 店中店登录
     *
     * @param inShopMemberId 店中店openId
     * @param phone 手机号
     * @param phoneCode 验证码
     * @author Charlie
     * @date 2018/12/10 14:16
     */
    Map<String, Object> inShopStoreLogin(Long inShopMemberId, String phone, String phoneCode);


    /**
     * 提交共享小程序店铺资料
     *
     * @param query query
     * @return com.e_commerce.miscroservice.user.entity.StoreWxa
     * @author Charlie
     * @date 2018/12/10 15:56
     */
    StoreWxaShopAuditData commitInShopWxaData(StoreWxaShopAuditDataQuery query);


    /**
     * 提交共享小程序店铺资料
     *
     * @param query query
     * @param store store
     * @return com.e_commerce.miscroservice.user.entity.StoreWxaShopAuditData
     * @author Charlie
     * @date 2018/12/19 10:27
     */
    StoreWxaShopAuditData doCommitShopWxaDataByStore(StoreWxaShopAuditDataQuery query, StoreBusiness store);



    /**
     * 用户是否可以开通店中店
     *
     * @param storeId storeId
     * @return boolean
     * @author Charlie
     * @date 2018/12/11 15:58
     */
    boolean isCanOpenInShop(Long storeId);


    /**
     * 根据店中店用户小程序用户id.查询APP账户id
     *
     * @param inShopMemberId inShopMemberId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/12 15:24
     */
    StoreBusiness findByInMemberId(Long inShopMemberId);


    /**
     * 开通店中店
     *
     * @param request request
     * @author Charlie
     * @date 2018/12/13 9:53
     */
    void openWxaInShop(MemberOperatorRequest request, StoreBusiness store);

    /**
     * 用户给是否可以开店
     *
     * @param inShopMemberId inShopMemberId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/13 18:20
     */
    Map<String, Object> iWantOpenStore(Long inShopMemberId);



    /**
     * 用户店铺资料
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/13 20:30
     */
    Map<String, Object> wxaDataHistoryList(Long inShopMemberId);





    /**
     * 打开店铺状态
     *
     * @param storeId storeId
     * @param isOpenWxa  是否开通小程序,0未开通(正常)1已开通(正常),2冻结(手工关闭)
     * @return boolean
     * @author Charlie
     * @date 2018/12/17 20:17
     */
    void openWxaStatus(Long storeId, Integer isOpenWxa);



    /**
     * 初始化小程序开通时间
     *
     * @param storeId 用户id
     * @param wxaOpenTime 开通时间
     * @param wxaCloseTime 结束时间
     * @param force 强制更新
     * @return boolean
     * @author Charlie
     * @date 2018/12/17 20:46
     */
    void initWxaOpenTime(Long storeId, Long wxaOpenTime, Long wxaCloseTime, Boolean force);



    /**
     * 用户店铺状态
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.commons.entity.user.StoreShopVo
     * @author Charlie
     * @date 2018/12/18 10:23
     */
    StoreShopVo storeShopStatus(Long storeId);



    /**
     * 是否可开通专享店铺
     *
     * @param storeId storeId
     * @return boolean
     * @author Charlie
     * @date 2018/12/18 10:32
     */
    boolean isCanOpenSelfShop(Long storeId);



    /**
     * 店铺是否可用
     *
     * @param storeId storeId
     * @return boolean true:可用
     * @author Charlie
     * @date 2018/12/19 16:17
     */
    Map<String, Object> checkStoreShop(Long storeId);

    String demo(Long storeId);

    void check();
    void deleteUrl();
}
