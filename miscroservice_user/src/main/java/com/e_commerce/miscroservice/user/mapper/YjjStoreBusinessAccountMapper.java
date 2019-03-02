package com.e_commerce.miscroservice.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:39
 * @Copyright 玖远网络
 */
@Mapper
public interface YjjStoreBusinessAccountMapper {


    /**
     * 更新账户金额操作
     * @param userId
     * @param operMoney
     * @return
     */
    Integer upUseMoney(@Param("userId") Long userId, @Param("operMoney") Double operMoney);

}
