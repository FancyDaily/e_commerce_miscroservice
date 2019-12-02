package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import java.util.List;
public interface SdxBookAfterReadingNoteUserDao {
    int saveTSdxBookAfterReadingNoteUserIfNotExist(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo);
    int modTSdxBookAfterReadingNoteUser(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo);
    int delTSdxBookAfterReadingNoteUserByIds(Long... ids);
    TSdxBookAfterReadingNoteUserPo findTSdxBookAfterReadingNoteUserById(Long id);
    List<TSdxBookAfterReadingNoteUserPo> findTSdxBookAfterReadingNoteUserByAll(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo, Integer page, Integer size);

	List<TSdxBookAfterReadingNoteUserPo> selectInAfrdnIdsAndUserIdAndIsPurchase(List<Long> afrdnIds, Integer isPurchase, Long userId);

	TSdxBookAfterReadingNoteUserPo selectByBookAfrnIdAndUserIdAndType(Long bookArnId, Long userId, int type);

	int insert(TSdxBookAfterReadingNoteUserPo... afterReadingNoteUserPo);

	int insert(List<TSdxBookAfterReadingNoteUserPo> afterReadingNoteUserPo);

	int update(TSdxBookAfterReadingNoteUserPo afterReadingNoteUserPo);

	List<TSdxBookAfterReadingNoteUserPo> selectByBookAfrnIdAndUserIdAndIsThumb(Long afrId, Long userId, int code);

	TSdxBookAfterReadingNoteUserPo selectByBookAfrnIdAndUserIdAndIsThumbAndType(Long afrId, Long userId, int code, int code1);
}
