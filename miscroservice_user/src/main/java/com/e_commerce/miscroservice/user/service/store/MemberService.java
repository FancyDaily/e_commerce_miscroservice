package com.e_commerce.miscroservice.user.service.store;

import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.entity.user.MemberOperatorRequest;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaShopAuditDataQuery;
import com.e_commerce.miscroservice.commons.enums.SystemPlatform;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:47
 * @Copyright 玖远网络
 */
public interface MemberService{

    /**
     * 根据用户id查询 会员信息
     * <p>
     * 如果会员已过期,已删除,返回null
     * </p>
     */
    Member findValidMemberByUserId(SystemPlatform systemPlatform, Long userId);


    /**
     * 根据用户id查询 会员信息(统一入口)
     *
     * @param systemPlatform systemPlatform
     * @param userId         userId
     * @return com.yujj.entity.product.Member
     * @author Charlie
     * @date 2018/8/16 10:45
     */
    Member findMemberByUserId(SystemPlatform systemPlatform, Long userId);

//    /**
//     * 买会员
//     * @param memberOperatorRequest memberOperatorRequest
//     * @return com.e_commerce.miscroservice.commons.entity.application.user.Member
//     * @date 2018/9/30 16:51
//     */
//    Member buyMember(MemberOperatorRequest memberOperatorRequest);


    /**
     * 购买会员成功
     * <p>店中店购买</p>
     *
     * @param request request
     * @return boolean
     * @author Charlie
     * @date 2018/12/19 10:19
     */
    boolean buyMemberSuccess(MemberOperatorRequest request);




    /**
     * 线下开通共享店铺
     *
     * @param memberReq memberReq
     * @param auditReq auditReq
     * @return boolean
     * @author Charlie
     * @date 2018/12/19 10:38
     */
    boolean openInShopMemberOffline(MemberOperatorRequest memberReq, StoreWxaShopAuditDataQuery auditReq);





    /**
     * 更新用户会员状态
     *
     * @param memberId memberId
     * @param delStatus 0 正常  1 删除
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/19 11:32
     */
    boolean updMemberDelStatus(Long memberId, Integer delStatus);
}
