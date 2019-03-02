package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.user.entity.StoreBusiness;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:46
 * @Copyright 玖远网络
 */
public interface StoreBusinessDao{

    static final String APP_VERSION_372 = "372";


    static StoreBusiness buildDefaultStore() {
        long curr = System.currentTimeMillis();
        StoreBusiness user = new StoreBusiness ();
        //账户资金
//        user.setCashIncome(BigDecimal.ZERO);
//        user.setAvailableBalance(BigDecimal.ZERO);
        //提现申请次数
        user.setWithdrawApply(0);
        user.setCreateTime(curr);
        user.setUpdateTime(curr);
        user.setBankCardFlag(0);
        user.setAlipayFlag(0);
        user.setWeixinFlag(0);
        user.setBusinessType(0);
        user.setLastErrorWithdrawPasswordTime(0L);
        user.setErrorCount(0);
        user.setDeep(1L);
        user.setOneStageTime(0);
        user.setTwoStageTime(0);
        user.setThreeStageTime(0);
        user.setGroundUserId(0L);
        user.setShopReservationsOrderSwitch(1);
        user.setGreetingSendType(0);
        user.setGreetingWords("");
        user.setGreetingImage("");

        //默认值
//        user.setCommissionPercentage(0.0);
//        user.setMemberCommissionPercentage(0.0);
//        user.setDefaultCommissionPercentage(0.0);
        user.setHasHotonline(0);
        user.setSynchronousButtonStatus(0);
        user.setVip(0);
        user.setSupplierId(0L);
        user.setIsOpenWxa(0);
        user.setUsedCouponTotalMemberCount(0);
        user.setUsedCouponTotalCount(0);
//        user.setUsedCouponTotalMoney(0.0);
        user.setWxaType(0);
//        user.setRate(0.0);
        user.setWxaArticleShow(0);
        user.setWxaOpenTime(0L);
        user.setWxaCloseTime(0L);
        user.setBankCardUseFlag(0);
        user.setAlipayUseFlag(0);
        user.setWeixinUseFlag(0);
        user.setMemberNumber(0);
//        user.setStoreArea(0.0);
        user.setStoreShowImgs("[]");
        user.setGrade(0);
        user.setWxaRenewProtectCloseTime(0L);
        user.setHotOnline("");
        user.setOnlineWxaVersion("1.1.1");
        user.setAppId(APP_VERSION_372);
        //首次登录
        user.setFirstLoginStatus(1);

//        user.setAuditStatus(StoreAuditStatusEnum.submit.getIntValue());
        user.setAuditTime(0L);

        //店铺资料认证 2未提交
        user.setDataAuditStatus(2);
        user.setDataAuditTime(0L);

        //0:未激活；>0激活时间
        user.setActiveTime(0L);
        user.setActivationTime(0L);
        //账户状态0正常，-1删除，1 禁用
        user.setStatus(0);
        //0正常，1 禁用
        user.setDistributionStatus(0);

        user.setWxaBusinessType(0);
        return user;

    }

    /**
     * 根据店中店openId查找APP账户
     *
     * @param inShopOpenId inShopOpenId
     * @return com.e_commerce.miscroservice.user.entity.StoreBusiness
     * @author Charlie
     * @date 2018/12/10 16:16
     */
    StoreBusiness findByInShopOpenId(String inShopOpenId);

    StoreBusiness findById(Long storeId);

    StoreBusiness findByInMemberId(Long inShopMemberId);

}
