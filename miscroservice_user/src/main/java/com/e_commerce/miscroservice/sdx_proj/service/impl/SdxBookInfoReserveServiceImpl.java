package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookInfoReserveDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoReservePo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoReserveService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoReserveVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍预定信息的service层
*
*/

@Service
@Log
public class SdxBookInfoReserveServiceImpl implements SdxBookInfoReserveService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxBookInfoReserveDao sdxBookInfoReserveDao;
    @Override
    public long modTSdxBookInfoReserve(TSdxBookInfoReservePo tSdxBookInfoReservePo) {
        if (tSdxBookInfoReservePo == null) {
            log.warn("操作书籍预定信息参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookInfoReservePo.getId() == null) {
            log.info("start添加书籍预定信息={}", tSdxBookInfoReservePo);
            int result = sdxBookInfoReserveDao.saveTSdxBookInfoReserveIfNotExist(tSdxBookInfoReservePo);
            return result != 0 ? tSdxBookInfoReservePo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍预定信息={}", tSdxBookInfoReservePo.getId());
            return sdxBookInfoReserveDao.modTSdxBookInfoReserve(tSdxBookInfoReservePo);
        }
    }
    @Override
    public int delTSdxBookInfoReserveByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍预定信息,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍预定信息", Arrays.asList(ids));
        return sdxBookInfoReserveDao.delTSdxBookInfoReserveByIds(ids);
    }
    @Override
    public TSdxBookInfoReserveVo findTSdxBookInfoReserveById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍预定信息,所传Id不符合规范");
            return new TSdxBookInfoReserveVo();
        }
        log.info("start根据Id={}查找书籍预定信息", id);
        TSdxBookInfoReservePo tSdxBookInfoReservePo = sdxBookInfoReserveDao.findTSdxBookInfoReserveById(id);
        return tSdxBookInfoReservePo==null?new TSdxBookInfoReserveVo():tSdxBookInfoReservePo.copyTSdxBookInfoReserveVo() ;
    }
    @Override
    public List<TSdxBookInfoReserveVo> findTSdxBookInfoReserveByAll(TSdxBookInfoReservePo tSdxBookInfoReservePo,Integer page, Integer size) {
        List    <TSdxBookInfoReserveVo> tSdxBookInfoReserveVos = new ArrayList<>();
        if (tSdxBookInfoReservePo == null) {
            log.warn("根据条件查找书籍预定信息,参数不对");
            return tSdxBookInfoReserveVos;
        }
        log.info("start根据条件查找书籍预定信息={}", tSdxBookInfoReservePo);
        List        <TSdxBookInfoReservePo> tSdxBookInfoReservePos = sdxBookInfoReserveDao.findTSdxBookInfoReserveByAll(            tSdxBookInfoReservePo,page,size);
        for (TSdxBookInfoReservePo po : tSdxBookInfoReservePos) {
            tSdxBookInfoReserveVos.add(po.copyTSdxBookInfoReserveVo());
        }
        return tSdxBookInfoReserveVos;
    }
}
