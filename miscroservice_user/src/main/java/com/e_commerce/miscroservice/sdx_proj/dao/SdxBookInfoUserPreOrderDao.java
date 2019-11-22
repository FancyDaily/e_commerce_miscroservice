package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoUserPreOrderPo;
import java.util.List;
public interface SdxBookInfoUserPreOrderDao {
    int saveTSdxBookInfoUserPreOrderIfNotExist(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo);
    int modTSdxBookInfoUserPreOrder(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo);
    int delTSdxBookInfoUserPreOrderByIds(Long... ids);
    TSdxBookInfoUserPreOrderPo findTSdxBookInfoUserPreOrderById(Long id);
    List<TSdxBookInfoUserPreOrderPo> findTSdxBookInfoUserPreOrderByAll(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo, Integer page, Integer size);

	List<TSdxBookInfoUserPreOrderPo> selectByBookInfoId(Long id);

	List<TSdxBookInfoUserPreOrderPo> selectByUserIdAndBookInfoId(Long userId, Long bookInfoId);

	int update(List<TSdxBookInfoUserPreOrderPo> toUpdater);
}
