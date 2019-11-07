package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteUserVo;
import java.util.List;
public interface SdxBookAfterReadingNoteUserService {
    long modTSdxBookAfterReadingNoteUser(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo);
    int delTSdxBookAfterReadingNoteUserByIds(Long... ids);
    TSdxBookAfterReadingNoteUserVo findTSdxBookAfterReadingNoteUserById(Long id);
    List<TSdxBookAfterReadingNoteUserVo> findTSdxBookAfterReadingNoteUserByAll(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo, Integer page, Integer size);
}
