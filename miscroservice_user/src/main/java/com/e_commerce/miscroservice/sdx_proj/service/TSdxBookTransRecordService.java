package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTransRecordVo;
import java.util.List;
public interface TSdxBookTransRecordService {
    long modTSdxBookTransRecord(TSdxBookTransRecordPo tSdxBookTransRecordPo);
    int delTSdxBookTransRecordByIds(Long... ids);
    TSdxBookTransRecordVo findTSdxBookTransRecordById(Long id);
    List<TSdxBookTransRecordVo> findTSdxBookTransRecordByAll(TSdxBookTransRecordPo tSdxBookTransRecordPo, Integer page, Integer size);
}
