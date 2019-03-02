package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.StoreWxaShopAuditDataDao;
import com.e_commerce.miscroservice.user.entity.StoreWxaShopAuditData;
import com.e_commerce.miscroservice.user.mapper.StoreWxaShopAuditDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/18 19:43
 * @Copyright 玖远网络
 */
@Component
public class StoreWxaShopAuditDataDaoImpl implements StoreWxaShopAuditDataDao{


    @Autowired
    private StoreWxaShopAuditDataMapper storeWxaShopAuditDataMapper;



    @Override
    public List<StoreWxaShopAuditData> findNormalAndDraftByShopName(String shopName) {
        return MybatisOperaterUtil.getInstance ().finAll (
                new StoreWxaShopAuditData (),
                new MybatisSqlWhereBuild (StoreWxaShopAuditData.class)
                        .eq (StoreWxaShopAuditData::getShopName, shopName)
                        .in (StoreWxaShopAuditData::getDelStatus, StateEnum.NORMAL, StateEnum.DRAFT)
        );
    }

    @Override
    public StoreWxaShopAuditData findDraftByStoreId(Long storeId, int shopType) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new StoreWxaShopAuditData (),
                new MybatisSqlWhereBuild (StoreWxaShopAuditData.class)
                        .eq (StoreWxaShopAuditData::getStoreId, storeId)
                        .in (StoreWxaShopAuditData::getDelStatus, StateEnum.DRAFT)
        );
    }

    @Override
    public int updateById(StoreWxaShopAuditData wxaUpd) {
        return MybatisOperaterUtil.getInstance ().update (
                wxaUpd, new MybatisSqlWhereBuild (StoreWxaShopAuditData.class).eq (StoreWxaShopAuditData::getId, wxaUpd.getId ())
        );
    }

    @Override
    public int safeInsertInShopWxaDraft(StoreWxaShopAuditData data) {
        return storeWxaShopAuditDataMapper.safeInsertInShopWxaDraft (data);
    }

    @Override
    public List<StoreWxaShopAuditData> findAllNormal(Long storeId) {
        return MybatisOperaterUtil.getInstance ().finAll (
                new StoreWxaShopAuditData (),
                new MybatisSqlWhereBuild (StoreWxaShopAuditData.class)
                        .eq (StoreWxaShopAuditData::getStoreId, storeId)
                        .eq (StoreWxaShopAuditData::getDelStatus, StateEnum.NORMAL)
        );
    }

    @Override
    public int deleteById(Long dataId) {
        StoreWxaShopAuditData upd = new StoreWxaShopAuditData ();
        upd.setDelStatus (StateEnum.DELETE);
        return MybatisOperaterUtil.getInstance ().update (
                upd, new MybatisSqlWhereBuild (StoreWxaShopAuditData.class).eq (StoreWxaShopAuditData::getId, dataId)
        );
    }


}
