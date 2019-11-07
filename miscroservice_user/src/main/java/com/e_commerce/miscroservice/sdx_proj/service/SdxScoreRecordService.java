package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxScoreRecordVo;
import java.util.List;
public interface SdxScoreRecordService {
    long modTSdxScoreRecord(TSdxScoreRecordPo tSdxScoreRecordPo);
    int delTSdxScoreRecordByIds(Long... ids);
    TSdxScoreRecordVo findTSdxScoreRecordById(Long id);
    List<TSdxScoreRecordVo> findTSdxScoreRecordByAll(TSdxScoreRecordPo tSdxScoreRecordPo, Integer page, Integer size);

	void dealWithScoreOutTypeBARN(TCsqUser csqUser, Integer forSaleScore);

	QueryResult list(Long id, Integer pageNum, Integer pageSize, Integer option);

	void dealWithScoreOut(TSdxBookOrderPo order);
}
