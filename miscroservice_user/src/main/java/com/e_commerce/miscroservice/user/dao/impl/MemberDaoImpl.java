package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.enums.SystemPlatform;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.MemberDao;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.service.impl.MemberServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 21:03
 * @Copyright 玖远网络
 */
@Component
public class MemberDaoImpl implements MemberDao{

    private Log logger = Log.getInstance (MemberDaoImpl.class);


    /**
     * 查询会员
     *
     * @param storeId        storeId
     * @param systemPlatform systemPlatform
     * @return com.e_commerce.miscroservice.commons.entity.application.user.Member
     * @author Charlie
     * @date 2018/12/10 20:49
     */
    @Override
    public Member findMember(Long storeId, SystemPlatform systemPlatform) {
        systemPlatform = systemPlatform == null ? SystemPlatform.STORE : systemPlatform;
        return MybatisOperaterUtil.getInstance ().findOne (
                new Member (), new MybatisSqlWhereBuild (Member.class).eq (Member::getUserId, storeId)
                        .eq (Member::getPlatformType, systemPlatform.getCode ())

        );
    }


    /**
     * 是够可以开通店中店会员
     *
     * @param store store
     * @return boolean
     * @author Charlie
     * @date 2018/12/10 17:50
     */
    @Override
    public boolean isCanOpenInShop(StoreBusiness store) {
        //当前小程序状态
        //老系统即使没有开通小程序,开通时间也可以设置(并没有严格的控制住)...所以做个判断,只有当前状态为开通的才能
        if (cannotBuyWxaShop (store)) {
            return Boolean.FALSE;
        }

        //是会员的,不能买
        Member member = findMember (store.getId (), SystemPlatform.STORE);
        if (member != null) {
            Integer memberType = member.getType();
            if (ObjectUtils.nullSafeEquals(memberType, 6)) {
                //980版可以购买
                return Boolean.TRUE;
            }
            return MemberServiceImpl.isCanBuyMember (member);
        }
        return Boolean.TRUE;
    }


    /**
     * 不能购买店铺(专享/共享)
     *
     * @param store store
     * @return true 不能购买
     * @author Charlie
     * @date 2018/12/18 10:30
     */
    @Override
    public boolean cannotBuyWxaShop(StoreBusiness store) {
        logger.info("用户信息 store={}", store);
        if (! store.getWxaBusinessType ().equals (0)) {
            //小程序未到期
            Long wxaCloseTime = store.getWxaCloseTime ();
            if (wxaCloseTime > System.currentTimeMillis ()) {
                logger.info ("已有一个没有过期的小程序");
                return true;
            }
            //账号关闭了
            if (store.getIsOpenWxa ().equals (2)) {
                logger.info ("账号关闭");
                return true;
            }

        }
        return false;
    }



    /**
     * 根据id查询
     *
     * @param id id
     * @author Charlie
     * @date 2018/12/18 10:30
     */
    @Override
    public Member findById(Long id) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new Member (), new MybatisSqlWhereBuild (Member.class).eq (Member::getId, id)
        );
    }

    @Override
    public int updateById(Member upd) {
        return MybatisOperaterUtil.getInstance ().update (
                upd, new MybatisSqlWhereBuild (Member.class).eq (Member::getId, upd.getId ())
        );
    }

}
