package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import java.util.List;
public interface TSdxBookAfterReadingNoteDao {
    int saveTSdxBookAfterReadingNoteIfNotExist(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo);
    int modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo);
    int delTSdxBookAfterReadingNoteByIds(Long... ids);
    TSdxBookAfterReadingNotePo findTSdxBookAfterReadingNoteById(Long id);
    List<TSdxBookAfterReadingNotePo> findTSdxBookAfterReadingNoteByAll(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo, Integer page, Integer size);

	List<TSdxBookAfterReadingNotePo> selectByBookInfoId(Long id);

	List<TSdxBookAfterReadingNotePo> selectByBookInfoIdPage(Long id, Integer pageNum, Integer pageSize);

	List<TSdxBookAfterReadingNotePo> selectByBookInfoIdPageDesc(Long id, Integer pageNum, Integer pageSize);
}
