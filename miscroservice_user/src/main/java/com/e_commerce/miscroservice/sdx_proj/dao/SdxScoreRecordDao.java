package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import java.util.List;
public interface SdxScoreRecordDao {
    int saveTSdxScoreRecordIfNotExist(TSdxScoreRecordPo tSdxScoreRecordPo);

	int save(TSdxScoreRecordPo po);

	int modTSdxScoreRecord(TSdxScoreRecordPo tSdxScoreRecordPo);
    int delTSdxScoreRecordByIds(Long... ids);
    TSdxScoreRecordPo findTSdxScoreRecordById(Long id);
    List<TSdxScoreRecordPo> findTSdxScoreRecordByAll(TSdxScoreRecordPo tSdxScoreRecordPo, Integer page, Integer size);

	List<TSdxScoreRecordPo> selectByUserIdPage(Long userId, Integer pageNum, Integer pageSize);

	List<TSdxScoreRecordPo> selectByUserId(Long userId);

	List<TSdxScoreRecordPo> selectByUserId(Long userId, Integer pageNum, Integer pageSize, boolean isPage);

	List<TSdxScoreRecordPo> selectByUserIdAndInOutPage(Long userId, Integer inOut, Integer pageNum, Integer pageSize);
}
