package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.user.dao.BusinessInformationDao;
import com.e_commerce.miscroservice.user.service.store.BusinessInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/25 2:44
 * @Copyright 玖远网络
 */
@Service
public class BusinessInformationServiceImpl implements BusinessInformationService {

    private Log logger = Log.getInstance(BusinessInformationServiceImpl.class);

    @Autowired
    private BusinessInformationDao businessInformationDao;

    @Override
    public Map<String, Object> findByStoreId(Long storeId) {
        logger.info("查询店铺信息storeId={}", storeId);
        return businessInformationDao.findByStoreId(storeId);
    }
}
