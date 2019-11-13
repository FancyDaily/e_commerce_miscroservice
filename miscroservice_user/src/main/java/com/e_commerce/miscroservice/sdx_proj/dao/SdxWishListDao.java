package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxWishListPo;
import java.util.List;
public interface SdxWishListDao {
    int saveTSdxWishListIfNotExist(TSdxWishListPo tSdxWishListPo);
    int modTSdxWishList(TSdxWishListPo tSdxWishListPo);
    int delTSdxWishListByIds(Long... ids);
    TSdxWishListPo findTSdxWishListById(Long id);
    List<TSdxWishListPo> findTSdxWishListByAll(TSdxWishListPo tSdxWishListPo, Integer page, Integer size);

	List<TSdxWishListPo> selectByUserId(Long userId, Integer pageNum, Integer pageSize);
}
