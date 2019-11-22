package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShoppingTrolleysVo;
import java.util.List;
public interface SdxShoppingTrolleysService {
    long modTSdxShoppingTrolleys(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo);
    int delTSdxShoppingTrolleysByIds(Long... ids);
    TSdxShoppingTrolleysVo findTSdxShoppingTrolleysById(Long id);
    List<TSdxShoppingTrolleysVo> findTSdxShoppingTrolleysByAll(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo, Integer page, Integer size);

	QueryResult list(Long id, Integer pageNum, Integer pageSize);

}
