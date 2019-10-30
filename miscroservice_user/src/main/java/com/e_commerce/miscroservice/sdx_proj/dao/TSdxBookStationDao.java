package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookStationPo;
import java.util.List;
public interface TSdxBookStationDao {
    int saveTSdxBookStationIfNotExist(TSdxBookStationPo tSdxBookStationPo);
    int modTSdxBookStation(TSdxBookStationPo tSdxBookStationPo);
    int delTSdxBookStationByIds(Long... ids);
    TSdxBookStationPo findTSdxBookStationById(Long id);
    List<TSdxBookStationPo> findTSdxBookStationByAll(TSdxBookStationPo tSdxBookStationPo, Integer page, Integer size);
}
