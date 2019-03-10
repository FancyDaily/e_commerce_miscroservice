package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderRelationshipMapper {

	List<TOrderRelationship> pageEnrollAndChoose(Long userId);
}
