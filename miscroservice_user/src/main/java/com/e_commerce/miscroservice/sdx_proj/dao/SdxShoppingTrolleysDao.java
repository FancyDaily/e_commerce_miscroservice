package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import java.util.List;
public interface SdxShoppingTrolleysDao {
    int saveTSdxShoppingTrolleysIfNotExist(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo);
    int modTSdxShoppingTrolleys(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo);
    int delTSdxShoppingTrolleysByIds(Long... ids);

	int delTSdxShoppingTrolleysByIds(List<Long> ids);

	TSdxShoppingTrolleysPo findTSdxShoppingTrolleysById(Long id);
    List<TSdxShoppingTrolleysPo> findTSdxShoppingTrolleysByAll(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo, Integer page, Integer size);

	TSdxShoppingTrolleysPo selectByPo(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo);

	List<TSdxShoppingTrolleysPo> selectByUserId(Long id);

	List<TSdxShoppingTrolleysPo> selectByUserIdPage(Long id, Integer pageNum, Integer pageSize);

	List<TSdxShoppingTrolleysPo> selectByUserIdInBookInfoIds(Long userId, List<Long> bookInfoIdList);
}
