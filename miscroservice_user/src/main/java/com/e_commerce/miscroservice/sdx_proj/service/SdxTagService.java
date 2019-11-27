package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTagPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxTagVo;
import java.util.List;
public interface SdxTagService {
    long modTSdxTag(TSdxTagPo tSdxTagPo);
    int delTSdxTagByIds(Long... ids);
    TSdxTagVo findTSdxTagById(Long id);
    List<TSdxTagVo> findTSdxTagByAll(TSdxTagPo tSdxTagPo, Integer page, Integer size);
}
