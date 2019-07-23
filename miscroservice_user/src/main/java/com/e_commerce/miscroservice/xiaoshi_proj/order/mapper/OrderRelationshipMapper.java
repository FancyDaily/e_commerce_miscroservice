package com.e_commerce.miscroservice.xiaoshi_proj.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderRelationshipMapper {
	/**
	 * 小程序的报名选人列表
	 * @param userId 当前用户ID
	 * @return
	 */
	List<TOrderRelationship> pageEnrollAndChoose(Long userId);

	/**
	 * 组织的订单选人列表
	 * @param userId 当前用户ID
	 * @return
	 */
	List<TOrderRelationship> getChooseList(Long userId);
}
