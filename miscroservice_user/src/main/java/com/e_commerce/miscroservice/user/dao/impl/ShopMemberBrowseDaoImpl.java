package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.ShopMemberBrowseDao;
import com.e_commerce.miscroservice.user.entity.ShopMemberBrowse;
import com.e_commerce.miscroservice.user.mapper.ShopMemberBrowseMapper;
import com.e_commerce.miscroservice.user.vo.ShopMemberBrowseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/7 15:54
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberBrowseDaoImpl implements ShopMemberBrowseDao{

    private Log logger = Log.getInstance (ShopMemberBrowseDaoImpl.class);


    @Autowired
    private ShopMemberBrowseMapper shopMemberBrowseMapper;


    /**
     * 新增一条
     *
     * @param query query
     * @return com.e_commerce.miscroservice.user.entity.ShopMemberBrowse
     * @author Charlie
     * @date 2018/12/7 16:01
     */
    @Override
    public ShopMemberBrowse insertShopInShop(ShopMemberBrowseQuery query) {

        Long inShopMemberId = query.getInShopMemberId ();
        Long targetId = query.getTargetId ();

        ShopMemberBrowse history = MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberBrowse (),
                new MybatisSqlWhereBuild (ShopMemberBrowse.class)
                        .eq (ShopMemberBrowse::getDelStatus, StateEnum.NORMAL)
                        .eq (ShopMemberBrowse::getInShopMemberId, inShopMemberId)
                        .eq (ShopMemberBrowse::getTargetId, targetId)
                        .eq (ShopMemberBrowse::getType, 1)
        );

        //已经有了就更新
        if (history != null) {
            logger.info ("更新浏览时间");
            Long historyId = history.getId ();
            ShopMemberBrowse browse = new ShopMemberBrowse ();
            browse.setId (historyId);
            browse.setUpdateTime (new Timestamp (System.currentTimeMillis ()));
            MybatisOperaterUtil.getInstance ().update (browse, new MybatisSqlWhereBuild (ShopMemberBrowse.class).eq (ShopMemberBrowse::getId, historyId));
            return browse;
        }
        else {
            //没有就插入
            ShopMemberBrowse browse = new ShopMemberBrowse ();
            browse.setTargetId(targetId);
            browse.setInShopMemberId(inShopMemberId);
            browse.setType(1);
            MybatisOperaterUtil.getInstance().save(browse);
            return browse;
//            shopMemberBrowseMapper.safeInsertShopInShop(targetId, inShopMemberId);
//            return MybatisOperaterUtil.getInstance ().findOne (
//                    new ShopMemberBrowse (),
//                    new MybatisSqlWhereBuild (ShopMemberBrowse.class)
//                            .eq (ShopMemberBrowse::getDelStatus, StateEnum.NORMAL)
//                            .eq (ShopMemberBrowse::getInShopMemberId, inShopMemberId)
//                            .eq (ShopMemberBrowse::getTargetId, targetId)
//                            .eq (ShopMemberBrowse::getType, 1)
//            );
        }

    }




    @Override
    public ShopMemberBrowse findById(Long userId, Long browseId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberBrowse(),
                new MybatisSqlWhereBuild (ShopMemberBrowse.class)
                        .eq (ShopMemberBrowse::getId, browseId)
                        .eq (ShopMemberBrowse::getInShopMemberId, userId)
                        .eq (ShopMemberBrowse::getDelStatus, StateEnum.NORMAL)
        );
    }

    @Override
    public int updateById(ShopMemberBrowse history) {
        return MybatisOperaterUtil.getInstance ().update (history, new MybatisSqlWhereBuild (ShopMemberBrowse.class).eq (ShopMemberBrowse::getId, history.getId ()));
    }
}
