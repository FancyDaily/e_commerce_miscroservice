package com.e_commerce.miscroservice.user.dao;


import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/25 2:47
 * @Copyright 玖远网络
 */
public interface BusinessInformationDao {
    Map<String, Object> findByStoreId(Long storeId);

}
