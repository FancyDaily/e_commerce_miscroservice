package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import java.util.List;
public interface SdxBookAfterReadingNoteService {
    long modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo);
    int delTSdxBookAfterReadingNoteByIds(Long... ids);
    TSdxBookAfterReadingNoteVo findTSdxBookAfterReadingNoteById(Long id);
    List<TSdxBookAfterReadingNoteVo> findTSdxBookAfterReadingNoteByAll(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo, Integer page, Integer size);

	void buy(Long bArnId, Long userId);

	void thumb(Long bookAfterReadingId, Long id, Integer option);

	Integer getIndex(TSdxBookAfterReadingNotePo copyTSdxBookAfterReadingNotePo);
}
