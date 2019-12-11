package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxThuPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxThuVo;
import java.util.List;
public interface TSdxThuService {
    long modTSdxThu(TSdxThuPo tSdxThuPo);
    int delTSdxThuByIds(Long... ids);
    TSdxThuVo findTSdxThuById(Long id);
    List<TSdxThuVo> findTSdxThuByAll(TSdxThuPo tSdxThuPo, Integer page, Integer size);
}
