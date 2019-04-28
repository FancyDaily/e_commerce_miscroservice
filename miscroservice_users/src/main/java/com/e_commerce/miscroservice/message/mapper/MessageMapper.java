package com.e_commerce.miscroservice.message.mapper;

import com.e_commerce.miscroservice.commons.entity.application.TMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MessageMapper {

	List<TMessage> selectMessageList(@Param("nowUserId")Long nowUserId , @Param("lastTime")Long lastTime);
}
