package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookStationPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookStationVo;
import java.util.List;
public interface TSdxBookStationService {
    long modTSdxBookStation(TSdxBookStationPo tSdxBookStationPo);
    int delTSdxBookStationByIds(Long... ids);
    TSdxBookStationVo findTSdxBookStationById(Long id);
    List<TSdxBookStationVo> findTSdxBookStationByAll(TSdxBookStationPo tSdxBookStationPo, Integer page, Integer size);
}
