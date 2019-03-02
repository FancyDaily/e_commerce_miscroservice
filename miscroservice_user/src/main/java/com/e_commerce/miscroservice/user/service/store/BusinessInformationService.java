package com.e_commerce.miscroservice.user.service.store;


import com.e_commerce.miscroservice.commons.helper.util.service.Response;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/25 2:44
 * @Copyright 玖远网络
 */
public interface BusinessInformationService {
    Map<String, Object> findByStoreId(Long storeId);

}
