package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.MemberLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 21:13
 * @Copyright 玖远网络
 */
@Mapper
public interface MemberLogMapper{
    int insertSelective(MemberLog log);
}
