package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxCommonsVo;
import java.util.List;
public interface TSdxCommonsService {
    long modTSdxCommons(TSdxCommonsPo tSdxCommonsPo);
    int delTSdxCommonsByIds(Long... ids);
    TSdxCommonsVo findTSdxCommonsById(Long id);
    List<TSdxCommonsVo> findTSdxCommonsByAll(TSdxCommonsPo tSdxCommonsPo, Integer page, Integer size);
}
