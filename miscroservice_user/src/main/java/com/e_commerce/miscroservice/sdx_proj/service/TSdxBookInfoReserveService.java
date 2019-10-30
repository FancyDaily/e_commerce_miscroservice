package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoReservePo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoReserveVo;
import java.util.List;
public interface TSdxBookInfoReserveService {
    long modTSdxBookInfoReserve(TSdxBookInfoReservePo tSdxBookInfoReservePo);
    int delTSdxBookInfoReserveByIds(Long... ids);
    TSdxBookInfoReserveVo findTSdxBookInfoReserveById(Long id);
    List<TSdxBookInfoReserveVo> findTSdxBookInfoReserveByAll(TSdxBookInfoReservePo tSdxBookInfoReservePo, Integer page, Integer size);
}
