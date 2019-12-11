package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import java.util.List;
public interface SdxBookTransRecordDao {
    int saveTSdxBookTransRecordIfNotExist(TSdxBookTransRecordPo tSdxBookTransRecordPo);
    int modTSdxBookTransRecord(TSdxBookTransRecordPo tSdxBookTransRecordPo);
    int delTSdxBookTransRecordByIds(Long... ids);
    TSdxBookTransRecordPo findTSdxBookTransRecordById(Long id);
    List<TSdxBookTransRecordPo> findTSdxBookTransRecordByAll(TSdxBookTransRecordPo tSdxBookTransRecordPo, Integer page, Integer size);

	List<TSdxBookTransRecordPo> selectByBookId(Long bookId);

	List<TSdxBookTransRecordPo> selectByBookIdAndType(Long bookId, int type);

	int save(List<TSdxBookTransRecordPo> toInserter);

	List<TSdxBookTransRecordPo> selectByBookInfoIdAndType(Long bookInfoId, int code);

	List<TSdxBookTransRecordPo> selectByBookInfoIdAndUserId(Long bookInfoId, Long userId);

	List<TSdxBookTransRecordPo> selectByBookInfoIdAndTypeDesc(Long bookInfoId, int code);
}
