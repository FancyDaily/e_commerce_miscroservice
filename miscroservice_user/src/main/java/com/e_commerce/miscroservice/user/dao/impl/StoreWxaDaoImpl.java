package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.StoreWxaDao;
import com.e_commerce.miscroservice.user.entity.StoreWxa;
import org.springframework.stereotype.Component;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/6 15:41
 * @Copyright 玖远网络
 */
@Component
public class StoreWxaDaoImpl implements StoreWxaDao{

    @Override
    public StoreWxa findSelfShopByStoreId(Long storeId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new StoreWxa (), new MybatisSqlWhereBuild (StoreWxa.class).eq (StoreWxa::getStoreId, storeId)
        );
    }
}
