package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookInfoUserPreOrderDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoUserPreOrderPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookInfoUserPreOrderService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoUserPreOrderVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍信息预定(书籍信息用户关系)的service层
*
*/

@Service
@Log
public class TSdxBookInfoUserPreOrderServiceImpl implements TSdxBookInfoUserPreOrderService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookInfoUserPreOrderDao tSdxBookInfoUserPreOrderDao;
    @Override
    public long modTSdxBookInfoUserPreOrder(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo) {
        if (tSdxBookInfoUserPreOrderPo == null) {
            log.warn("操作书籍信息预定(书籍信息用户关系)参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookInfoUserPreOrderPo.getId() == null) {
            log.info("start添加书籍信息预定(书籍信息用户关系)={}", tSdxBookInfoUserPreOrderPo);
            int result = tSdxBookInfoUserPreOrderDao.saveTSdxBookInfoUserPreOrderIfNotExist(tSdxBookInfoUserPreOrderPo);
            return result != 0 ? tSdxBookInfoUserPreOrderPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍信息预定(书籍信息用户关系)={}", tSdxBookInfoUserPreOrderPo.getId());
            return tSdxBookInfoUserPreOrderDao.modTSdxBookInfoUserPreOrder(tSdxBookInfoUserPreOrderPo);
        }
    }
    @Override
    public int delTSdxBookInfoUserPreOrderByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍信息预定(书籍信息用户关系),ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍信息预定(书籍信息用户关系)", Arrays.asList(ids));
        return tSdxBookInfoUserPreOrderDao.delTSdxBookInfoUserPreOrderByIds(ids);
    }
    @Override
    public TSdxBookInfoUserPreOrderVo findTSdxBookInfoUserPreOrderById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍信息预定(书籍信息用户关系),所传Id不符合规范");
            return new TSdxBookInfoUserPreOrderVo();
        }
        log.info("start根据Id={}查找书籍信息预定(书籍信息用户关系)", id);
        TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo = tSdxBookInfoUserPreOrderDao.findTSdxBookInfoUserPreOrderById(id);
        return tSdxBookInfoUserPreOrderPo==null?new TSdxBookInfoUserPreOrderVo():tSdxBookInfoUserPreOrderPo.copyTSdxBookInfoUserPreOrderVo() ;
    }
    @Override
    public List<TSdxBookInfoUserPreOrderVo> findTSdxBookInfoUserPreOrderByAll(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo,Integer page, Integer size) {
        List    <TSdxBookInfoUserPreOrderVo> tSdxBookInfoUserPreOrderVos = new ArrayList<>();
        if (tSdxBookInfoUserPreOrderPo == null) {
            log.warn("根据条件查找书籍信息预定(书籍信息用户关系),参数不对");
            return tSdxBookInfoUserPreOrderVos;
        }
        log.info("start根据条件查找书籍信息预定(书籍信息用户关系)={}", tSdxBookInfoUserPreOrderPo);
        List        <TSdxBookInfoUserPreOrderPo> tSdxBookInfoUserPreOrderPos = tSdxBookInfoUserPreOrderDao.findTSdxBookInfoUserPreOrderByAll(            tSdxBookInfoUserPreOrderPo,page,size);
        for (TSdxBookInfoUserPreOrderPo po : tSdxBookInfoUserPreOrderPos) {
            tSdxBookInfoUserPreOrderVos.add(po.copyTSdxBookInfoUserPreOrderVo());
        }
        return tSdxBookInfoUserPreOrderVos;
    }
}
