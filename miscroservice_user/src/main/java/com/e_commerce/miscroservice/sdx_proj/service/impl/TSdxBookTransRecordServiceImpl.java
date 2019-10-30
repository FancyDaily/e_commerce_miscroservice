package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookTransRecordDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookTransRecordService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTransRecordVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍漂流记录的service层
*
*/

@Service
@Log
public class TSdxBookTransRecordServiceImpl implements TSdxBookTransRecordService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookTransRecordDao tSdxBookTransRecordDao;
    @Override
    public long modTSdxBookTransRecord(TSdxBookTransRecordPo tSdxBookTransRecordPo) {
        if (tSdxBookTransRecordPo == null) {
            log.warn("操作书籍漂流记录参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookTransRecordPo.getId() == null) {
            log.info("start添加书籍漂流记录={}", tSdxBookTransRecordPo);
            int result = tSdxBookTransRecordDao.saveTSdxBookTransRecordIfNotExist(tSdxBookTransRecordPo);
            return result != 0 ? tSdxBookTransRecordPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍漂流记录={}", tSdxBookTransRecordPo.getId());
            return tSdxBookTransRecordDao.modTSdxBookTransRecord(tSdxBookTransRecordPo);
        }
    }
    @Override
    public int delTSdxBookTransRecordByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍漂流记录,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍漂流记录", Arrays.asList(ids));
        return tSdxBookTransRecordDao.delTSdxBookTransRecordByIds(ids);
    }
    @Override
    public TSdxBookTransRecordVo findTSdxBookTransRecordById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍漂流记录,所传Id不符合规范");
            return new TSdxBookTransRecordVo();
        }
        log.info("start根据Id={}查找书籍漂流记录", id);
        TSdxBookTransRecordPo tSdxBookTransRecordPo = tSdxBookTransRecordDao.findTSdxBookTransRecordById(id);
        return tSdxBookTransRecordPo==null?new TSdxBookTransRecordVo():tSdxBookTransRecordPo.copyTSdxBookTransRecordVo() ;
    }
    @Override
    public List<TSdxBookTransRecordVo> findTSdxBookTransRecordByAll(TSdxBookTransRecordPo tSdxBookTransRecordPo,Integer page, Integer size) {
        List    <TSdxBookTransRecordVo> tSdxBookTransRecordVos = new ArrayList<>();
        if (tSdxBookTransRecordPo == null) {
            log.warn("根据条件查找书籍漂流记录,参数不对");
            return tSdxBookTransRecordVos;
        }
        log.info("start根据条件查找书籍漂流记录={}", tSdxBookTransRecordPo);
        List        <TSdxBookTransRecordPo> tSdxBookTransRecordPos = tSdxBookTransRecordDao.findTSdxBookTransRecordByAll(            tSdxBookTransRecordPo,page,size);
        for (TSdxBookTransRecordPo po : tSdxBookTransRecordPos) {
            tSdxBookTransRecordVos.add(po.copyTSdxBookTransRecordVo());
        }
        return tSdxBookTransRecordVos;
    }
}
