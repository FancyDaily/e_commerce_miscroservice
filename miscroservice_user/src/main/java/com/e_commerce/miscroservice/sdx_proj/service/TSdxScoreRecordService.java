package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxScoreRecordVo;
import java.util.List;
public interface TSdxScoreRecordService {
    long modTSdxScoreRecord(TSdxScoreRecordPo tSdxScoreRecordPo);
    int delTSdxScoreRecordByIds(Long... ids);
    TSdxScoreRecordVo findTSdxScoreRecordById(Long id);
    List<TSdxScoreRecordVo> findTSdxScoreRecordByAll(TSdxScoreRecordPo tSdxScoreRecordPo, Integer page, Integer size);
}
