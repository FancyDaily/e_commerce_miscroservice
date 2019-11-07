package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxWishListPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxWishListVo;
import java.util.List;
public interface SdxWishListService {
    long modTSdxWishList(TSdxWishListPo tSdxWishListPo);
    int delTSdxWishListByIds(Long... ids);
    TSdxWishListVo findTSdxWishListById(Long id);
    List<TSdxWishListVo> findTSdxWishListByAll(TSdxWishListPo tSdxWishListPo, Integer page, Integer size);
}
