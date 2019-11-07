package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoReservePo;
import java.util.List;
public interface SdxBookInfoReserveDao {
    int saveTSdxBookInfoReserveIfNotExist(TSdxBookInfoReservePo tSdxBookInfoReservePo);
    int modTSdxBookInfoReserve(TSdxBookInfoReservePo tSdxBookInfoReservePo);
    int delTSdxBookInfoReserveByIds(Long... ids);
    TSdxBookInfoReservePo findTSdxBookInfoReserveById(Long id);
    List<TSdxBookInfoReservePo> findTSdxBookInfoReserveByAll(TSdxBookInfoReservePo tSdxBookInfoReservePo, Integer page, Integer size);
}
