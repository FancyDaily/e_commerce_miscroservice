package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookAfterReadingNoteDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍读后感的service层
*
*/

@Service
@Log
public class TSdxBookAfterReadingNoteServiceImpl implements TSdxBookAfterReadingNoteService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookAfterReadingNoteDao tSdxBookAfterReadingNoteDao;
    @Override
    public long modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo) {
        if (tSdxBookAfterReadingNotePo == null) {
            log.warn("操作书籍读后感参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookAfterReadingNotePo.getId() == null) {
            log.info("start添加书籍读后感={}", tSdxBookAfterReadingNotePo);
            int result = tSdxBookAfterReadingNoteDao.saveTSdxBookAfterReadingNoteIfNotExist(tSdxBookAfterReadingNotePo);
            return result != 0 ? tSdxBookAfterReadingNotePo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍读后感={}", tSdxBookAfterReadingNotePo.getId());
            return tSdxBookAfterReadingNoteDao.modTSdxBookAfterReadingNote(tSdxBookAfterReadingNotePo);
        }
    }
    @Override
    public int delTSdxBookAfterReadingNoteByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍读后感,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍读后感", Arrays.asList(ids));
        return tSdxBookAfterReadingNoteDao.delTSdxBookAfterReadingNoteByIds(ids);
    }
    @Override
    public TSdxBookAfterReadingNoteVo findTSdxBookAfterReadingNoteById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍读后感,所传Id不符合规范");
            return new TSdxBookAfterReadingNoteVo();
        }
        log.info("start根据Id={}查找书籍读后感", id);
        TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = tSdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteById(id);
        return tSdxBookAfterReadingNotePo==null?new TSdxBookAfterReadingNoteVo():tSdxBookAfterReadingNotePo.copyTSdxBookAfterReadingNoteVo() ;
    }
    @Override
    public List<TSdxBookAfterReadingNoteVo> findTSdxBookAfterReadingNoteByAll(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo,Integer page, Integer size) {
        List    <TSdxBookAfterReadingNoteVo> tSdxBookAfterReadingNoteVos = new ArrayList<>();
        if (tSdxBookAfterReadingNotePo == null) {
            log.warn("根据条件查找书籍读后感,参数不对");
            return tSdxBookAfterReadingNoteVos;
        }
        log.info("start根据条件查找书籍读后感={}", tSdxBookAfterReadingNotePo);
        List        <TSdxBookAfterReadingNotePo> tSdxBookAfterReadingNotePos = tSdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteByAll(            tSdxBookAfterReadingNotePo,page,size);
        for (TSdxBookAfterReadingNotePo po : tSdxBookAfterReadingNotePos) {
            tSdxBookAfterReadingNoteVos.add(po.copyTSdxBookAfterReadingNoteVo());
        }
        return tSdxBookAfterReadingNoteVos;
    }



}
