package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookAfterReadingNoteUserDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookAfterReadingNoteUserService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteUserVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍读后感用户相关的service层
*
*/

@Service
@Log
public class TSdxBookAfterReadingNoteUserServiceImpl implements TSdxBookAfterReadingNoteUserService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookAfterReadingNoteUserDao tSdxBookAfterReadingNoteUserDao;
    @Override
    public long modTSdxBookAfterReadingNoteUser(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo) {
        if (tSdxBookAfterReadingNoteUserPo == null) {
            log.warn("操作书籍读后感用户相关参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookAfterReadingNoteUserPo.getId() == null) {
            log.info("start添加书籍读后感用户相关={}", tSdxBookAfterReadingNoteUserPo);
            int result = tSdxBookAfterReadingNoteUserDao.saveTSdxBookAfterReadingNoteUserIfNotExist(tSdxBookAfterReadingNoteUserPo);
            return result != 0 ? tSdxBookAfterReadingNoteUserPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍读后感用户相关={}", tSdxBookAfterReadingNoteUserPo.getId());
            return tSdxBookAfterReadingNoteUserDao.modTSdxBookAfterReadingNoteUser(tSdxBookAfterReadingNoteUserPo);
        }
    }
    @Override
    public int delTSdxBookAfterReadingNoteUserByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍读后感用户相关,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍读后感用户相关", Arrays.asList(ids));
        return tSdxBookAfterReadingNoteUserDao.delTSdxBookAfterReadingNoteUserByIds(ids);
    }
    @Override
    public TSdxBookAfterReadingNoteUserVo findTSdxBookAfterReadingNoteUserById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍读后感用户相关,所传Id不符合规范");
            return new TSdxBookAfterReadingNoteUserVo();
        }
        log.info("start根据Id={}查找书籍读后感用户相关", id);
        TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo = tSdxBookAfterReadingNoteUserDao.findTSdxBookAfterReadingNoteUserById(id);
        return tSdxBookAfterReadingNoteUserPo==null?new TSdxBookAfterReadingNoteUserVo():tSdxBookAfterReadingNoteUserPo.copyTSdxBookAfterReadingNoteUserVo() ;
    }
    @Override
    public List<TSdxBookAfterReadingNoteUserVo> findTSdxBookAfterReadingNoteUserByAll(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo,Integer page, Integer size) {
        List    <TSdxBookAfterReadingNoteUserVo> tSdxBookAfterReadingNoteUserVos = new ArrayList<>();
        if (tSdxBookAfterReadingNoteUserPo == null) {
            log.warn("根据条件查找书籍读后感用户相关,参数不对");
            return tSdxBookAfterReadingNoteUserVos;
        }
        log.info("start根据条件查找书籍读后感用户相关={}", tSdxBookAfterReadingNoteUserPo);
        List        <TSdxBookAfterReadingNoteUserPo> tSdxBookAfterReadingNoteUserPos = tSdxBookAfterReadingNoteUserDao.findTSdxBookAfterReadingNoteUserByAll(            tSdxBookAfterReadingNoteUserPo,page,size);
        for (TSdxBookAfterReadingNoteUserPo po : tSdxBookAfterReadingNoteUserPos) {
            tSdxBookAfterReadingNoteUserVos.add(po.copyTSdxBookAfterReadingNoteUserVo());
        }
        return tSdxBookAfterReadingNoteUserVos;
    }
}
