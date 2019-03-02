package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.entity.StoreWxa;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:45
 * @Copyright 玖远网络
 */
@Mapper
public interface StoreBusinessMapper{

    /**
     * 更新微信小程序关闭时间
     *
     * @param storeId 门店id
     * @param newWxaCloseTime 新的时间
     * @param historyWxaCloseTime 老的时间
     * @return int
     * @author Charlie
     * @date 2018/8/16 14:24
     */
    int updateWxaCloseTime(@Param("storeId") Long storeId, @Param ("newWxaCloseTime") Long newWxaCloseTime, @Param ("historyWxaCloseTime") Long historyWxaCloseTime, @Param ("updTime") Long updTime);

    StoreBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreBusiness updInfo);

    StoreBusiness selectOne(StoreBusinessVo query);

    int insertSelective(StoreBusiness user);

    /**
     * 更新老的微信信息
     * @param storeBusiness
     * @return
     */
    int oldUserBindWeixin(StoreBusinessVo storeBusiness);

    List<StoreBusiness> selectList(StoreBusinessVo userNameQuery);

    /**
     * 查询共享版的二维码是否存在
     */
    Map<String, Object> getShareShopLoginQr(@Param("storeId") Long storeId);

    /**
     * 查询店铺信息
     */
    List<StoreWxa>selectStoreWxaAppidList(@Param("storeId")Long storeId);

    /**
     * 根据id查询是否开通小程序
     * @param storeId
     * @return
     */
    StoreBusiness openWxUserByStoreId(@Param("storeId") Long storeId);

    int updateUrlNew(@Param("storeId") Long storeId,@Param("url")String url);

    List<Long> selectAllStoreId();

    int updeteUrl();
}
