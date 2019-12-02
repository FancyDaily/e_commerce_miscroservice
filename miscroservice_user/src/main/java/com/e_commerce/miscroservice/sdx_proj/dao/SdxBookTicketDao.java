package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTicketPo;
import java.util.List;
public interface SdxBookTicketDao {
    int saveTSdxBookTicktIfNotExist(TSdxBookTicketPo tSdxBookTicketPo);
    int modTSdxBookTickt(TSdxBookTicketPo tSdxBookTicketPo);
    int delTSdxBookTicktByIds(Long... ids);
    TSdxBookTicketPo findTSdxBookTicktById(Long id);
    List<TSdxBookTicketPo> findTSdxBookTicktByAll(TSdxBookTicketPo tSdxBookTicketPo, Integer page, Integer size);

	List<TSdxBookTicketPo> selectByUserId(Long userId);

	List<TSdxBookTicketPo> selectByUserIdAndIsUsed(Long userId, int code);
}
