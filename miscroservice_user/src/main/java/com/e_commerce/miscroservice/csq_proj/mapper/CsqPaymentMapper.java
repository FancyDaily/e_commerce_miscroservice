package com.e_commerce.miscroservice.csq_proj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CsqPaymentMapper {

	/**
	 * 收入支出 统计
	 * @param userId
	 * @param inOut
	 * @return
	 */
	Double countMoney(@Param("userId") Long userId, @Param("inOut") Integer inOut);
}
