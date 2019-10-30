package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import java.util.List;
public interface TSdxBookAfterReadingNoteUserDao {
    int saveTSdxBookAfterReadingNoteUserIfNotExist(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo);
    int modTSdxBookAfterReadingNoteUser(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo);
    int delTSdxBookAfterReadingNoteUserByIds(Long... ids);
    TSdxBookAfterReadingNoteUserPo findTSdxBookAfterReadingNoteUserById(Long id);
    List<TSdxBookAfterReadingNoteUserPo> findTSdxBookAfterReadingNoteUserByAll(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo, Integer page, Integer size);
}
