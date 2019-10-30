package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShoppingTrolleysVo;
import java.util.List;
public interface TSdxShoppingTrolleysService {
    long modTSdxShoppingTrolleys(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo);
    int delTSdxShoppingTrolleysByIds(Long... ids);
    TSdxShoppingTrolleysVo findTSdxShoppingTrolleysById(Long id);
    List<TSdxShoppingTrolleysVo> findTSdxShoppingTrolleysByAll(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo, Integer page, Integer size);
}
