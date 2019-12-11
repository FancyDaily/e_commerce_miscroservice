package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import java.util.List;
public interface TSdxCommonsDao {
    int saveTSdxCommonsIfNotExist(TSdxCommonsPo tSdxCommonsPo);
    int modTSdxCommons(TSdxCommonsPo tSdxCommonsPo);
    int delTSdxCommonsByIds(Long... ids);
    TSdxCommonsPo findTSdxCommonsById(Long id);
    List<TSdxCommonsPo> findTSdxCommonsByAll(TSdxCommonsPo tSdxCommonsPo, Integer page, Integer size);
}
