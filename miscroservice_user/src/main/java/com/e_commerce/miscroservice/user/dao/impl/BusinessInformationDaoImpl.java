package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.BusinessInformationDao;
import com.e_commerce.miscroservice.user.mapper.BusinessInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/25 2:47
 * @Copyright 玖远网络
 */
@Component
public class BusinessInformationDaoImpl implements BusinessInformationDao {

    @Autowired
    private BusinessInformationMapper businessInformationMapper;

    @Override
    public Map<String, Object> findByStoreId(Long storeId) {
        return businessInformationMapper.findByStoreId(storeId);
    }
}
