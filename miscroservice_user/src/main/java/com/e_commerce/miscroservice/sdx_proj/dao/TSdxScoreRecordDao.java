package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import java.util.List;
public interface TSdxScoreRecordDao {
    int saveTSdxScoreRecordIfNotExist(TSdxScoreRecordPo tSdxScoreRecordPo);
    int modTSdxScoreRecord(TSdxScoreRecordPo tSdxScoreRecordPo);
    int delTSdxScoreRecordByIds(Long... ids);
    TSdxScoreRecordPo findTSdxScoreRecordById(Long id);
    List<TSdxScoreRecordPo> findTSdxScoreRecordByAll(TSdxScoreRecordPo tSdxScoreRecordPo, Integer page, Integer size);
}
