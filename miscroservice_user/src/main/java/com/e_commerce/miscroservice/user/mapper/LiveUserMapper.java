package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.user.entity.rep.AnchorRep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/23 9:50
 */
@Mapper
public interface LiveUserMapper {
    /**
     * 查询 主播列表
     * @param storeId
     * @return
     */
    List<AnchorRep> findLiveUserStoreId(@Param("storeId") Long storeId);
}
