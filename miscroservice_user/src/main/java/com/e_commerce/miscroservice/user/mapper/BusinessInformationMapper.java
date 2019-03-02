package com.e_commerce.miscroservice.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/25 6:34
 * @Copyright 玖远网络
 */
@Mapper
public interface BusinessInformationMapper {
    Map<String,Object> findByStoreId(@Param("storeId") Long storeId);

}
