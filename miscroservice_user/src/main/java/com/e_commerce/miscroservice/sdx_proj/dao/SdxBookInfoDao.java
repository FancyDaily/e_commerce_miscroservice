package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import java.util.List;
public interface SdxBookInfoDao {
    int saveTSdxBookInfoIfNotExist(TSdxBookInfoPo tSdxBookInfoPo);
    int modTSdxBookInfo(TSdxBookInfoPo tSdxBookInfoPo);
    int delTSdxBookInfoByIds(Long... ids);
    TSdxBookInfoPo findTSdxBookInfoById(Long id);
    List<TSdxBookInfoPo> findTSdxBookInfoByAll(TSdxBookInfoPo tSdxBookInfoPo, Integer page, Integer size, Integer sortType);

	TSdxBookInfoPo selectByPrimaryKey(Long id);

	TSdxBookInfoPo selectByName(String name);

	List<TSdxBookInfoPo> selectInIds(Long... bookInfoIds);

	List<TSdxBookInfoPo> selectInIds(List<Long> bookInfoIds);

	List<TSdxBookInfoPo> selectInServiceIds(List<Long> serviceIds);
}
