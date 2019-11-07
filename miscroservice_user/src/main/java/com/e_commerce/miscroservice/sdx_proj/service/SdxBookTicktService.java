package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTicketPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTicktVo;
import java.util.List;
public interface SdxBookTicktService {
    long modTSdxBookTickt(TSdxBookTicketPo tSdxBookTicketPo);
    int delTSdxBookTicktByIds(Long... ids);
    TSdxBookTicktVo findTSdxBookTicktById(Long id);
    List<TSdxBookTicktVo> findTSdxBookTicktByAll(TSdxBookTicketPo tSdxBookTicketPo, Integer page, Integer size);
}
