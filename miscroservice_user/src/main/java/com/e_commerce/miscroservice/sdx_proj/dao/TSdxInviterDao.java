package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxInviterPo;
import java.util.List;
public interface TSdxInviterDao {
    int saveTSdxInviterIfNotExist(TSdxInviterPo tSdxInviterPo);
    int modTSdxInviter(TSdxInviterPo tSdxInviterPo);
    int delTSdxInviterByIds(Long... ids);
    TSdxInviterPo findTSdxInviterById(Long id);
    List<TSdxInviterPo> findTSdxInviterByAll(TSdxInviterPo tSdxInviterPo, Integer page, Integer size);
}
