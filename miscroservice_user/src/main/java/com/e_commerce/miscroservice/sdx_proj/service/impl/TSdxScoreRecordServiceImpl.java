package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxScoreRecordDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxScoreRecordService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxScoreRecordVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 积分流水的service层
*
*/

@Service
@Log
public class TSdxScoreRecordServiceImpl implements TSdxScoreRecordService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxScoreRecordDao tSdxScoreRecordDao;
    @Override
    public long modTSdxScoreRecord(TSdxScoreRecordPo tSdxScoreRecordPo) {
        if (tSdxScoreRecordPo == null) {
            log.warn("操作积分流水参数为空");
            return ERROR_LONG;
        }
        if (tSdxScoreRecordPo.getId() == null) {
            log.info("start添加积分流水={}", tSdxScoreRecordPo);
            int result = tSdxScoreRecordDao.saveTSdxScoreRecordIfNotExist(tSdxScoreRecordPo);
            return result != 0 ? tSdxScoreRecordPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改积分流水={}", tSdxScoreRecordPo.getId());
            return tSdxScoreRecordDao.modTSdxScoreRecord(tSdxScoreRecordPo);
        }
    }
    @Override
    public int delTSdxScoreRecordByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除积分流水,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},积分流水", Arrays.asList(ids));
        return tSdxScoreRecordDao.delTSdxScoreRecordByIds(ids);
    }
    @Override
    public TSdxScoreRecordVo findTSdxScoreRecordById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找积分流水,所传Id不符合规范");
            return new TSdxScoreRecordVo();
        }
        log.info("start根据Id={}查找积分流水", id);
        TSdxScoreRecordPo tSdxScoreRecordPo = tSdxScoreRecordDao.findTSdxScoreRecordById(id);
        return tSdxScoreRecordPo==null?new TSdxScoreRecordVo():tSdxScoreRecordPo.copyTSdxScoreRecordVo() ;
    }
    @Override
    public List<TSdxScoreRecordVo> findTSdxScoreRecordByAll(TSdxScoreRecordPo tSdxScoreRecordPo,Integer page, Integer size) {
        List    <TSdxScoreRecordVo> tSdxScoreRecordVos = new ArrayList<>();
        if (tSdxScoreRecordPo == null) {
            log.warn("根据条件查找积分流水,参数不对");
            return tSdxScoreRecordVos;
        }
        log.info("start根据条件查找积分流水={}", tSdxScoreRecordPo);
        List        <TSdxScoreRecordPo> tSdxScoreRecordPos = tSdxScoreRecordDao.findTSdxScoreRecordByAll(            tSdxScoreRecordPo,page,size);
        for (TSdxScoreRecordPo po : tSdxScoreRecordPos) {
            tSdxScoreRecordVos.add(po.copyTSdxScoreRecordVo());
        }
        return tSdxScoreRecordVos;
    }
}
