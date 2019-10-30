package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoUserPreOrderPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoUserPreOrderVo;
import java.util.List;
public interface TSdxBookInfoUserPreOrderService {
    long modTSdxBookInfoUserPreOrder(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo);
    int delTSdxBookInfoUserPreOrderByIds(Long... ids);
    TSdxBookInfoUserPreOrderVo findTSdxBookInfoUserPreOrderById(Long id);
    List<TSdxBookInfoUserPreOrderVo> findTSdxBookInfoUserPreOrderByAll(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo, Integer page, Integer size);
}
