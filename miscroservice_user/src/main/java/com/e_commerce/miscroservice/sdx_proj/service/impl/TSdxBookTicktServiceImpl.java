package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookTicketDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTicketPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookTicktService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTicktVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 预定书券的service层
*
*/

@Service
@Log
public class TSdxBookTicktServiceImpl implements TSdxBookTicktService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxBookTicketDao sdxBookTicketDao;
    @Override
    public long modTSdxBookTickt(TSdxBookTicketPo tSdxBookTicketPo) {
        if (tSdxBookTicketPo == null) {
            log.warn("操作预定书券参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookTicketPo.getId() == null) {
            log.info("start添加预定书券={}", tSdxBookTicketPo);
            int result = sdxBookTicketDao.saveTSdxBookTicktIfNotExist(tSdxBookTicketPo);
            return result != 0 ? tSdxBookTicketPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改预定书券={}", tSdxBookTicketPo.getId());
            return sdxBookTicketDao.modTSdxBookTickt(tSdxBookTicketPo);
        }
    }
    @Override
    public int delTSdxBookTicktByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除预定书券,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},预定书券", Arrays.asList(ids));
        return sdxBookTicketDao.delTSdxBookTicktByIds(ids);
    }
    @Override
    public TSdxBookTicktVo findTSdxBookTicktById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找预定书券,所传Id不符合规范");
            return new TSdxBookTicktVo();
        }
        log.info("start根据Id={}查找预定书券", id);
        TSdxBookTicketPo tSdxBookTicketPo = sdxBookTicketDao.findTSdxBookTicktById(id);
        return tSdxBookTicketPo ==null?new TSdxBookTicktVo(): tSdxBookTicketPo.copyTSdxBookTicktVo() ;
    }
    @Override
    public List<TSdxBookTicktVo> findTSdxBookTicktByAll(TSdxBookTicketPo tSdxBookTicketPo, Integer page, Integer size) {
        List    <TSdxBookTicktVo> tSdxBookTicktVos = new ArrayList<>();
        if (tSdxBookTicketPo == null) {
            log.warn("根据条件查找预定书券,参数不对");
            return tSdxBookTicktVos;
        }
        log.info("start根据条件查找预定书券={}", tSdxBookTicketPo);
        List        <TSdxBookTicketPo> tSdxBookTicketPos = sdxBookTicketDao.findTSdxBookTicktByAll(tSdxBookTicketPo,page,size);
        for (TSdxBookTicketPo po : tSdxBookTicketPos) {
            tSdxBookTicktVos.add(po.copyTSdxBookTicktVo());
        }
        return tSdxBookTicktVos;
    }
}
