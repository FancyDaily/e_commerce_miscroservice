package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxWishListDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxWishListPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxWishListService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxWishListVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 心愿单的service层
*
*/

@Service
@Log
public class TSdxWishListServiceImpl implements TSdxWishListService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxWishListDao tSdxWishListDao;
    @Override
    public long modTSdxWishList(TSdxWishListPo tSdxWishListPo) {
        if (tSdxWishListPo == null) {
            log.warn("操作心愿单参数为空");
            return ERROR_LONG;
        }
        if (tSdxWishListPo.getId() == null) {
            log.info("start添加心愿单={}", tSdxWishListPo);
            int result = tSdxWishListDao.saveTSdxWishListIfNotExist(tSdxWishListPo);
            return result != 0 ? tSdxWishListPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改心愿单={}", tSdxWishListPo.getId());
            return tSdxWishListDao.modTSdxWishList(tSdxWishListPo);
        }
    }
    @Override
    public int delTSdxWishListByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除心愿单,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},心愿单", Arrays.asList(ids));
        return tSdxWishListDao.delTSdxWishListByIds(ids);
    }
    @Override
    public TSdxWishListVo findTSdxWishListById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找心愿单,所传Id不符合规范");
            return new TSdxWishListVo();
        }
        log.info("start根据Id={}查找心愿单", id);
        TSdxWishListPo tSdxWishListPo = tSdxWishListDao.findTSdxWishListById(id);
        return tSdxWishListPo==null?new TSdxWishListVo():tSdxWishListPo.copyTSdxWishListVo() ;
    }
    @Override
    public List<TSdxWishListVo> findTSdxWishListByAll(TSdxWishListPo tSdxWishListPo,Integer page, Integer size) {
        List    <TSdxWishListVo> tSdxWishListVos = new ArrayList<>();
        if (tSdxWishListPo == null) {
            log.warn("根据条件查找心愿单,参数不对");
            return tSdxWishListVos;
        }
        log.info("start根据条件查找心愿单={}", tSdxWishListPo);
        List        <TSdxWishListPo> tSdxWishListPos = tSdxWishListDao.findTSdxWishListByAll(            tSdxWishListPo,page,size);
        for (TSdxWishListPo po : tSdxWishListPos) {
            tSdxWishListVos.add(po.copyTSdxWishListVo());
        }
        return tSdxWishListVos;
    }
}
