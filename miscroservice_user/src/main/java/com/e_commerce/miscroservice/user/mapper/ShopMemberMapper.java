package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:39
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberMapper{


    ShopMember selectOne(ShopMemberQuery query);
}
