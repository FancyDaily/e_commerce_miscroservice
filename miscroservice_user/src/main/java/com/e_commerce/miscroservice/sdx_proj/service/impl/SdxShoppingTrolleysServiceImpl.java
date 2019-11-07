package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxShoppingTrolleysDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxShoppingTrolleysService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShoppingTrolleysVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 购物车的service层
*
*/

@Service
@Log
public class SdxShoppingTrolleysServiceImpl implements SdxShoppingTrolleysService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxShoppingTrolleysDao sdxShoppingTrolleysDao;
    @Override
    public long modTSdxShoppingTrolleys(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo) {
        if (tSdxShoppingTrolleysPo == null) {
            log.warn("操作购物车参数为空");
            return ERROR_LONG;
        }
        if (tSdxShoppingTrolleysPo.getId() == null) {
            log.info("start添加购物车={}", tSdxShoppingTrolleysPo);
            int result = sdxShoppingTrolleysDao.saveTSdxShoppingTrolleysIfNotExist(tSdxShoppingTrolleysPo);
            return result != 0 ? tSdxShoppingTrolleysPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改购物车={}", tSdxShoppingTrolleysPo.getId());
            return sdxShoppingTrolleysDao.modTSdxShoppingTrolleys(tSdxShoppingTrolleysPo);
        }
    }
    @Override
    public int delTSdxShoppingTrolleysByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除购物车,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},购物车", Arrays.asList(ids));
        return sdxShoppingTrolleysDao.delTSdxShoppingTrolleysByIds(ids);
    }
    @Override
    public TSdxShoppingTrolleysVo findTSdxShoppingTrolleysById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找购物车,所传Id不符合规范");
            return new TSdxShoppingTrolleysVo();
        }
        log.info("start根据Id={}查找购物车", id);
        TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo = sdxShoppingTrolleysDao.findTSdxShoppingTrolleysById(id);
        return tSdxShoppingTrolleysPo==null?new TSdxShoppingTrolleysVo():tSdxShoppingTrolleysPo.copyTSdxShoppingTrolleysVo() ;
    }
    @Override
    public List<TSdxShoppingTrolleysVo> findTSdxShoppingTrolleysByAll(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo,Integer page, Integer size) {
        List    <TSdxShoppingTrolleysVo> tSdxShoppingTrolleysVos = new ArrayList<>();
        if (tSdxShoppingTrolleysPo == null) {
            log.warn("根据条件查找购物车,参数不对");
            return tSdxShoppingTrolleysVos;
        }
        log.info("start根据条件查找购物车={}", tSdxShoppingTrolleysPo);
        List        <TSdxShoppingTrolleysPo> tSdxShoppingTrolleysPos = sdxShoppingTrolleysDao.findTSdxShoppingTrolleysByAll(            tSdxShoppingTrolleysPo,page,size);
        for (TSdxShoppingTrolleysPo po : tSdxShoppingTrolleysPos) {
            tSdxShoppingTrolleysVos.add(po.copyTSdxShoppingTrolleysVo());
        }
        return tSdxShoppingTrolleysVos;
    }
}
