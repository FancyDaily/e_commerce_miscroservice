package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import java.util.List;
public interface TSdxBookAfterReadingNoteService {
    long modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo);
    int delTSdxBookAfterReadingNoteByIds(Long... ids);
    TSdxBookAfterReadingNoteVo findTSdxBookAfterReadingNoteById(Long id);
    List<TSdxBookAfterReadingNoteVo> findTSdxBookAfterReadingNoteByAll(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo, Integer page, Integer size);

	void buy(Long bArnId, Long userId);
}
