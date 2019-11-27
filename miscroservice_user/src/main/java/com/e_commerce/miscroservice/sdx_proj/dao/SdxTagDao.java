package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTagPo;
import java.util.List;
public interface SdxTagDao {
    int saveTSdxTagIfNotExist(TSdxTagPo tSdxTagPo);
    int modTSdxTag(TSdxTagPo tSdxTagPo);
    int delTSdxTagByIds(Long... ids);
    TSdxTagPo findTSdxTagById(Long id);
    List<TSdxTagPo> findTSdxTagByAll(TSdxTagPo tSdxTagPo, Integer page, Integer size);

	List<TSdxTagPo> selectInNames(String... tagNames);
}
