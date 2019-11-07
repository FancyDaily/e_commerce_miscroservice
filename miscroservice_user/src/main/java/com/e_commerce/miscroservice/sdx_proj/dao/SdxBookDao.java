package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import java.util.List;
public interface SdxBookDao {
    int saveTSdxBookIfNotExist(TSdxBookPo tSdxBookPo);
    int modTSdxBook(TSdxBookPo tSdxBookPo);
    int delTSdxBookByIds(Long... ids);
    TSdxBookPo findTSdxBookById(Long id);
    List<TSdxBookPo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size);

	List<TSdxBookPo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size, Integer sortType);

	List<TSdxBookPo> selectByBookInfoIdAndStatus(long bookInfoId, int status);

	List<TSdxBookPo> selectByBookInfoIdInStatus(long bookInfoId, List<Integer> status);

	List<TSdxBookPo> selectInIds(List<Long> bookIds);

	int update(List<TSdxBookPo> toUpdaters);
}
