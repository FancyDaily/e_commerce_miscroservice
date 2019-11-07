package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookStationPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookStationVo;
import java.util.List;
public interface SdxBookStationService {
    long modTSdxBookStation(TSdxBookStationPo tSdxBookStationPo);
    int delTSdxBookStationByIds(Long... ids);
    TSdxBookStationVo findTSdxBookStationById(Long id);
    List<TSdxBookStationVo> findTSdxBookStationByAll(TSdxBookStationPo tSdxBookStationPo, Integer page, Integer size);

	QueryResult list(String city, Double longitude, Double latitude, Integer pageNum, Integer pageSize);
}
