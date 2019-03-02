package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.enums.SystemPlatform;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 21:03
 * @Copyright 玖远网络
 */
public interface MemberDao{


    /**
     * 查询会员
     *
     * @param storeId storeId
     * @param systemPlatform systemPlatform
     * @return com.e_commerce.miscroservice.commons.entity.application.user.Member
     * @author Charlie
     * @date 2018/12/10 20:49
     */
    Member findMember(Long storeId, SystemPlatform systemPlatform);


    /**
     *
     * 是够可以开通店中店会员
     *
     * @param store store
     * @return boolean
     * @author Charlie
     * @date 2018/12/10 17:50
     */
    boolean isCanOpenInShop(StoreBusiness store);

    /**
     * 不能购买店铺(专享/共享)
     *
     * @param store store
     * @return true 不能购买
     * @author Charlie
     * @date 2018/12/18 10:30
     */
    boolean cannotBuyWxaShop(StoreBusiness store);

    /**
     * 根据id查询
     *
     * @param id id
     * @author Charlie
     * @date 2018/12/18 10:30
     */
    Member findById(Long id);

    int updateById(Member upd);
}
