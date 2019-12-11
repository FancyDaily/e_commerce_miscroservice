package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxThuPo;
import java.util.List;
public interface TSdxThuDao {
    int saveTSdxThuIfNotExist(TSdxThuPo tSdxThuPo);
    int modTSdxThu(TSdxThuPo tSdxThuPo);
    int delTSdxThuByIds(Long... ids);
    TSdxThuPo findTSdxThuById(Long id);
    List<TSdxThuPo> findTSdxThuByAll(TSdxThuPo tSdxThuPo, Integer page, Integer size);
}
