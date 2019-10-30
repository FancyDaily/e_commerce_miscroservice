package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import java.util.List;
public interface TSdxBookTransRecordDao {
    int saveTSdxBookTransRecordIfNotExist(TSdxBookTransRecordPo tSdxBookTransRecordPo);
    int modTSdxBookTransRecord(TSdxBookTransRecordPo tSdxBookTransRecordPo);
    int delTSdxBookTransRecordByIds(Long... ids);
    TSdxBookTransRecordPo findTSdxBookTransRecordById(Long id);
    List<TSdxBookTransRecordPo> findTSdxBookTransRecordByAll(TSdxBookTransRecordPo tSdxBookTransRecordPo, Integer page, Integer size);
}
