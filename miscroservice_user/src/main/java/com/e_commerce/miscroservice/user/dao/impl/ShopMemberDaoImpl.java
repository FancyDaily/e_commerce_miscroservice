package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.ShopMemberDao;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import com.e_commerce.miscroservice.user.mapper.ShopMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:38
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberDaoImpl implements ShopMemberDao {

    @Autowired
    private ShopMemberMapper shopMemberMapper;

    @Override
    public ShopMember selectOne(ShopMemberQuery query) {
        return shopMemberMapper.selectOne(query);
    }


    /**
     * 根据openId查询
     *
     * @param openId openId
     * @return com.e_commerce.miscroservice.user.entity.ShopMember
     * @author Charlie
     * @date 2018/12/12 9:50
     */
    @Override
    public ShopMember findByOpenId(String openId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ShopMember(),
                new MybatisSqlWhereBuild(ShopMember.class)
                        .eq(ShopMember::getBindWeixin, openId)
                        .in(ShopMember::getStatus, 0, 1)
        );
    }

    @Override
    public int save(ShopMember newUser) {
        return MybatisOperaterUtil.getInstance().save(newUser);
    }

    @Override
    public ShopMember findById(Long id) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ShopMember(),
                new MybatisSqlWhereBuild(ShopMember.class)
                        .eq(ShopMember::getId, id)
        );
    }


    /**
     * 中端用户id查找店中店的用户id
     *
     * @param inShopMember inShopOpenId
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.user.entity.ShopMember
     * @author Charlie
     * @date 2018/12/12 9:39
     */
    @Override
    public ShopMember findByInShopMemberIdAndStoreId(Long inShopMember, Long storeId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ShopMember(),
                new MybatisSqlWhereBuild(ShopMember.class)
                        .eq(ShopMember::getInShopMemberId, inShopMember)
                        .eq(ShopMember::getStoreId, storeId)
                        .in(ShopMember::getStatus, 0, 1)
        );
    }

    @Override
    public int updateById(ShopMember updShopMember) {
        long id = updShopMember.getId();
        updShopMember.setId(null);
        return MybatisOperaterUtil.getInstance().update(updShopMember, new MybatisSqlWhereBuild(ShopMember.class).eq(ShopMember::getId, id));
    }

    @Override
    public ShopMember findByCurrentOpenIdAndStoreId(String currentOpenId, Long storeId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ShopMember(),
                new MybatisSqlWhereBuild(ShopMember.class)
                        .eq(ShopMember::getBindWeixin, currentOpenId)
                        .eq(ShopMember::getStoreId, storeId)
        );
    }


}
