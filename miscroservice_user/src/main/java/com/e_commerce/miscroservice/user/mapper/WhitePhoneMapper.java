package com.e_commerce.miscroservice.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Create by hyf on 2018/11/6
 */
@Mapper
public interface WhitePhoneMapper {
    int getWhitePhone(@Param("phone") String phone);
}
