package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxInviterPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxInviterVo;
import java.util.List;
public interface SdxInviterService {
    long modTSdxInviter(TSdxInviterPo tSdxInviterPo);
    int delTSdxInviterByIds(Long... ids);
    TSdxInviterVo findTSdxInviterById(Long id);
    List<TSdxInviterVo> findTSdxInviterByAll(TSdxInviterPo tSdxInviterPo, Integer page, Integer size);
}
