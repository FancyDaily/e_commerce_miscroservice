package com.e_commerce.miscroservice.lpglxt_proj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DemoMapper {

	/**
	 * 收入支出 统计
	 * @param userId
	 * @param inOut
	 * @return
	 */
	Double countMoney(@Param("userId") Long userId, @Param("inOut") Integer inOut);
}
