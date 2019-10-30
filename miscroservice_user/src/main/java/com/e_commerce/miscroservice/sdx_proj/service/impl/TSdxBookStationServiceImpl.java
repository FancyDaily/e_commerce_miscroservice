package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookStationDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookStationPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookStationService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookStationVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍回收驿站的service层
*
*/

@Service
@Log
public class TSdxBookStationServiceImpl implements TSdxBookStationService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookStationDao tSdxBookStationDao;
    @Override
    public long modTSdxBookStation(TSdxBookStationPo tSdxBookStationPo) {
        if (tSdxBookStationPo == null) {
            log.warn("操作书籍回收驿站参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookStationPo.getId() == null) {
            log.info("start添加书籍回收驿站={}", tSdxBookStationPo);
            int result = tSdxBookStationDao.saveTSdxBookStationIfNotExist(tSdxBookStationPo);
            return result != 0 ? tSdxBookStationPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍回收驿站={}", tSdxBookStationPo.getId());
            return tSdxBookStationDao.modTSdxBookStation(tSdxBookStationPo);
        }
    }
    @Override
    public int delTSdxBookStationByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍回收驿站,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍回收驿站", Arrays.asList(ids));
        return tSdxBookStationDao.delTSdxBookStationByIds(ids);
    }
    @Override
    public TSdxBookStationVo findTSdxBookStationById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍回收驿站,所传Id不符合规范");
            return new TSdxBookStationVo();
        }
        log.info("start根据Id={}查找书籍回收驿站", id);
        TSdxBookStationPo tSdxBookStationPo = tSdxBookStationDao.findTSdxBookStationById(id);
        return tSdxBookStationPo==null?new TSdxBookStationVo():tSdxBookStationPo.copyTSdxBookStationVo() ;
    }
    @Override
    public List<TSdxBookStationVo> findTSdxBookStationByAll(TSdxBookStationPo tSdxBookStationPo,Integer page, Integer size) {
        List    <TSdxBookStationVo> tSdxBookStationVos = new ArrayList<>();
        if (tSdxBookStationPo == null) {
            log.warn("根据条件查找书籍回收驿站,参数不对");
            return tSdxBookStationVos;
        }
        log.info("start根据条件查找书籍回收驿站={}", tSdxBookStationPo);
        List        <TSdxBookStationPo> tSdxBookStationPos = tSdxBookStationDao.findTSdxBookStationByAll(            tSdxBookStationPo,page,size);
        for (TSdxBookStationPo po : tSdxBookStationPos) {
            tSdxBookStationVos.add(po.copyTSdxBookStationVo());
        }
        return tSdxBookStationVos;
    }
}
